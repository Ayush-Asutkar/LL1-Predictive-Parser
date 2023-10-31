package grammar;

import model.ProductionRule;
import model.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BaseGrammar implements Grammar {
    protected static final String EPSILON = String.valueOf('\u03B5');
    protected static final String END_OF_LINE_SYMBOL = "$";

    private String firstSymbol;
    private final List<ProductionRule> productionRules;

    //store the non-terminal symbols and terminal symbols
    private final Set<String> terminalSymbols;
    private final Set<String> nonTerminalSymbols;

    //First set and follow set of all Non-Terminals
    private final Map<String, Set<String>> firstSet;
    private final Map<String, Set<String>> followSet;

    public BaseGrammar() {
        this.productionRules = new ArrayList<>();
        this.terminalSymbols = new HashSet<>();
        this.terminalSymbols.add(END_OF_LINE_SYMBOL);
        this.nonTerminalSymbols = new HashSet<>();
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
    }

    @Override
    public String getFirstSymbol() {
        return firstSymbol;
    }

    @Override
    public void setFirstSymbol(String firstSymbol) {
        this.firstSymbol = firstSymbol;
    }

    @Override
    public List<ProductionRule> getProductionRules() {
        return Collections.unmodifiableList(productionRules);
    }
    
    private ProductionRule getProductionRuleBasedOnNonTerminal(String symbol) {
        if(this.productionRules.contains(new ProductionRule(symbol))) {
            int index = this.productionRules.indexOf(new ProductionRule(symbol));
            return this.productionRules.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void addRule (String leftHandSide, Set<List<String>> rightHandSide) {
        ProductionRule alreadyExistingProductionRule = this.getProductionRuleBasedOnNonTerminal(leftHandSide);
        
        if (alreadyExistingProductionRule == null) {
            ProductionRule newProductionRule = new ProductionRule(leftHandSide);
            newProductionRule.addAllRightHandSide(rightHandSide);
            this.productionRules.add(newProductionRule);
        } else {
            alreadyExistingProductionRule.addAllRightHandSide(rightHandSide);
        }
    }

    /**
     * To add the production rule to the grammar
     * @param rule is of format: A -> B | C | D
     */
    @Override
    public void addRule (String rule) {
//        System.out.println(rule);


        String[] ruleSplit = rule.split("->");
        String leftSide = ruleSplit[0];
        leftSide = leftSide.trim();
//        System.out.println(leftSide);


        String[] rightSide = ruleSplit[1].split("\\|");

//        System.out.println(Arrays.toString(rightSide));

        Set<List<String>> rightFinal = new HashSet<>();

        for (String right: rightSide) {
            right = right.trim();
            String[] symbols = right.split(" ");
            rightFinal.add(new ArrayList<>(List.of(symbols)));
        }
        this.addRule(leftSide, rightFinal);
    }

    @Override
    public Set<String> getTerminalSymbols() {
        return Collections.unmodifiableSet(terminalSymbols);
    }

    @Override
    public void addTerminalSymbol(String str) {
        this.terminalSymbols.add(str);
    }

    @Override
    public void addAllTerminalSymbolFromIterator(Iterator<String> iterator) {
        while(iterator.hasNext()) {
            this.addTerminalSymbol(iterator.next());
        }
    }

    @Override
    public boolean isTerminalSymbol(String str) {
        return this.terminalSymbols.contains(str);
    }

    @Override
    public Set<String> getNonTerminalSymbols() {
        return Collections.unmodifiableSet(nonTerminalSymbols);
    }

    @Override
    public void addNonTerminalSymbol(String str) {
        this.nonTerminalSymbols.add(str);
    }

    @Override
    public void addAllNonTerminalSymbolFromIterator(Iterator<String> iterator) {
        while(iterator.hasNext()) {
            this.addNonTerminalSymbol(iterator.next());
        }
    }

    @Override
    public boolean isNonTerminalSymbol(String str) {
        return this.nonTerminalSymbols.contains(str);
    }

    @Override
    public void addFirstSet(String symbol, String firstSetSymbol) {
        if (this.firstSet.containsKey(symbol)) {
            this.firstSet.get(symbol).add(firstSetSymbol);
        } else {
            Set<String> toAdd = new HashSet<>();
            toAdd.add(firstSetSymbol);
            this.firstSet.put(symbol, toAdd);
        }
    }

    @Override
    public void addAllFirstSet(String symbol, Set<String> firstSet) {
        for(String first: firstSet) {
            this.addFirstSet(symbol, first);
        }
    }

    @Override
    public Set<String> getFirstSet(String symbol) {
        if (!this.firstSet.containsKey(symbol)) {
            return null;
        }
        return Collections.unmodifiableSet(this.firstSet.get(symbol));
    }
    
    private void computeFirstSetForAllTerminalSymbols() {
        for (String terminalSymbol: this.terminalSymbols) {
            if(terminalSymbol.equals(END_OF_LINE_SYMBOL)) {
                continue;
            }
            this.addFirstSet(terminalSymbol, terminalSymbol);
        }
    }
    
    private Set<String> computeFirstSetForParticularSymbol(String symbol) {
        if (this.isTerminalSymbol(symbol)) {
            Set<String> result = new HashSet<>();
            result.add(symbol);
            return result;
        }
        
        ProductionRule productionRule = this.getProductionRuleBasedOnNonTerminal(symbol);
        assert productionRule != null;

        Set<String> result = new HashSet<>();
        for (List<String> rightSide: productionRule.getRightHandSide()) {
            if(rightSide.get(0).equals(EPSILON)) {
                result.add(EPSILON);
            } else {
                boolean toAddEpsilon = true;
                for(String symbolForRightSide: rightSide) {
                    Set<String> toAdd = new HashSet<>(this.computeFirstSetForParticularSymbol(symbolForRightSide));
                    if(toAdd.contains(EPSILON)) {
                        toAdd.remove(EPSILON);
                        result.addAll(toAdd);
                    } else {
                        toAddEpsilon = false;
                        result.addAll(toAdd);
                        break;
                    }
                }
                
                if(toAddEpsilon) {
                    result.add(EPSILON);
                }
            }
        }
        
        return result;
    }
    
    private void computeFirstSetForAllNonTerminalSymbols() {
        for (String nonTerminal: this.nonTerminalSymbols) {
            Set<String> toAdd = this.computeFirstSetForParticularSymbol(nonTerminal);
            this.addAllFirstSet(nonTerminal, toAdd);
        }
    }

    @Override
    public void computeFirstSetForAllSymbols() {
        this.computeFirstSetForAllTerminalSymbols();
        
        this.computeFirstSetForAllNonTerminalSymbols();
    }

    @Override
    public void addFollowSet(String symbol, String followSetSymbol) {
        if (this.followSet.containsKey(symbol)) {
            this.followSet.get(symbol).add(followSetSymbol);
        } else {
            Set<String> toAdd = new HashSet<>();
            toAdd.add(followSetSymbol);
            this.followSet.put(symbol, toAdd);
        }
    }

    @Override
    public void addAllFollowSet(String nonTerminal, Set<String> followSet) {
        for(String follow: followSet) {
            this.addFollowSet(nonTerminal, follow);
        }
    }

    @Override
    public Set<String> getFollowSet(String symbol) {
        if (!this.followSet.containsKey(symbol)) {
            return null;
        }

        return Collections.unmodifiableSet(this.followSet.get(symbol));
    }

    private Set<String> computeFollowSetForNonTerminalSymbol(String nonTerminal) {
        Set<String> result = new HashSet<>();

        if(nonTerminal.equals(this.firstSymbol)) {
            result.add(END_OF_LINE_SYMBOL);
        }

        for(ProductionRule productionRule: this.productionRules) {
            String leftHandSide = productionRule.getLeftHandSide();

            for(List<String> right: productionRule.getRightHandSide()) {
                for(int i=0; i<right.size(); i++) {
                    String symbol = right.get(i);

                    if(symbol.equals(nonTerminal)) {
                        if(i == (right.size() - 1)) {
                            if(leftHandSide.equals(nonTerminal)) {
                                continue;
                            } else {
                                result.addAll(this.computeFollowSetForNonTerminalSymbol(leftHandSide));
                            }
                        } else {
                            Set<String> firstOfNext = new HashSet<>(this.getFirstSet(right.get(i+1)));
                            if(firstOfNext.contains(EPSILON)) {
                                firstOfNext.remove(EPSILON);
                                result.addAll(firstOfNext);
                                result.addAll(this.computeFollowSetForNonTerminalSymbol(leftHandSide));
                            } else {
                                result.addAll(firstOfNext);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void computeFollowSetForAllSymbols() {
        for(String nonTerminal: this.nonTerminalSymbols) {
            Set<String> toAdd = computeFollowSetForNonTerminalSymbol(nonTerminal);
            this.addAllFollowSet(nonTerminal, toAdd);
        }
    }

    @Override
    public void computeFirstAndFollowForAllSymbols() {
        this.computeFirstSetForAllSymbols();
        this.computeFollowSetForAllSymbols();
    }

    /**
     * Prints the production rules of the grammar and the information about terminal symbol and non terminal symbol
     */
    @Override
    public void printGrammar() {
        System.out.println("Following is the set of terminal symbols: " + this.terminalSymbols.toString());
        System.out.println("Following is the set of non-terminal symbols: " + this.nonTerminalSymbols.toString());
        System.out.println("Following are the rules in the given grammar:");
        for (ProductionRule productionRule: this.productionRules) {
            System.out.println(productionRule);
        }
        System.out.println();
    }

    @Override
    public void printGrammarToFile(String pathToDirectory, String note) throws IOException {
        String pathToFile = pathToDirectory + "\\" + note.replace(" ", "") + ".txt";
//        System.out.println(pathToFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile));
        writer.write(note + "\n");
        writer.write("Following is the set of terminal symbols:" + this.terminalSymbols.toString() + "\n");
        writer.write("Following is the set of non-terminal symbols: " + this.nonTerminalSymbols.toString() + "\n");
        writer.write("Following are the rules in the given grammar: \n");
        for (ProductionRule productionRule: this.productionRules) {
            writer.write(productionRule.toString() + "\n");
        }
        writer.close();
    }

    @Override
    public void printFirstAndFollowSet() {
        System.out.println("Following is the first set:");
        for(Map.Entry<String, Set<String>> elem: firstSet.entrySet()) {
            System.out.println(elem.getKey() + " => " + elem.getValue());
        }

        System.out.println("Following is the follow set:");
        for(Map.Entry<String, Set<String>> elem: followSet.entrySet()) {
            System.out.println(elem.getKey() + " => " + elem.getValue());
        }
    }

    @Override
    public void printFirstAndFollowSetToFile(String pathToDirectory, String note) throws IOException {
        String pathToFile = pathToDirectory + "\\" + note.replace(" ", "") + ".txt";
//        System.out.println(pathToFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile));
        writer.write("Following is the first set:\n");
        for(Map.Entry<String, Set<String>> elem: firstSet.entrySet()) {
            writer.write(elem.getKey() + " => " + elem.getValue() + "\n");
        }

        writer.write("\nFollowing is the follow set:\n");
        for(Map.Entry<String, Set<String>> elem: followSet.entrySet()) {
            writer.write(elem.getKey() + " => " + elem.getValue() + "\n");
        }
        writer.close();
    }

    private String findNewName(String leftHandSide) {
        String newName = leftHandSide + "'";
        boolean notUnique = true;
        while (notUnique) {
            notUnique = false;

            for(ProductionRule rule: this.productionRules) {
                if (rule.equals(new ProductionRule(newName))) {
                    newName += "'";
                    notUnique = true;
                }
            }
        }

        return newName;
    }

    private void solveNonImmediateLR(ProductionRule first, ProductionRule second) {
        String secondLeftHandSide = second.getLeftHandSide();

        Set<List<String>> newRightHandSideOfFirst = new HashSet<>();

        for (List<String> firstRightHandSide: first.getRightHandSide()) {
            if (firstRightHandSide.get(0).equals(secondLeftHandSide)) {
                for (List<String> secondRightHandSide: second.getRightHandSide()) {

                    List<String> newCurrFirstRule = new ArrayList<>(secondRightHandSide);
                    List<String> remainingOfFirst = new ArrayList<>(firstRightHandSide);

                    remainingOfFirst.remove(0);
                    newCurrFirstRule.addAll(remainingOfFirst);

                    newRightHandSideOfFirst.add(newCurrFirstRule);
                }
            } else {
                newRightHandSideOfFirst.add(firstRightHandSide);
            }
        }
        first.setNewRightHandSide(newRightHandSideOfFirst);
    }

    private void solveImmediateLR(ProductionRule first) {
        String leftHandSide = first.getLeftHandSide();
        String newName = this.findNewName(leftHandSide);

        Set<List<String>> leftRecursiveOne = new HashSet<>();
        Set<List<String>> nonLeftRecursiveOne = new HashSet<>();

        // Check if there is left recursion
        for (List<String> rule: first.getRightHandSide()) {
            if (rule.get(0).equals(leftHandSide)) {
                List<String> newRule = new ArrayList<>(rule);
                newRule.remove(0);
                leftRecursiveOne.add(newRule);
            } else {
                nonLeftRecursiveOne.add(rule);
            }
        }

        // if no left recursion exists
        if (leftRecursiveOne.isEmpty()) {
            return;
        }

        // add the new name to Non-Terminal Symbols
        this.addNonTerminalSymbol(newName);

        Set<List<String>> changeRuleForFirst = new HashSet<>();
        Set<List<String>> newRuleForNewName = new HashSet<>();

        if (nonLeftRecursiveOne.isEmpty()) {
            List<String> whenEmpty = new ArrayList<>();
            whenEmpty.add(newName);
            changeRuleForFirst.add(whenEmpty);
        }

        for (List<String> beta: nonLeftRecursiveOne) {
            List<String> forNonRecursive = new ArrayList<>(beta);
            forNonRecursive.add(newName);
            changeRuleForFirst.add(forNonRecursive);
        }

        for (List<String> alpha: leftRecursiveOne) {
            List<String> forRecursive = new ArrayList<>(alpha);
            forRecursive.add(newName);
            newRuleForNewName.add(forRecursive);
        }

        //Amend the original rule
        first.setNewRightHandSide(changeRuleForFirst);

        List<String> forEpsilon = new ArrayList<>();
        forEpsilon.add(EPSILON);
        //add new production rule
        newRuleForNewName.add(forEpsilon);

        ProductionRule newProductionRule = new ProductionRule(newName);
        newProductionRule.setNewRightHandSide(newRuleForNewName);
        this.productionRules.add(newProductionRule);
    }

    @Override
    public void applyAlgorithmForRemovalOfLeftRecursion() {
        int size = productionRules.size();

        for (int i=0; i<size; i++) {
            for (int j=0; j<i; j++) {
                this.solveNonImmediateLR(productionRules.get(i), productionRules.get(j));
            }
            solveImmediateLR(productionRules.get(i));
        }
    }

    private int findCommonPrefixForTwoListOfString(List<String> first, List<String> second) {
        if(first.isEmpty()  ||  second.isEmpty()  ||  !(first.get(0).equals(second.get(0)))) {
            return -1;
        }

        List<String> small = first;
        List<String> large = second;
        if (small.size() > large.size()) {
            small = second;
            large = first;
        }

        int index = 0;
        for (String largeString: large) {
            if (index == small.size()) {
                break;
            }
            if (!largeString.equals(small.get(index))) {
                break;
            }
            index++;
        }

        //for 0-based indexing
        index--;

        return index;
    }

    private List<String> findStringWhichIsLongestCommonPrefixForArray(List<List<String>> rightHandSide) {
        int indexWithCommonPref = -1;
        int outerCommonPrefixIndex = Integer.MAX_VALUE;

        for (int i=0; i<rightHandSide.size(); i++) {
            int commonPrefixIndex = Integer.MAX_VALUE;
            for (int j=i+1; j<rightHandSide.size(); j++) {
//                System.out.println("i -> " + rightHandSide.get(i));
//                System.out.println("j -> " + rightHandSide.get(j));

                // Check if this two has a common prefix
                int currCommonPrefixIndex = findCommonPrefixForTwoListOfString(rightHandSide.get(i), rightHandSide.get(j));
//                System.out.println(currCommonPrefixIndex);
//                System.out.println(currCommonPrefixIndex + " : " + rightHandSide.get(i).subList(0, currCommonPrefixIndex+1));

                if (currCommonPrefixIndex >= 0) {
                    commonPrefixIndex = Math.min(currCommonPrefixIndex, commonPrefixIndex);
                }
            }
//            System.out.println("Checking for : " + rightHandSide.get(i));
            if (commonPrefixIndex == Integer.MAX_VALUE) {
                continue;
            }
//            System.out.println("commonPrefixIndex = " + commonPrefixIndex + " :-> " + rightHandSide.get(i).substring(0, commonPrefixIndex));

            if (outerCommonPrefixIndex > commonPrefixIndex) {
                outerCommonPrefixIndex = commonPrefixIndex;
                indexWithCommonPref = i;
//                System.out.println("outerCommonPrefixIndex = " + outerCommonPrefixIndex);
            }
        }

        if (indexWithCommonPref == -1) {
            // no common prefix for any string
//            System.out.println("No common prefix");
            return null;
        }
//        System.out.println("indexWithCommonPref = " + indexWithCommonPref + ", and the string corresponding to it is: " + rightHandSide.get(indexWithCommonPref));
//        System.out.println("String = " + rightHandSide.get(indexWithCommonPref).substring(0, outerCommonPrefixIndex));

        List<String> result = new ArrayList<>();
        for (int i=0; i<=outerCommonPrefixIndex; i++) {
            result.add(rightHandSide.get(indexWithCommonPref).get(i));
        }
        return result;
//        return rightHandSide.get(indexWithCommonPref).substring(0, outerCommonPrefixIndex);
//        return null;
    }

    private boolean checkRuleStartsWithCommonPrefix(List<String> rule, List<String> longestCommonPrefix) {
        for(int i=0; i<longestCommonPrefix.size(); i++) {
            if(rule.size() == i) {
                // rule was shorter
                return false;
            }

            if (!rule.get(i).equals(longestCommonPrefix.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean applyAlgorithmForProducingAnEquivalentLeftFactoredOnParticularRule(ProductionRule productionRule){
//        System.out.println("To apply rule on: " + productionRule);

        List<String> longestCommonPrefix = this.findStringWhichIsLongestCommonPrefixForArray(new ArrayList<>(productionRule.getRightHandSide()));
        if(longestCommonPrefix == null) {
//            System.out.println("No common prefix");
            return false;
        }
//        System.out.println("longestCommonPrefix = " + longestCommonPrefix);

        String leftHandSide = productionRule.getLeftHandSide();
        String newName = findNewName(leftHandSide);

        // add the new name to set of non-terminal symbols
        this.addNonTerminalSymbol(newName);

        Set<List<String>> amendRules = new HashSet<>();
        Set<List<String>> newRulesForNewName = new HashSet<>();

        for (List<String> rule: productionRule.getRightHandSide()) {
            if (this.checkRuleStartsWithCommonPrefix(rule, longestCommonPrefix)) {
                if (rule.size() == longestCommonPrefix.size()) {
                    List<String> forEpsilon = new ArrayList<>();
                    forEpsilon.add(EPSILON);
                    newRulesForNewName.add(forEpsilon);
                } else {
                    List<String> toAdd = new ArrayList<>();
                    for (int i=longestCommonPrefix.size(); i <rule.size(); i++) {
                        toAdd.add(rule.get(i));
                    }
                    newRulesForNewName.add(toAdd);
                }
            } else {
                amendRules.add(rule);
            }
        }

        List<String> forNewName = new ArrayList<>();
        forNewName.addAll(longestCommonPrefix);
        forNewName.add(newName);
        amendRules.add(forNewName);
//
////        System.out.println("newRulesForNewName = " + newRulesForNewName);
////        System.out.println("amendRules = " + amendRules);
//
        //amend the rules
        productionRule.setNewRightHandSide(amendRules);

        ProductionRule newProductionRule = new ProductionRule(newName);
        newProductionRule.addAllRightHandSide(newRulesForNewName);
        //ConcurrentModificationException
//        this.productionRules.add(newProductionRule);
        this.toStoreNewRules.add(newProductionRule);
        return true;
    }

    private final List<ProductionRule> toStoreNewRules = new ArrayList<>();

    @Override
    public void applyAlgorithmForProducingAnEquivalentLeftFactored() {
        boolean value = true;

        //applying the algorithm continuously
        while(value) {
            value = false;
            this.toStoreNewRules.clear();

            for (ProductionRule productionRule : this.productionRules) {
                boolean check = applyAlgorithmForProducingAnEquivalentLeftFactoredOnParticularRule(productionRule);
                value = value | check;
            }
            this.productionRules.addAll(toStoreNewRules);
        }
    }

    @Override
    public void createParsingTable() {
        System.err.println("This function should be overridden by child class");
    }

    @Override
    public void printParsingTable() {
        System.err.println("This function should be overridden by child class");
    }

    @Override
    public void printParsingTableToFile(String path) throws IOException {
        System.err.println("This function should be overridden by child class");
    }

    @Override
    public void printParsingStepsToFile(String path, boolean accepted) throws IOException {
        System.err.println("This function should be overridden by child class");
    }

    @Override
    public boolean parser(List<Token> tokens) {
        System.err.println("This function should be overridden by child class");
        return false;
    }

    // for testing
    public static void main(String[] args) {
        BaseGrammar baseGrammar = new BaseGrammar();
//        baseGrammar.addRule("A -> aAB | aBc | aAc");
//        baseGrammar.addRule("E -> b");
//        baseGrammar.addTerminalSymbol("a");
//        baseGrammar.addTerminalSymbol("b");
//        baseGrammar.addNonTerminalSymbol("S");
//        baseGrammar.addRule("S -> S a | S b | c | d");

//        baseGrammar.addRule("A -> B a | A a | c");
//        baseGrammar.addRule("B -> B b | A b | d");

//        baseGrammar.addRule("X -> X S b | S a | b");
//        baseGrammar.addRule("S -> S b | X a | a");

//        baseGrammar.addRule("S -> A a | b");
//        baseGrammar.addRule("A -> A c | A a d | b d | ε");
//        baseGrammar.printGrammar();

//        System.out.println("After removal of left recursion");
//        baseGrammar.applyAlgorithmForRemovalOfLeftRecursion();
//        baseGrammar.printGrammar();

//        baseGrammar.addRule("S -> b S S a a S | b S S a S b | b S b | a");
//        baseGrammar.addRule("A -> a A B | a B c | a A c");
//        baseGrammar.addRule("S -> a S S b S | a S a S b | a b b | b");
//        baseGrammar.addRule("S -> a | a b | a b c | a b c d");

//        baseGrammar.addRule("S -> a A d | a B");
//        baseGrammar.addRule("A -> a | a b");
//        baseGrammar.addRule("B -> c c d | d d c");
//        baseGrammar.printGrammar();
//
//        System.out.println("After finding equivalent left factored baseGrammar");
//        baseGrammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
//        baseGrammar.printGrammar();


//        baseGrammar.setFirstSymbol("E");
//
//        baseGrammar.addTerminalSymbol("(");
//        baseGrammar.addTerminalSymbol(")");
//        baseGrammar.addTerminalSymbol("+");
//        baseGrammar.addTerminalSymbol("*");
//        baseGrammar.addTerminalSymbol("id");
//
//        baseGrammar.addNonTerminalSymbol("E");
//        baseGrammar.addNonTerminalSymbol("E'");
//        baseGrammar.addNonTerminalSymbol("T");
//        baseGrammar.addNonTerminalSymbol("T'");
//        baseGrammar.addNonTerminalSymbol("F");
//
//        baseGrammar.addRule("E -> T E'");
//        baseGrammar.addRule("E' -> + T E' | ε");
//        baseGrammar.addRule("T -> F T'");
//        baseGrammar.addRule("T' -> * F T' | ε");
//        baseGrammar.addRule("F -> ( E ) | id");

//        baseGrammar.setFirstSymbol("S");
//        baseGrammar.addNonTerminalSymbol("S");
//        baseGrammar.addNonTerminalSymbol("A");
//        baseGrammar.addTerminalSymbol("a");
//        baseGrammar.addRule("S -> A | a");
//        baseGrammar.addRule("A -> a");
//        baseGrammar.printGrammar();

//        baseGrammar.setFirstSymbol("S");
//        baseGrammar.addNonTerminalSymbol("S");
//        baseGrammar.addNonTerminalSymbol("L");
//        baseGrammar.addNonTerminalSymbol("L'");
//        baseGrammar.addTerminalSymbol("(");
//        baseGrammar.addTerminalSymbol(")");
//        baseGrammar.addTerminalSymbol("a");
//        baseGrammar.addRule("S -> ( L ) | a");
//        baseGrammar.addRule("L -> S L'");
//        baseGrammar.addRule("L' -> ) S L' | ε");

//        baseGrammar.setFirstSymbol("S");
//        baseGrammar.addNonTerminalSymbol("S");
//        baseGrammar.addTerminalSymbol("a");
//        baseGrammar.addTerminalSymbol("+");
//        baseGrammar.addTerminalSymbol("*");
//        baseGrammar.addRule("S -> S S + | S S * | a");

        baseGrammar.addRule("A -> B a | A a | c");
        baseGrammar.addRule("B -> B b | A b | d");

        baseGrammar.printGrammar();

        baseGrammar.applyAlgorithmForRemovalOfLeftRecursion();

        baseGrammar.printGrammar();

//        baseGrammar.computeFirstAndFollowForAllSymbols();

//        baseGrammar.printFirstAndFollowSet();
    }
}

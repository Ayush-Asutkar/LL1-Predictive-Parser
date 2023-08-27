import model.ProductionRule;

import java.util.*;

public class Grammar {
    private static final String EPSILON = String.valueOf('\u03B5');
    private final List<ProductionRule> productionRules;

    //store the non-terminal symbols and terminal symbols
    private final Set<String> terminalSymbols;
    private final Set<String> nonTerminalSymbols;

    //First set and follow set of all Non-Terminals
    Map<String, Set<String>> firstSet;
    Map<String, Set<String>> followSet;

    public Grammar() {
        this.productionRules = new ArrayList<>();
        this.terminalSymbols = new HashSet<>();
        this.terminalSymbols.add("$");
        this.nonTerminalSymbols = new HashSet<>();
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
    }

    public List<ProductionRule> getProductionRules() {
        return Collections.unmodifiableList(productionRules);
    }

    public Set<String> getTerminalSymbols() {
        return Collections.unmodifiableSet(terminalSymbols);
    }

    public Set<String> getNonTerminalSymbols() {
        return Collections.unmodifiableSet(nonTerminalSymbols);
    }

    public void addTerminalSymbol(String str) {
        this.terminalSymbols.add(str);
    }

    public void addNonTerminalSymbol(String str) {
        this.nonTerminalSymbols.add(str);
    }

    public void addRule (String leftHandSide, Set<List<String>> rightHandSide) {
        if (this.productionRules.contains(new ProductionRule(leftHandSide))) {
            int index = this.productionRules.indexOf(new ProductionRule(leftHandSide));
            ProductionRule newProductionRule = this.productionRules.get(index);
            newProductionRule.addAllRightHandSide(rightHandSide);
        } else {
            ProductionRule newProductionRule = new ProductionRule(leftHandSide);
            newProductionRule.addAllRightHandSide(rightHandSide);
            this.productionRules.add(newProductionRule);
        }
    }

    /**
     * To add the production rule to the grammar
     * @param rule is of format: A -> B | C | D
     */
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

    public void addAllFirstSet(String nonTerminal, Set<String> firstSet) {
        if(this.firstSet.containsKey(nonTerminal)) {
            this.firstSet.get(nonTerminal).addAll(firstSet);
        } else {
            this.firstSet.put(nonTerminal, new HashSet<>(firstSet));
        }
    }

    public void addAllFollowSet(String nonTerminal, Set<String> followSet) {
        if(this.followSet.containsKey(nonTerminal)) {
            this.followSet.get(nonTerminal).addAll(followSet);
        } else {
            this.followSet.put(nonTerminal, new HashSet<>(followSet));
        }
    }

    /**
     * Prints the production rules of the grammar and the information about terminal symbol and non terminal symbol
     */
    public void printGrammar() {
        System.out.println("Following is the set of terminal symbols: " + this.terminalSymbols.toString());
        System.out.println("Following is the set of non-terminal symbols: " + this.nonTerminalSymbols);
        System.out.println("Following are the rules in the given grammar:");
        for (ProductionRule productionRule: this.productionRules) {
            System.out.println(productionRule);
        }
        System.out.println();
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

    public void applyAlgorithmForRemovalOfLeftRecursion() {
        int size = productionRules.size();

        for (int i=0; i<size; i++) {
            for (int j=0; j<i; j++) {
                this.solveNonImmediateLR(productionRules.get(i), productionRules.get(j));
            }
            solveImmediateLR(productionRules.get(i));
        }
    }

    private int findCommonPrefixForTwoString(String first, String second) {
        String small = first;
        String large = second;
        if (small.length() > large.length()) {
            small = second;
            large = first;
        }

        int index = 0;
        for (char c: large.toCharArray()) {
            if (index == small.length()) {
                break;
            }
            if (c != small.charAt(index)) {
                break;
            }
            index++;
        }

        return index;
    }

    private String findStringWhichIsLongestCommonPrefixForArray(List<String> rightHandSide) {
        int indexWithCommonPref = -1;
        int outerCommonPrefixIndex = Integer.MAX_VALUE;
        for (int i=0; i<rightHandSide.size(); i++) {
            int commonPrefixIndex = Integer.MAX_VALUE;
            for (int j=i+1; j<rightHandSide.size(); j++) {
//                System.out.println("i -> " + rightHandSide.get(i));
//                System.out.println("j -> " + rightHandSide.get(j));

                // Check if this two has a common prefix
                int currCommonPrefixIndex = findCommonPrefixForTwoString(rightHandSide.get(i), rightHandSide.get(j));
//                System.out.println(commonPrefixIndex + " : " + rightHandSide.get(i).substring(0, commonPrefixIndex));

                if (currCommonPrefixIndex != 0) {
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

        return rightHandSide.get(indexWithCommonPref).substring(0, outerCommonPrefixIndex);
    }

    private List<String> convertProductionRuleToStringList(Set<List<String>> rules) {
        List<String> result = new ArrayList<>();

        for (List<String> rule: rules) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String inside: rule) {
                stringBuilder.append(inside);
            }
            result.add(stringBuilder.toString());
        }

//        System.out.println(result);
        return result;
    }

    private int findIndexTillWhereCommonPrefixIsPresentInRule (List<String> rule, String longestCommonPrefix) {
        if (longestCommonPrefix.isEmpty()) {
            return -1;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rule.size(); i++) {
            stringBuilder.append(rule.get(i));
            if (longestCommonPrefix.length() < stringBuilder.length()) {
                return -1;
            }

            if (stringBuilder.toString().equals(longestCommonPrefix)) {
                return i;
            }
        }

        return -1;
    }

    private List<String> breakLongestCommonPrefixToList(List<String> rule, int index) {
        List<String> result = new ArrayList<>();
        for (int i=0; i<=index; i++) {
            result.add(rule.get(i));
        }
        return result;
    }

    private boolean applyAlgorithmForProducingAnEquivalentLeftFactoredOnParticularRule(ProductionRule productionRule){
//        System.out.println("To apply rule on: " + productionRule);

        String longestCommonPrefix = this.findStringWhichIsLongestCommonPrefixForArray(convertProductionRuleToStringList(productionRule.getRightHandSide()));
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

        int indexInLongestCommonPrefix = -1;
        List<String> ruleForFindingListOfLongestCommonPrefix = null;

        for (List<String> rule: productionRule.getRightHandSide()) {
            int indexTillWhereCommonPrefixPresent = this.findIndexTillWhereCommonPrefixIsPresentInRule(rule, longestCommonPrefix);
            if (indexTillWhereCommonPrefixPresent >= 0) {
                indexInLongestCommonPrefix = indexTillWhereCommonPrefixPresent;
                ruleForFindingListOfLongestCommonPrefix = rule;
                if (indexTillWhereCommonPrefixPresent == (rule.size() - 1)) {
                    List<String> forEpsilon = new ArrayList<>();
                    forEpsilon.add(EPSILON);
                    newRulesForNewName.add(forEpsilon);
                } else {
                    List<String> currNewList = new ArrayList<>(rule);
                    for (int i=0; i<indexTillWhereCommonPrefixPresent+1; i++) {
                        currNewList.remove(0);
                    }

                    newRulesForNewName.add(currNewList);
                }
            } else {
                amendRules.add(rule);
            }
        }
        List<String> forNewName = new ArrayList<>();
        forNewName.addAll(breakLongestCommonPrefixToList(ruleForFindingListOfLongestCommonPrefix, indexInLongestCommonPrefix));
        forNewName.add(newName);
        amendRules.add(forNewName);

//        System.out.println("newRulesForNewName = " + newRulesForNewName);
//        System.out.println("amendRules = " + amendRules);

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

    // for testing
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
//        grammar.addRule("A -> aAB | aBc | aAc");
//        grammar.addRule("E -> b");
        grammar.addTerminalSymbol("a");
        grammar.addTerminalSymbol("b");
        grammar.addNonTerminalSymbol("S");
//        grammar.addRule("S -> S a | S b | c | d");

//        grammar.addRule("A -> B a | A a | c");
//        grammar.addRule("B -> B b | A b | d");

//        grammar.addRule("X -> X S b | S a | b");
//        grammar.addRule("S -> S b | X a | a");

//        grammar.addRule("S -> A a | b");
//        grammar.addRule("A -> A c | A a d | b d | ε");
//        grammar.printGrammar();

//        System.out.println("After removal of left recursion");
//        grammar.applyAlgorithmForRemovalOfLeftRecursion();
//        grammar.printGrammar();

//        grammar.addRule("A -> a A B | a B c | a A c");
//        grammar.addRule("S -> b S S a a S | b S S a S b | b S b | a");
//        grammar.addRule("S -> a S S b S | a S a S b | a b b | b");
//        grammar.addRule("S -> a | a b | a b c | a b c d");
        grammar.addRule("S -> a A d | a B");
        grammar.addRule("A -> a | a b");
        grammar.addRule("B -> c c d | d d c");
        grammar.printGrammar();

        System.out.println("After finding equivalent left factored grammar");
        grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
        grammar.printGrammar();
    }
}

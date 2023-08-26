import model.ProductionRule;

import java.util.*;

public class Grammar {
    private static final String EPSILON = String.valueOf('\u03B5');
    private final List<ProductionRule> productionRules;

    //store the non-terminal symbols and terminal symbols
    private final Set<String> terminalSymbols;
    private final Set<String> nonTerminalSymbols;

    public Grammar() {
        this.productionRules = new ArrayList<>();
        this.terminalSymbols = new HashSet<>();
        this.nonTerminalSymbols = new HashSet<>();
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

    public void addRule (String leftHandSide, Set<String> rightHandSide) {
        ProductionRule newProductionRule;
        if (this.productionRules.contains(new ProductionRule(leftHandSide))) {
            int index = this.productionRules.indexOf(new ProductionRule(leftHandSide));
            newProductionRule = this.productionRules.get(index);
            newProductionRule.addAllRightHandSide(rightHandSide);
        } else {
            newProductionRule = new ProductionRule(leftHandSide);
            newProductionRule.addAllRightHandSide(rightHandSide);
            this.productionRules.add(newProductionRule);
        }
    }

    /**
     * To add the production rule to the grammar
     * @param rule is of format: A -> B | C | D
     */
    public void addRule (String rule) {
        rule = rule.replaceAll(" ","");
//        System.out.println(rule);

//        System.out.println(Arrays.toString(rule.split("->")));

        String[] ruleSplit = rule.split("->");
        String leftSide = ruleSplit[0];
//        System.out.println(leftSide);


        String[] rightSide = ruleSplit[1].split("\\|");

        Set<String> rightSideSet = new HashSet<>(List.of(rightSide));
        this.addRule(leftSide, rightSideSet);
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

    private void solveNonImmediateLR(ProductionRule first, ProductionRule second) {

        String secondLeftHandSide = second.getLeftHandSide();

        Set<String> newRulesOfFirst = new HashSet<>();

        for (String rule: first.getRightHandSide()) {
            if (rule.startsWith(secondLeftHandSide)) {
                for (String innerRule: second.getRightHandSide()) {
                    newRulesOfFirst.add(innerRule + rule.substring(secondLeftHandSide.length()));
                }
            } else {
                newRulesOfFirst.add(rule);
            }
        }
        first.setNewRightHandSide(newRulesOfFirst);
    }

    private void solveImmediateLR(ProductionRule first) {
        String leftHandSide = first.getLeftHandSide();
        String newName = leftHandSide + "'";

        Set<String> leftRecursiveOne = new HashSet<>();
        Set<String> nonLeftRecursiveOne = new HashSet<>();

        // Check if there is left recursion
        for (String rule: first.getRightHandSide()) {
            if (rule.startsWith(leftHandSide)) {
                leftRecursiveOne.add(rule.substring(leftHandSide.length()));
            } else {
                nonLeftRecursiveOne.add(rule);
            }
        }

        // if no left recursion exists
        if (leftRecursiveOne.isEmpty()) {
            return;
        }

        Set<String> changeRuleForFirst = new HashSet<>();
        Set<String> newRuleForNewName = new HashSet<>();

        if (nonLeftRecursiveOne.isEmpty()) {
            changeRuleForFirst.add(newName);
        }

        for (String beta: nonLeftRecursiveOne) {
            changeRuleForFirst.add(beta + newName);
        }

        for (String alpha: leftRecursiveOne) {
            newRuleForNewName.add(alpha + newName);
        }

        //Amend the original rule
        first.setNewRightHandSide(changeRuleForFirst);

        //add new production rule
        newRuleForNewName.add(EPSILON);

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

        this.nonTerminalSymbols.add(newName);

        return newName;
    }

    private boolean applyAlgorithmForProducingAnEquivalentLeftFactoredOnParticularRule(ProductionRule productionRule){
//        System.out.println("To apply rule on: " + productionRule);

        String longestCommonPrefix = this.findStringWhichIsLongestCommonPrefixForArray(new ArrayList<>(productionRule.getRightHandSide()));
        if(longestCommonPrefix == null) {
//            System.out.println("No common prefix");
            return false;
        }
//        System.out.println("longestCommonPrefix = " + longestCommonPrefix);

        String leftHandSide = productionRule.getLeftHandSide();
        String newName = findNewName(leftHandSide);

        Set<String> amendRules = new HashSet<>();
        Set<String> newRulesForNewName = new HashSet<>();

        for (String rule: productionRule.getRightHandSide()) {
            if (rule.startsWith(longestCommonPrefix)) {
                if (rule.length() == longestCommonPrefix.length()) {
                    newRulesForNewName.add(EPSILON);
                } else {
                    newRulesForNewName.add(rule.substring(longestCommonPrefix.length()));
                }
            } else {
                amendRules.add(rule);
            }
        }
        amendRules.add(longestCommonPrefix + newName);

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
        grammar.addRule("S -> bSSaaS | bSSaSb | bSb | a");
        grammar.printGrammar();

//        grammar.applyAlgorithmForRemovalOfLeftRecursion();
//        grammar.printRules();

        System.out.println("After finding equivalent left factored grammar");
        grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
        grammar.printGrammar();
    }
}

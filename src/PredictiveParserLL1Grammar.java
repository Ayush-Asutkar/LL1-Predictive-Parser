import model.ProductionRule;

import java.util.*;

public class PredictiveParserLL1Grammar extends Grammar {
    /**
     * ArrayList(Non-Terminal, Map(Terminal, ProductionRule))
     */
    private final Map<String, Map<String, ProductionRule>> parsingTable;

    public PredictiveParserLL1Grammar() {
        super();
        this.parsingTable = new HashMap<>();
    }

    private void createEmptyParsingTable() {
        Set<String> terminalSymbols = super.getTerminalSymbols();
        Set<String> nonTerminalSymbols = super.getNonTerminalSymbols();

        for(String nonTerminal: nonTerminalSymbols) {
            for(String terminal: terminalSymbols) {
                if (this.parsingTable.containsKey(nonTerminal)) {
                    this.parsingTable.get(nonTerminal).put(terminal, null);
                } else {
                    Map<String, ProductionRule> map = new HashMap<>();
                    map.put(terminal, null);
                    this.parsingTable.put(nonTerminal, map);
                }
            }
        }
    }

    private void addToParsingTable(String terminal, String nonTerminal, ProductionRule productionRule) {
        this.parsingTable.get(nonTerminal).put(terminal, productionRule);
    }

    private void addToParsingTable(Set<String> terminal, String nonTerminal, ProductionRule productionRule) {
        for (String symbolTerminal: terminal) {
            this.addToParsingTable(symbolTerminal, nonTerminal, productionRule);
        }
    }

    private Set<String> terminalSymbolsWhereToAddRule(String leftHandSide, List<String> rightHandSide) {
        Set<String> result = new HashSet<>();

        boolean needToAddFollowSet = true;

        for(String symbol: rightHandSide) {

            // find the first set of symbol
            Set<String> firstSetOfSymbol = super.getFirstSet(symbol);
            if (firstSetOfSymbol == null) {
                continue;
            }

            Set<String> toAdd = new HashSet<>(firstSetOfSymbol);
            if(!toAdd.contains(EPSILON)) {
                needToAddFollowSet = false;
                result.addAll(toAdd);
                break;
            } else {
                toAdd.remove(EPSILON);
                result.addAll(toAdd);
            }
        }

        //check if we need to add follow set
        if(needToAddFollowSet) {
            Set<String> toAdd = new HashSet<>(super.getFollowSet(leftHandSide));
            result.addAll(toAdd);
        }

        return result;
    }

    public void createParsingTable() {
        this.createEmptyParsingTable();

        for (ProductionRule productionRule: super.getProductionRules()) {
            String leftHandSide = productionRule.getLeftHandSide();
            for (List<String> rightHandSide: productionRule.getRightHandSide()) {
                Set<String> terminalSymbolsToAddRule = terminalSymbolsWhereToAddRule(leftHandSide, rightHandSide);

                ProductionRule toAddInTable = new ProductionRule(leftHandSide);
                toAddInTable.addRightHandSide(rightHandSide);
                this.addToParsingTable(terminalSymbolsToAddRule, leftHandSide, toAddInTable);
            }
        }
    }

    public void printParsingTable() {
        System.out.println("This is the parsing table: ");

        // print header for non-terminal
        for (String terminal: super.getTerminalSymbols()) {
            System.out.format("%25s", terminal);
        }
        System.out.println();
        for (String nonTerminal: super.getNonTerminalSymbols()) {
            System.out.print(nonTerminal);
            for (String terminal: super.getTerminalSymbols()) {
                System.out.format("%25s", this.parsingTable.get(nonTerminal).get(terminal));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        PredictiveParserLL1Grammar predictiveParserLL1Grammar = new PredictiveParserLL1Grammar();

        predictiveParserLL1Grammar.addTerminalSymbol("(");
        predictiveParserLL1Grammar.addTerminalSymbol(")");
        predictiveParserLL1Grammar.addTerminalSymbol("+");
        predictiveParserLL1Grammar.addTerminalSymbol("*");
        predictiveParserLL1Grammar.addTerminalSymbol("id");
        predictiveParserLL1Grammar.addNonTerminalSymbol("E");
        predictiveParserLL1Grammar.addNonTerminalSymbol("E'");
        predictiveParserLL1Grammar.addNonTerminalSymbol("T");
        predictiveParserLL1Grammar.addNonTerminalSymbol("T'");
        predictiveParserLL1Grammar.addNonTerminalSymbol("F");
        predictiveParserLL1Grammar.addRule("E -> T E'");
        predictiveParserLL1Grammar.addRule("E' -> + T E' | ε");
        predictiveParserLL1Grammar.addRule("T -> F T'");
        predictiveParserLL1Grammar.addRule("T' -> * F T' | ε");
        predictiveParserLL1Grammar.addRule("F -> ( E ) | id");
//        predictiveParserLL1Grammar.printGrammar();

        //Add first set and follow set manually
        Set<String> firstSet = new HashSet<>();
        firstSet.add("id");
        firstSet.add("(");
        predictiveParserLL1Grammar.addAllFirstSet("E", firstSet);

        firstSet.clear();
        firstSet.add("+");
        firstSet.add("ε");
        predictiveParserLL1Grammar.addAllFirstSet("E'", firstSet);

        firstSet.clear();
        firstSet.add("id");
        firstSet.add("(");
        predictiveParserLL1Grammar.addAllFirstSet("T", firstSet);

        firstSet.clear();
        firstSet.add("*");
        firstSet.add("ε");
        predictiveParserLL1Grammar.addAllFirstSet("T'", firstSet);

        firstSet.clear();
        firstSet.add("id");
        firstSet.add("(");
        predictiveParserLL1Grammar.addAllFirstSet("F", firstSet);

        firstSet.clear();
        firstSet.add("id");
        predictiveParserLL1Grammar.addAllFirstSet("id#", firstSet);

        firstSet.clear();
        firstSet.add("+");
        predictiveParserLL1Grammar.addAllFirstSet("+", firstSet);

        firstSet.clear();
        firstSet.add("*");
        predictiveParserLL1Grammar.addAllFirstSet("*", firstSet);

        firstSet.clear();
        firstSet.add("(");
        predictiveParserLL1Grammar.addAllFirstSet("(", firstSet);

        firstSet.clear();
        firstSet.add(")");
        predictiveParserLL1Grammar.addAllFirstSet(")", firstSet);

        Set<String> followSet = new HashSet<>();
        followSet.add("$");
        followSet.add(")");
        predictiveParserLL1Grammar.addAllFollowSet("E", followSet);

        followSet.clear();
        followSet.add("$");
        followSet.add(")");
        predictiveParserLL1Grammar.addAllFollowSet("E'", followSet);

        followSet.clear();
        followSet.add("+");
        followSet.add("$");
        followSet.add(")");
        predictiveParserLL1Grammar.addAllFollowSet("T", followSet);

        followSet.clear();
        followSet.add("+");
        followSet.add("$");
        followSet.add(")");
        predictiveParserLL1Grammar.addAllFollowSet("T'", followSet);

        followSet.clear();
        followSet.add("*");
        followSet.add("+");
        followSet.add("$");
        followSet.add(")");
        predictiveParserLL1Grammar.addAllFollowSet("F", followSet);

        predictiveParserLL1Grammar.printFirstAndFollowSet();
//        predictiveParserLL1Grammar.printGrammar();
        predictiveParserLL1Grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
//        predictiveParserLL1Grammar.printGrammar();
        predictiveParserLL1Grammar.applyAlgorithmForRemovalOfLeftRecursion();
//        predictiveParserLL1Grammar.printGrammar();

        predictiveParserLL1Grammar.createParsingTable();

        predictiveParserLL1Grammar.printParsingTable();
    }
}

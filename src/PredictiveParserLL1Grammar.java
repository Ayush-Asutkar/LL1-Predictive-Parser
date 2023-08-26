import model.ProductionRule;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            this.parsingTable.get(nonTerminal).put("$", null);
        }
    }

    public void createParsingTable() {
        this.createEmptyParsingTable();

    }

    public void printParsingTable() {
        System.out.println("This is the parsing table: ");

        // print header for non-terminal
        for (String terminal: super.getTerminalSymbols()) {
            System.out.format("%15s", terminal);
        }
        System.out.format("%15s", "$");
        System.out.println();
        for (String nonTerminal: super.getNonTerminalSymbols()) {
            System.out.print(nonTerminal);
            for (String terminal: super.getTerminalSymbols()) {
                System.out.format("%15s", this.parsingTable.get(nonTerminal).get(terminal));
            }
            System.out.format("%15s", this.parsingTable.get(nonTerminal).get("$"));
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
        predictiveParserLL1Grammar.addRule("E' -> +T E' | ε");
        predictiveParserLL1Grammar.addRule("T -> FT'");
        predictiveParserLL1Grammar.addRule("T' -> *FT' | ε");
        predictiveParserLL1Grammar.addRule("F -> (E) | id");
        predictiveParserLL1Grammar.printGrammar();

        System.out.println("Removing Left Recursion");
        predictiveParserLL1Grammar.applyAlgorithmForRemovalOfLeftRecursion();
        predictiveParserLL1Grammar.printGrammar();

        System.out.println("After finding equivalent left factored grammar");
        predictiveParserLL1Grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
        predictiveParserLL1Grammar.printGrammar();

        predictiveParserLL1Grammar.createParsingTable();

        predictiveParserLL1Grammar.printParsingTable();
    }
}

package grammar;

import model.ProductionRule;
import model.Token;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface Grammar {
    String getFirstSymbol();
    void setFirstSymbol(String firstSymbol);
    List<ProductionRule> getProductionRules();
    void addRule(String leftHandSide, Set<List<String>> rightHandSide);
    void addRule (String rule);
    Set<String> getTerminalSymbols();
    void addTerminalSymbol(String str);
    void addAllTerminalSymbolFromIterator(Iterator<String> iterator);
    boolean isTerminalSymbol(String str);
    Set<String> getNonTerminalSymbols();
    void addNonTerminalSymbol(String str);
    void addAllNonTerminalSymbolFromIterator(Iterator<String> iterator);
    boolean isNonTerminalSymbol(String str);
    void addFirstSet(String symbol, String firstSetSymbol);
    void addAllFirstSet(String symbol, Set<String> firstSet);
    Set<String> getFirstSet(String symbol);
    void computeFirstSetForAllSymbols();
    void addFollowSet(String symbol, String followSetSymbol);
    void addAllFollowSet(String nonTerminal, Set<String> followSet);
    Set<String> getFollowSet(String symbol);
    void computeFollowSetForAllSymbols();
    void computeFirstAndFollowForAllSymbols();
    void printGrammar();
    void printGrammarToFile(String pathToDirectory, String note) throws IOException;
    void printFirstAndFollowSet();
    void printFirstAndFollowSetToFile(String pathToDirectory, String note) throws IOException;
    void applyAlgorithmForRemovalOfLeftRecursion();
    void applyAlgorithmForProducingAnEquivalentLeftFactored();

    void createParsingTable();
    void printParsingTable();
    void printParsingTableToFile(String path) throws IOException;
    void printParsingStepsToFile(String path, boolean accepted) throws IOException;
    boolean parser(List<Token> tokens);
}

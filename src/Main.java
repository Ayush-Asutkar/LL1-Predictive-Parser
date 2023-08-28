import grammar.PredictiveParserLL1Grammar;
import helperfunction.ReadingInput;
import model.Token;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path homeDirectory = FileSystems.getDefault().getPath("").toAbsolutePath();

    private static PredictiveParserLL1Grammar takeLL1GrammarInput() throws IOException {
        String pathToInputGrammar = homeDirectory + "\\Input\\InputGrammar.txt";
        return ReadingInput.readAndCreateLL1Grammar(pathToInputGrammar);
    }

    private static List<Token> takeFlexProgramTokenList(int grammarChoice) throws IOException {
        String pathToFlexProgram = homeDirectory + "\\Flex\\Grammar" + grammarChoice + "\\output.txt";
//            System.out.println("pathToFlexProgram: " + pathToFlexProgram);
        return ReadingInput.readTokensGeneratedFromFlex(pathToFlexProgram);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Which grammar to apply on (1 or 2): ");
        int grammarChoice = sc.nextInt();
        sc.close();

        assert grammarChoice == 1 || grammarChoice == 2;

        //take input the grammar
        PredictiveParserLL1Grammar grammar = null;
        try {
            grammar = takeLL1GrammarInput();
        } catch (IOException e) {
            System.out.println("Unable to read from grammar file");
            System.out.println(e.getMessage());
        }

        //apply left recursion and left factoring
        assert grammar != null;

        System.out.println("Input Grammar: ");
        grammar.printGrammar();

        grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
        grammar.applyAlgorithmForRemovalOfLeftRecursion();

        System.out.println("After finding equivalent left factored and removing of left recursion: ");
        grammar.printGrammar();

        //compute the first and follow set
        grammar.computeFirstAndFollowForAllSymbols();
        grammar.printFirstAndFollowSet();

        //Take the list of tokens from flex program
        List<Token> tokens = null;
        try {
            tokens = takeFlexProgramTokenList(grammarChoice);
        } catch (IOException e) {
            System.out.println("Unable to read from Flex output file");
            System.out.println(e.getMessage());
        }

        assert tokens != null;
//        System.out.println(tokens);

        //create parsing table
        grammar.createParsingTable();
        grammar.printParsingTable();

        //apply the parser
        boolean parserAccepted = grammar.parser(tokens);
        if(parserAccepted) {
            System.out.println("The given input text is accepted");
        } else {
            System.out.println("The given output text is not accepted");
        }
    }
}

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

    public static void main(String[] args) {

        //take input the grammar
        PredictiveParserLL1Grammar grammar = null;
        try {
            String pathToInputGrammar = homeDirectory + "\\Input\\InputGrammar.txt";
            grammar = ReadingInput.readAndCreateLL1Grammar(pathToInputGrammar);
            System.out.println("Input Grammar: ");
            grammar.printGrammar();
        } catch (IOException e) {
            System.out.println("Unable to read from grammar file");
            System.out.println(e.getMessage());
        }

        //apply left recursion and left factoring
        assert grammar != null;
        grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
        grammar.applyAlgorithmForRemovalOfLeftRecursion();

        System.out.println("After finding equivalent left factored and removing of left recursion: ");
        grammar.printGrammar();

        //compute the first and follow set
        grammar.computeFirstAndFollowForAllSymbols();
        grammar.printFirstAndFollowSet();

        Scanner sc = new Scanner(System.in);
        System.out.print("Which grammar to apply on (1 or 2): ");
        int grammarChoice = sc.nextInt();
        sc.close();

        assert grammarChoice == 1 || grammarChoice == 2;

        //Take the list of tokens from flex program
        List<Token> tokens = null;
        try {
            String pathToFlexProgram = homeDirectory + "\\Flex\\Grammar" + grammarChoice + "\\output.txt";
//            System.out.println("pathToFlexProgram: " + pathToFlexProgram);
            tokens =  ReadingInput.readTokensGeneratedFromFlex(pathToFlexProgram);
        } catch (IOException e) {
            System.out.println("Unable to read from Flex output file");
            System.out.println(e.getMessage());
        }

        assert tokens != null;
        System.out.println(tokens);
        PredictiveParserLL1Grammar predictiveParserLL1Grammar = (PredictiveParserLL1Grammar) grammar;

        //create parsing table
        predictiveParserLL1Grammar.createParsingTable();
        predictiveParserLL1Grammar.printParsingTable();

        //apply the parser
        boolean parserAccepted = predictiveParserLL1Grammar.parser(tokens);
        if(parserAccepted) {
            System.out.println("The given input text is accepted");
        } else {
            System.out.println("The given output text is not accepted");
        }
    }
}

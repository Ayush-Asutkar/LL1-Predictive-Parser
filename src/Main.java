import grammar.PredictiveParserLL1Grammar;
import helperfunction.ReadingInput;
import helperfunction.RunningLex;
import model.Token;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path homeDirectory = FileSystems.getDefault().getPath("").toAbsolutePath();

    private static void copyContentsFromInputTextToInput(int grammarChoice) throws IOException {
        String sourceFilePath = homeDirectory + "\\Input\\InputText.txt";
        String destinationFilePath = homeDirectory + "\\Flex\\Grammar" + grammarChoice + "\\input.txt";
        RunningLex.copyContentForFlex(sourceFilePath, destinationFilePath);
    }

    private static PredictiveParserLL1Grammar takeLL1GrammarInput() throws IOException {
        String pathToInputGrammar = homeDirectory + "\\Input\\InputGrammar.txt";
        return ReadingInput.readAndCreateLL1Grammar(pathToInputGrammar);
    }

    private static List<Token> takeFlexProgramTokenList(int grammarChoice) throws IOException {
        String pathToFlexProgram = homeDirectory + "\\Flex\\Grammar" + grammarChoice + "\\output.txt";
//            System.out.println("pathToFlexProgram: " + pathToFlexProgram);
        return ReadingInput.readTokensGeneratedFromFlex(pathToFlexProgram);
    }

    private static void deleteOutputDirectory () throws IOException {
        Path path = Path.of(homeDirectory + "\\Output");
//        System.out.println("path: " + path);

        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        assert Files.exists(path);

    }

    private static void createOutputDirectory() throws IOException {
        try {
            deleteOutputDirectory();
        } catch (IOException e) {
//            System.out.println("Output file does not exist yet");
        }
        Path outputDirectoryPath = Path.of(homeDirectory + "\\Output");
        Files.createDirectory(outputDirectoryPath);
    }

    private static void printGrammarWithNoteToFile(PredictiveParserLL1Grammar grammar, String note) throws IOException {
        String pathToOutputDirectory = homeDirectory + "\\Output";
        grammar.printGrammarToFile(pathToOutputDirectory, note);
    }

    private static void printFirstFollowSetToFile(PredictiveParserLL1Grammar grammar) throws IOException {
        String pathToOutputDirectory = homeDirectory + "\\Output";
        grammar.printFirstAndFollowSetToFile(pathToOutputDirectory, "First Follow Set");
    }

    private static void printParsingTableToFile(PredictiveParserLL1Grammar grammar) throws IOException {
        String pathToOutputFile = homeDirectory + "\\Output\\ParsingTable.txt";
        grammar.printParsingTableToFile(pathToOutputFile);
    }

    private static void printParsingStepsToFile(PredictiveParserLL1Grammar grammar, boolean parserAccepted) throws IOException {
        String pathToOutputFile = homeDirectory + "\\Output\\ParsingSteps.txt";
        grammar.printParsingStepsToFile(pathToOutputFile, parserAccepted);
    }

    public static void main(String[] args) {

        //create empty output directory
        try {
            createOutputDirectory();
        } catch (IOException e) {
            System.out.println("Could not create output file");
            System.out.println(e.getMessage());
        }


        Scanner sc = new Scanner(System.in);
        System.out.print("Which grammar to apply on (1 or 2): ");
        int grammarChoice = sc.nextInt();
        sc.close();

        assert grammarChoice == 1 || grammarChoice == 2;


        //read text from Input/InputText.txt to particular flex input
        try {
            copyContentsFromInputTextToInput(grammarChoice);
        } catch (IOException e) {
            System.out.println("Could not copy contents of file InputText to particular flex input");
        }

//        //take input the grammar
//        PredictiveParserLL1Grammar grammar = null;
//        try {
//            grammar = takeLL1GrammarInput();
//        } catch (IOException e) {
//            System.out.println("Unable to read from grammar file");
//            System.out.println(e.getMessage());
//        }
//
//        assert grammar != null;
//
//        System.out.println("Input Grammar: ");
//        grammar.printGrammar();
//
//        try {
//            printGrammarWithNoteToFile(grammar, "Input Grammar");
//        } catch (IOException e) {
//            System.out.println("Could not write input grammar to output file");
//            System.out.println(e.getMessage());
//        }
//
//
//        grammar.applyAlgorithmForProducingAnEquivalentLeftFactored();
//        grammar.applyAlgorithmForRemovalOfLeftRecursion();
//
//        System.out.println("After finding equivalent left factored and removing of left recursion: ");
//        grammar.printGrammar();
//
//
//        try {
//            printGrammarWithNoteToFile(grammar, "Equivalent Left Factored and Removing Left Recursion Grammar");
//        } catch (IOException e) {
//            System.out.println("Could not write Equivalent Left Factored and Removing Left Recursion grammar to output file");
//            System.out.println(e.getMessage());
//        }
//
//        //compute the first and follow set
//        grammar.computeFirstAndFollowForAllSymbols();
//        grammar.printFirstAndFollowSet();
//
//        try {
//            printFirstFollowSetToFile(grammar);
//        } catch (IOException e) {
//            System.out.println("Could not write First Follow Set to output file");
//            System.out.println(e.getMessage());
//        }
//
//        //Take the list of tokens from flex program
//        List<Token> tokens = null;
//        try {
//            tokens = takeFlexProgramTokenList(grammarChoice);
//        } catch (IOException e) {
//            System.out.println("Unable to read from Flex output file");
//            System.out.println(e.getMessage());
//        }
//
//        assert tokens != null;
////        System.out.println(tokens);
//
//        //create parsing table
//        grammar.createParsingTable();
//        grammar.printParsingTable();
//
//        try {
//            printParsingTableToFile(grammar);
//        } catch (IOException e) {
//            System.out.println("Could not write parsing table to output file");
//            System.out.println(e.getMessage());
//        }
//
//        //apply the parser
//        boolean parserAccepted = grammar.parser(tokens);
//        try {
//            printParsingStepsToFile(grammar, parserAccepted);
//        } catch (IOException e) {
//            System.out.println("Unable to write parser steps to output file");
//            System.out.println(e.getMessage());
//        }
//
//        if(parserAccepted) {
//            System.out.println("The given input text is ACCEPTED");
//        } else {
//            System.out.println("The given input text is REJECTED");
//        }
    }
}

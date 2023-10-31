package helperfunction;

import grammar.Grammar;
import grammar.PredictiveParserLL1Grammar;
import model.Token;
import model.TokenBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadingInput {
    public static List<Token> readTokensGeneratedFromFlex(String path) throws IOException {
//        System.out.println("Path: " + path);

        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        List<Token> result = new ArrayList<>();
        while((st = br.readLine()) != null) {
//            System.out.println(st);
            if (st.isEmpty()) {
                continue;
            }

            String[] splitedString = st.split(",");
            assert splitedString.length == 4;

            String tokenName = splitedString[0].trim();
            String value = splitedString[1].trim();
            int lineNumber = Integer.parseInt(splitedString[2].trim());
            int indexInLine = Integer.parseInt(splitedString[3].trim());
            Token newToken = new TokenBuilder()
                    .setTokenName(tokenName)
                    .setValue(value)
                    .setLineNumber(lineNumber)
                    .setIndexInLine(indexInLine)
                    .buildToken();
            result.add(newToken);
        }

        Token lastToken = new TokenBuilder()
                .setTokenName("$")
                .setValue("$")
                .setLineNumber(result.get(result.size() - 1).getLineNumber() + 1)
                .setIndexInLine(0)
                .buildToken();
        result.add(lastToken);

        return result;
    }

    public static Grammar readAndCreateLL1Grammar(String path) throws IOException {
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        Grammar grammar = new PredictiveParserLL1Grammar();

        String startSymbol = br.readLine();
        grammar.setFirstSymbol(startSymbol);

        String nonTerminalString = br.readLine();
//        System.out.println("terminalSymbols read from input: " + terminalSymbols);
        String[] nonTerminals = nonTerminalString.trim().split(" ");
        grammar.addAllNonTerminalSymbolFromIterator(Arrays.stream(nonTerminals).iterator());

        String terminalString = br.readLine();
//        System.out.println("Non Terminal symbols read from input: " + nonTerminalSymbols);
        String[] terminals = terminalString.trim().split(" ");
        grammar.addAllTerminalSymbolFromIterator(Arrays.stream(terminals).iterator());

        String input;
        while((input = br.readLine()) != null) {
            if(input.isEmpty()) {
                continue;
            }

            grammar.addRule(input);
        }

        return grammar;
    }
}

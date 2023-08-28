package helperfunction;

import model.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadingInput {
    public static List<Token> readTokensGeneratedFromFlex(String path) throws IOException {
        System.out.println("Path: " + path);

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
            String tokenName = splitedString[0].trim();
            String value = splitedString[1].trim();
            Token newToken = new Token(tokenName, value);
            result.add(newToken);
        }

        return result;
    }
}

import helperfunction.ReadingInput;
import model.Token;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        String pathString = path.toString();
        pathString += "\\Flex\\Grammar1\\output.txt";

        try {
            List<Token> tokens = ReadingInput.readTokensGeneratedFromFlex(pathString);
//            System.out.println(tokens);


        } catch (IOException e) {
            System.out.println("Unable to read from file: " + pathString);
            return;
        }

    }
}
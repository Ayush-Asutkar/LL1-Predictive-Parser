package helperfunction;

import java.io.*;

public class RunningLex {
    private static String readFromAllFromFile(String path) throws IOException {
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder result = new StringBuilder();
        String st;
        while((st = br.readLine()) != null) {
            result.append(st).append("\n");
        }

        return result.toString();
    }

    private static void writeToAFile(String path, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        writer.write(content);

        writer.close();
    }
    public static void copyContentForFlex(String sourceFilePath, String destinationFilePath) throws IOException {
        String content;
        try {
            content = readFromAllFromFile(sourceFilePath);
        } catch (IOException e) {
            System.out.println("Could not read from input text");
            System.out.println(e.getMessage());
            throw new IOException("Could not read from " + sourceFilePath);
        }

        try {
            writeToAFile(destinationFilePath, content);
        } catch (IOException e) {
            System.out.println("Could write to particular flex input");
            System.out.println(e.getMessage());
            throw new IOException("Could not write to " + destinationFilePath);
        }
    }
}

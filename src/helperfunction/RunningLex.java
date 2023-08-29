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
    public static void copyContentForFlex(String sourceFilePath, String destinationFilePath) throws RuntimeException {
        String content;
        try {
            content = readFromAllFromFile(sourceFilePath);
        } catch (IOException e) {
            System.out.println("Could not read from input text");
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not read from " + sourceFilePath);
        }

        try {
            writeToAFile(destinationFilePath, content);
        } catch (IOException e) {
            System.out.println("Could write to particular flex input");
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not write to " + destinationFilePath);
        }
    }

    private static void runOnCommandLine(String pathToDirectory, String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command, null, new File(pathToDirectory));
        int exitCode = process.waitFor();
        System.out.println("Command executed with exit code: " + exitCode);
    }

    private static void compileFlex(String pathToDirectory) throws IOException, InterruptedException {
        String command = "flex lex.l";
        runOnCommandLine(pathToDirectory, command);
    }

    private static void compileLexCProgram(String pathToDirectory) throws IOException, InterruptedException {
        String command = "gcc lex.yy.c -o output";
        runOnCommandLine(pathToDirectory, command);
    }

    private static void runOutputFile(String pathToDirectory) throws IOException, InterruptedException {
        String command = pathToDirectory + "\\output";
        runOnCommandLine(pathToDirectory, command);
    }

    public static void compileAndRunFlex(String pathToDirectory) throws RuntimeException {
        // compile the flex program
        try {
            compileFlex(pathToDirectory);
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not compile flex program");
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not compile flex program");
        }

        //compile the lex.yy.c
        try {
            compileLexCProgram(pathToDirectory);
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not compile the lex.yy.c program");
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not compile lex.yy.c");
        }

        //run the output.exe
        try {
            runOutputFile(pathToDirectory);
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not run output.exe program");
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not run output.exe");
        }
    }
}

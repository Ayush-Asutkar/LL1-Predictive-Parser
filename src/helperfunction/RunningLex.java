package helperfunction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunningLex {
    public static void runFlexBasedCommands () {
//        File dir = new File("./");
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Path: " + s);
    }

    public static void main(String[] args) {
        runFlexBasedCommands();
    }
}

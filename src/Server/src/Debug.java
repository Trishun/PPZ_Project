import java.io.*;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Logging class
 * Created by PD on 01.05.2017.
 */
class Debug {

    private Debug(){}

    static void Log(String message){
        try {
            PrintWriter file1 = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
            file1.println(message);
            file1.close();
        } catch (IOException e) {
            System.out.println(String.valueOf(e));
        }
    }

    static void initLog() {
        try {
            boolean file = new File("log.txt").isFile();
            if (file) {
                ArrayList<String> commands = new ArrayList<>();
                commands.add("mv");
                commands.add("log.txt");
                commands.add("log." + Instant.now() + "txt");
                ProcessBuilder processbuilder = new ProcessBuilder(commands);
                processbuilder.start();
            }
            OutputStream os = new FileOutputStream("log.txt");
            os.close();
        } catch (IOException e) {
            System.out.println(String.valueOf(e));
        }
    }
}

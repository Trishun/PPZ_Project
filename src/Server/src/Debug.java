import java.io.*;

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
            OutputStream os = new FileOutputStream("log.txt");
            os.close();
        } catch (IOException e) {
            System.out.println(String.valueOf(e));
        }
    }
}

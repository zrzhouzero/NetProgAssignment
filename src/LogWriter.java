import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * A class to handle log writing.
 */
public class LogWriter {

    public static void writeGameLog(String str) {
        final String GAME_LOG_FILE_NAME = "gamelog.txt";
        try {
            File file = new File(GAME_LOG_FILE_NAME);
            PrintWriter logWriter = new PrintWriter(new FileWriter(file, true), true);
            logWriter.println(str + " " + new Date().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeServerLog(String str) {
        final String SERVER_LOG_FILE_NAME = "serverlog.txt";
        try {
            File file = new File(SERVER_LOG_FILE_NAME);
            PrintWriter logWriter = new PrintWriter(new FileWriter(file, true), true);
            logWriter.println(str + " " + new Date().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/**
 * A class the store the client output string to support multi-language.
 */
public class ClientStrings {

    public static String inputIpAddress() {
        return "Please enter the server ip address: ";
    }

    public static String connectionEstablished(String ip, String port) {
        return "Connected to " + ip + ", port: " + port;
    }

    public static String gameOver() {
        return "Game Over!";
    }

    public static String playAgain() {
        return "Do you want to quit?\n" +
                "\"q\" for quit,\n" +
                "others to continue.";
    }

    public static String waiting() {
        return "Please wait.";
    }

}

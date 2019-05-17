import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class GameClient extends Project {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private static Scanner readFromConsole = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            new GameClient();
            System.out.println(ClientStrings.playAgain());
            String ans = readFromConsole.nextLine();

            // ask the player if to play again
            if (ans.toLowerCase().equals("q")) {
                System.exit(0);
            }
        }
    }

    private GameClient() {
        System.out.println(ClientStrings.inputIpAddress());
        String ip = readFromConsole.nextLine();

        socket = null;

        try {
            // initialise socket and establish connection
            socket = new Socket(ip, PORT_NUMBER);
            System.out.println(ClientStrings.connectionEstablished(ip, String.valueOf(PORT_NUMBER)));
            System.out.println(socket);
            System.out.println(ClientStrings.waiting());

            // initialise I/O streams
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // read from server: player id
            System.out.println(in.readUTF());

            // send player id to server
            out.writeUTF(readFromConsole.nextLine());

            // read from server: greetings
            System.out.println(in.readUTF());

            // read from server: game start
            System.out.println(in.readUTF());

            // read from server: game instructions
            System.out.println(in.readUTF());

            // declare variables to store if the player guessed the number and if the game needed to continue
            boolean guessed;
            boolean ifContinue;

            while (true) {
                // read from server: input a number
                System.out.println(in.readUTF());

                // write a number to the server
                out.writeUTF(readFromConsole.nextLine());

                // receive if the number is guessed and if the game needed to continue
                guessed = in.readBoolean();
                ifContinue = in.readBoolean();

                if (guessed) {
                    // read from server: the number is guessed
                    System.out.println(in.readUTF());
                } else {
                    // read from server: the number is not guessed
                    System.out.println(in.readUTF());

                    // read from server: if the target number is greater or lower
                    System.out.println(in.readUTF());
                }

                if (!ifContinue) {
                    // read from server when the game is over, print the result
                    System.out.println(in.readUTF());
                    break;
                }
            }

            // receive game result;
            // print the target number
            System.out.println(in.readUTF());
            // print waiting for other players to finish
            System.out.println(in.readUTF());
            // print the result of all players
            System.out.println(in.readUTF());
            // print the winner
            System.out.println(in.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println(ClientStrings.gameOver());
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}

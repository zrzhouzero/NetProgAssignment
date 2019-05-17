import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class GameServer extends Project {

    private ServerSocket serverSocket = null;

    // A queue to store players
    private Queue<Socket> playersInQueue = new LinkedList<>();

    // An array list to store players in game
    private ArrayList<Socket> playersInGame = new ArrayList<>();

    private GameServer() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Server is running and listening to port " + PORT_NUMBER);
            System.out.println(InetAddress.getLocalHost());
        } catch (IOException e) {
            e.printStackTrace();
            LogWriter.writeServerLog("Unable to establish connection!");
        }
    }

    private void start() {
        while (true) {
            String socketInfo = "";
            try {
                Socket socket = serverSocket.accept();
                socketInfo = socket.toString();
                LogWriter.writeServerLog(socketInfo + " connected!\t" + new Date());

                // start a thread for game
                Runnable r = () -> enqueue(socket);
                new Thread(r).start();
                LogWriter.writeServerLog(socketInfo + " disconnected!\t" + new Date());
            } catch (IOException e) {
                e.printStackTrace();
                LogWriter.writeServerLog(socketInfo + " error!\t" + new Date());
            }
        }
    }

    private void enqueue(Socket socket) {
        // put the client socket (player) into the queue
        // if there is only one player, start a single player game
        // if there are players in game, wait in the queue
        if (playersInGame.size() == 0 && playersInQueue.size() == 0) {
            playersInGame.add(socket);
            runGame();
        } else {
            playersInQueue.add(socket);
        }
    }

    private void runGame() {
        // if there are enough players (3 in this case), start the game
        // or if there are less than 3 players but there is no more players waiting in queue, start the game
        if (playersInGame.size() == Game.getMaxNumberOfPlayers() || playersInQueue.size() == 0) {
            // start game
            Game game = new Game();
            new GameThread(playersInGame, game).run();

            // reset game
            playersInGame = new ArrayList<>();

            // check if there are players in queue
            if (playersInQueue.size() > 0) {
                while (playersInQueue.size() > 0) {
                    Socket socket = playersInQueue.poll();
                    playersInGame.add(socket);

                    // check if the number of players is enough
                    if (playersInGame.size() == 3) break;
                }

                // start a new game again
                runGame();
            }
        }
    }

    public static void main(String[] args) {
        new GameServer().start();
    }

}
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GameThread extends Thread {

    private Game game;
    private ArrayList<Socket> players;

    // store the result of each players using a hash map
    private HashMap<Socket, PlayerResult> results;
    // store the on-game threads
    private ArrayList<Thread> gameThreads;

    public GameThread(ArrayList<Socket> players, Game game) {
        this.players = players;
        this.game = game;
        results = new HashMap<>();
        gameThreads = new ArrayList<>();
    }

    @Override
    public void run() {
        for (Socket socket : players) {
            Runnable r = () -> startGame(socket);
            Thread t = new Thread(r);
            t.start();
            gameThreads.add(t);
        }
        for (Thread t : gameThreads) {
            try {
                // the game finish together
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogWriter.writeServerLog(t + " error!\t" + new Date());
            }
        }
        broadcastResults();
        System.out.println("End Game.");
    }

    private void startGame(Socket socket) {
        try {
            // initialise I/O streams
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // ask players for their id
            out.writeUTF(ServerStrings.askForId());
            String playerId = in.readUTF();

            // initialise player's result
            PlayerResult result = new PlayerResult(playerId);

            // print player info on server
            System.out.println("Player " + playerId + " accepted!");

            // send greetings to players
            out.writeUTF(ServerStrings.greetings(playerId));

            // write to client the game start message and game instructions
            out.writeUTF(ServerStrings.gameStart());
            out.writeUTF(ServerStrings.gameInstruction());

            // declare the game-over condition
            int guessTaken = 0;
            boolean isGameOver = guessTaken >= game.getMaximumGuessCount();

            // start guess
            while (!isGameOver) {

                // ask players for input
                out.writeUTF(ServerStrings.askForInput());
                int input;

                // get players' input, and convert invalid input to 0
                try {
                    input = Integer.parseInt(in.readUTF());
                } catch (Exception e) {
                    input = 0;
                }

                // increase guesses the player takes
                result.increaseGuessesTaken();

                // check if the target number is guessed
                int guessed = game.guessNumber(input);

                // end the game when the number is guessed or the guess count exceed the max guess count
                if (guessed == 0) {
                    isGameOver = true;
                    result.setGuessed();
                } else {
                    isGameOver = result.getGuessesTaken() >= game.getMaximumGuessCount();
                }

                // update result
                results.put(socket, result);

                // write to client if the number is guessed and if the game is over
                out.writeBoolean(guessed == 0);
                out.writeBoolean(!isGameOver);

                // display message to players if the number is guessed, if not, display if their guess is greater or lower
                if (guessed == 0) {
                    out.writeUTF(ServerStrings.correctGuess());
                } else if (guessed > 0) {
                    out.writeUTF(ServerStrings.wrongGuess());
                    out.writeUTF(ServerStrings.answerLower());
                } else {
                    out.writeUTF(ServerStrings.wrongGuess());
                    out.writeUTF(ServerStrings.answerGreater());
                }

                if (isGameOver) {
                    // update the result board and write to the game log
                    result.setGameFinished();
                    results.put(socket, result);
                    LogWriter.writeGameLog(result.toString());
                    System.out.println(result.toString());

                    // write individual result to the clients
                    if (guessed == 0) {
                        out.writeUTF(ServerStrings.success(result.getGuessesTaken()));
                    } else {
                        out.writeUTF(ServerStrings.fail(game.getMaximumGuessCount()));
                    }
                    out.writeUTF(ServerStrings.correctNumber(game.getTargetNumber()));

                    // tell the clients to wait for other players
                    out.writeUTF(ServerStrings.waitingForOtherPlayersToFinish());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogWriter.writeServerLog(socket + " error!\t" + new Date());
        }
    }

    private void broadcastResults() {
        // set current minimum guess as maximum guess of the game
        int minimumGuesses = Game.getDefaultMaximumGuess();

        // declare a string array list to store the names of the winners
        ArrayList<String> winners = new ArrayList<>();

        // determine the minimum guess taken
        for (PlayerResult r : results.values()) {
            minimumGuesses = minimumGuesses <= r.getGuessesTaken() ? minimumGuesses : r.getGuessesTaken();
        }

        // add the names of players who used the minimum guesses
        for (PlayerResult r : results.values()) {
            if (r.getGuessesTaken() == minimumGuesses && r.isGuessed()) {
                winners.add(r.getPlayerName());
            }
        }

        // build the winner string
        StringBuilder stringBuilderForWinner = new StringBuilder();
        if (winners.size() == 0) {
            stringBuilderForWinner.append(ServerStrings.noWinners());
        } else {
            stringBuilderForWinner.append(winners.get(0));
            if (winners.size() > 1) {
                for (int i = 1; i < winners.size(); i++) {
                    stringBuilderForWinner.append(", ").append(winners.get(i));
                }
            }
            if (winners.size() == 1) {
                stringBuilderForWinner.append(ServerStrings.winner(true));
            } else {
                stringBuilderForWinner.append(ServerStrings.winner(false));
            }
        }

        // build the result string of all players and write the result to the log
        StringBuilder stringBuilderForAllResults = new StringBuilder();
        for (PlayerResult r : results.values()) {
            if (r.isGameFinished()) {
                stringBuilderForAllResults.append(r.toString()).append(System.lineSeparator());
            } else {
                stringBuilderForAllResults.append(ServerStrings.disconnectedPlayerResult(r.getPlayerName())).append(System.lineSeparator());
            }
        }

        // write the winners and the result to all the players
        for (Socket socket : players) {
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(stringBuilderForAllResults.toString());
                out.writeUTF(stringBuilderForWinner.toString());
            } catch (IOException e) {
                e.printStackTrace();
                LogWriter.writeServerLog(socket + " error!\t" + new Date());
            }
        }
    }

}

import java.util.Random;

public class Game {

    private int targetNumber;
    private int maximumGuessCount;

    private final static int DEFAULT_MAXIMUM_GUESS = 4;

    public Game() {
        generateTargetNumber();
        this.maximumGuessCount = DEFAULT_MAXIMUM_GUESS;
    }

    public Game(int maximumGuessCount) {
        generateTargetNumber();
        this.maximumGuessCount = maximumGuessCount;
    }

    public int getMaximumGuessCount() {
        return maximumGuessCount;
    }

    private void generateTargetNumber() {
        Random random = new Random();
        this.targetNumber = random.nextInt(10);
    }

    public int getTargetNumber() {
        return this.targetNumber;
    }

    public int guessNumber(int n) {
        return Integer.compare(n, targetNumber);
    }

    public static int getDefaultMaximumGuess() {
        return DEFAULT_MAXIMUM_GUESS;
    }
}

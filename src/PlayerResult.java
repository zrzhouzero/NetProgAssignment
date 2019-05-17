public class PlayerResult {

    private String playerName;
    private int guessesTaken;
    private boolean isGuessed;
    private boolean isGameFinished;

    public PlayerResult(String playerName) {
        this.playerName = playerName;
        guessesTaken = 0;
        isGuessed = false;
        isGameFinished = false;
    }

    public void setGuessed() {
        isGuessed = true;
    }

    public void increaseGuessesTaken() {
        this.guessesTaken++;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getGuessesTaken() {
        return guessesTaken;
    }

    public boolean isGuessed() {
        return isGuessed;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void setGameFinished() {
        isGameFinished = true;
    }

    @Override
    public String toString() {
        if (isGuessed) {
            return "Player " + playerName + " took " + guessesTaken + " guess(es).";
        } else {
            return "Player " + playerName + " failed to guess in 4 guesses.";
        }
    }
}

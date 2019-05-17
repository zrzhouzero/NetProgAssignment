public class ServerStrings {

    public static String gameStart() {
        return "Game Start";
    }

    public static String gameInstruction() {
        return  "|------------------ * Instructions * ------------------\n" +
                "|  Guess a number from 0 to 9,\n" +
                "|  the server will tell if the number is greater or lower.\n" +
                "|  Then you can guess again.\n" +
                "|  When the correct number is guessed, or the maximum guess is reached,\n" +
                "|  the game is over.\n" +
                "|  Note: if invalid input occurs, the server will considered it 0.\n" +
                "|---------------------- * above * ---------------------";
    }

    public static String askForInput() {
        return "Please Guess a Number From 0 to 9: ";
    }

    public static String correctGuess() {
        return "Correct!";
    }

    public static String wrongGuess() {
        return "Wrong!";
    }

    public static String answerGreater() {
        return "The Answer is greater than your guess";
    }

    public static String answerLower() {
        return "The Answer is lower than your guess";
    }

    public static String success(int count) {
        if (count > 1) {
            return "Congratulations! You used " + count + " times to guess the number!";
        } else return "Congratulations! You used " + count + " time to guess the number!";
    }

    public static String fail(int count) {
        return "Sorry! You failed to guess the number in " + count + " guesses.";
    }

    public static String winner(boolean b) {
        if (b) {
            return " is the winner!";
        } else {
            return " are the winners!";
        }
    }

    public static String askForId() {
        return "Enter your name please:";
    }

    public static String greetings(String userId) {
        return "Hello " + userId + "!";
    }

    public static String noWinners() {
        return "There is no winner.";
    }

    public static String correctNumber(int num) {
        return "The target number is " + num + ".";
    }

    public static String disconnectedPlayerResult(String name) {
        return "Player " + name + " left the game before end.";
    }

    public static String waitingForOtherPlayersToFinish() {
        return "Waiting for other players to finish...";
    }

}

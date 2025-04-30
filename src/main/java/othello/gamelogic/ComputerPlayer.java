package othello.gamelogic;

/**
 * Represents a computer player that will make decisions autonomously during their turns.
 * Employs a specific computer strategy passed in through program arguments.
 */
public class ComputerPlayer extends Player {
    private AIStrategy strategy;


    public ComputerPlayer(String strategyName) {
        // PART 2
        // TODO: Use the strategyName input to create a specific strategy class for this computer
        // This input should match the ones specified in App.java!
        switch (strategyName.replaceAll("\\s+", "").toLowerCase()) {
            case "expectedmax":
                //this.strategy = new ExpectedmaxStrategy();
                break;
            case "minimax":
                //this.strategy = new MinimaxStrategy();
                break;
            case "cnn":
                this.strategy = new NeuralNetworkStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        }
    }

    // implement a method that returns a BoardSpace that a strategy selects
    public BoardSpace selectMove(BoardSpace[][] board, Player opponent) {
        return strategy.chooseMove(board, this, opponent);
    }
}
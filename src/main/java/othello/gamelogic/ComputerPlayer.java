package othello.gamelogic;

import othello.ai.AIStrategy;
import othello.ai.Minimax;
import othello.ai.MCTS;
import othello.ai.NeuralNetworkStrategy;

/**
 * Represents a computer player that will make decisions autonomously during their turns.
 * Employs a specific computer strategy passed in through program arguments.
 */
public class ComputerPlayer extends Player{
    private final AIStrategy strategy;

    public ComputerPlayer(String strategyName) {
        // PART 2
        // TODO: Use the strategyName input to create a specific strategy class for this computer
        // This input should match the ones specified in App.java!
        switch (strategyName.toLowerCase()) {
            case "minimax":
                this.strategy = new Minimax();
                break;
            case "mcts":
                this.strategy = new MCTS();
                break;
            case "custom":
                // your third algorithm; here we use the NN you wrote
                this.strategy = new NeuralNetworkStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown AI strategy: " + strategyName);
        }
    }

    // PART 2
    // TODO: implement a method that returns a BoardSpace that a strategy selects
    public BoardSpace chooseMove(BoardSpace[][] board, Player opponent) {
        return strategy.chooseMove(board, this, opponent);
    }
}
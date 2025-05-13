package othello.ai;

import othello.gamelogic.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NeuralNetworkVsMinimaxMCTSTest {
    //this is going to talk a very long time if you do 1000 times.
    //You can do it within 5 or 10 times
    private static final int NUM_GAMES = 1000;

    static class NNPlayer extends ComputerPlayer {
        public NNPlayer() {
            super("cnn");
            setColor(BoardSpace.SpaceType.BLACK);
        }
    }

    static class MCTSPlayer extends ComputerPlayer {
        public MCTSPlayer() {
            super("mcts");
            setColor(BoardSpace.SpaceType.WHITE);
        }
    }

    static class MinimaxPlayer extends ComputerPlayer {
        public MinimaxPlayer() {
            super("minimax");
            setColor(BoardSpace.SpaceType.WHITE);
        }
    }

    @Test
    public void testNeuralNetworkWinsAbove80PercentMCTS() {
        int neuralWins = 0;
        int draws = 0;

        for (int i = 0; i < NUM_GAMES; i++) {
            NNPlayer nn = new NNPlayer();
            MCTSPlayer mcts = new MCTSPlayer();
            OthelloGame game = new OthelloGame(nn, mcts);

            BoardSpace[][] board = game.getBoard();
            Player current = nn;
            Player opponent = mcts;

            while (true) {
                Map<BoardSpace, List<BoardSpace>> moves = game.getAvailableMoves(current);
                if (!moves.isEmpty()) {
                    BoardSpace move = game.computerDecision((ComputerPlayer) current);
                    if (move != null) {
                        game.takeSpaces(current, opponent, moves, move);
                    }
                }

                // Check if both players have no moves
                boolean currentHasMoves = !game.getAvailableMoves(current).isEmpty();
                boolean opponentHasMoves = !game.getAvailableMoves(opponent).isEmpty();
                if (!currentHasMoves && !opponentHasMoves) break;

                // Swap
                Player temp = current;
                current = opponent;
                opponent = temp;
            }

            int nnCount = countDiscs(board, nn.getColor());
            int mmCount = countDiscs(board, mcts.getColor());

            if (nnCount > mmCount) neuralWins++;
            else if (nnCount == mmCount) draws++;
        }

        double winRate = neuralWins * 1.0 / NUM_GAMES;
        System.out.println("Neural wins: " + neuralWins + "/" + NUM_GAMES + " (" + (winRate * 100) + "%)");

        assertTrue(winRate >= 0.80, "Neural network strategy should win at least 80% of games");
    }


    @Test
    public void testNeuralNetworkWinsAbove80PercentMinimax() {
        int neuralWins = 0;
        int draws = 0;

        for (int i = 0; i < NUM_GAMES; i++) {
            NNPlayer nn = new NNPlayer();
            MinimaxPlayer minimax = new MinimaxPlayer();
            OthelloGame game = new OthelloGame(nn, minimax);

            BoardSpace[][] board = game.getBoard();
            Player current = nn;
            Player opponent = minimax;

            while (true) {
                Map<BoardSpace, List<BoardSpace>> moves = game.getAvailableMoves(current);
                if (!moves.isEmpty()) {
                    BoardSpace move = game.computerDecision((ComputerPlayer) current);
                    if (move != null) {
                        game.takeSpaces(current, opponent, moves, move);
                    }
                }

                // Check if both players have no moves
                boolean currentHasMoves = !game.getAvailableMoves(current).isEmpty();
                boolean opponentHasMoves = !game.getAvailableMoves(opponent).isEmpty();
                if (!currentHasMoves && !opponentHasMoves) break;

                // Swap
                Player temp = current;
                current = opponent;
                opponent = temp;
            }

            int nnCount = countDiscs(board, nn.getColor());
            int mmCount = countDiscs(board, minimax.getColor());

            if (nnCount > mmCount) neuralWins++;
            else if (nnCount == mmCount) draws++;
        }

        double winRate = neuralWins * 1.0 / NUM_GAMES;
        System.out.println("Neural wins: " + neuralWins + "/" + NUM_GAMES + " (" + (winRate * 100) + "%)");

        assertTrue(winRate >= 0.80, "Neural network strategy should win at least 80% of games");
    }

    private int countDiscs(BoardSpace[][] board, BoardSpace.SpaceType color) {
        int count = 0;
        for (BoardSpace[] row : board) {
            for (BoardSpace s : row) {
                if (s.getType() == color) count++;
            }
        }
        return count;
    }
}

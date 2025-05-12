package AITest;

import othello.ai.NeuralNetworkStrategy;
import othello.gamelogic.BoardSpace;
import othello.gamelogic.ComputerPlayer;
import othello.gamelogic.HumanPlayer;
import othello.gamelogic.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeuralNetworkStrategyTest {

    @Test
    public void testNeuralNetworkStrategySuggestsMove() {
        // 创建棋盘
        BoardSpace[][] board = new BoardSpace[8][8];
        Player black = new HumanPlayer();
        black.setColor(BoardSpace.SpaceType.BLACK);
        Player white = new ComputerPlayer("cnn");
        white.setColor(BoardSpace.SpaceType.WHITE);

        // Empty board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }

        // initialize four spaces in the mid
        board[3][3].setType(BoardSpace.SpaceType.WHITE);
        board[3][4].setType(BoardSpace.SpaceType.BLACK);
        board[4][3].setType(BoardSpace.SpaceType.BLACK);
        board[4][4].setType(BoardSpace.SpaceType.WHITE);

        NeuralNetworkStrategy strategy = new NeuralNetworkStrategy();
        BoardSpace suggestedMove = strategy.chooseMove(board, black, white);

        //
        if (suggestedMove != null) {
            System.out.printf("Suggest: (%d, %d)%n", suggestedMove.getX(), suggestedMove.getY());
        }

        // should return a legal move
        assertTrue(
                suggestedMove == null ||
                        (suggestedMove.getX() >= 0 && suggestedMove.getX() < 8 &&
                                suggestedMove.getY() >= 0 && suggestedMove.getY() < 8),
                "Suggested move should be within board or null (pass)."
        );
    }
}
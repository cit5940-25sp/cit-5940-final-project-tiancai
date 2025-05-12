package AITest;

import othello.ai.Minimax;
import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxTest {

    /**
     * A tiny Player subclass where we can manually seed owned spaces.
     */
    static class TestPlayer extends Player {
        public TestPlayer(BoardSpace.SpaceType color) {
            setColor(color);
        }
    }

    /**
     * Scenario: only one legal move exists (0,2), so Minimax must pick it.
     * Board:
     *   B W .
     *   . . .
     *   . . .
     */
    @Test
    void chooseMove_singleOption() {
        // build an 8×8 empty board
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }
        // place a Black at (0,0) and a White at (0,1)
        board[0][0].setType(BoardSpace.SpaceType.BLACK);
        board[0][1].setType(BoardSpace.SpaceType.WHITE);

        // create players and seed their owned spaces
        TestPlayer black = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer white = new TestPlayer(BoardSpace.SpaceType.WHITE);
        black.getPlayerOwnedSpacesSpaces().add(board[0][0]);
        white.getPlayerOwnedSpacesSpaces().add(board[0][1]);

        // run Minimax
        Minimax ai = new Minimax();
        BoardSpace choice = ai.chooseMove(board, black, white);

        // the only legal flip is at (0,2)
        assertNotNull(choice, "Expected a move, not null");
        assertEquals(0, choice.getX());
        assertEquals(2, choice.getY());
    }

    /**
     * Scenario: no legal moves for Black → chooseMove should return null.
     */
    @Test
    void chooseMove_noMoves() {
        // 8×8 empty board with a single Black that has no flips possible
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }
        board[4][4].setType(BoardSpace.SpaceType.BLACK);

        TestPlayer black = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer white = new TestPlayer(BoardSpace.SpaceType.WHITE);
        black.getPlayerOwnedSpacesSpaces().add(board[4][4]);

        Minimax ai = new Minimax();
        BoardSpace choice = ai.chooseMove(board, black, white);

        assertNull(choice, "With no legal moves, AI should return null");
    }
}
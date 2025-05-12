package othello.ai;

import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxTest {

    // A tiny Player subclass where we can manually seed owned spaces.
    static class TestPlayer extends Player {
        TestPlayer(BoardSpace.SpaceType color) {
            setColor(color);
        }
    }

    /**
     * 1) Single legal move → pick it.
     */
    @Test
    void chooseMove_singleOption() {
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);

        // B at (0,0), W at (0,1)
        board[0][0].setType(BoardSpace.SpaceType.BLACK);
        board[0][1].setType(BoardSpace.SpaceType.WHITE);

        TestPlayer black = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer white = new TestPlayer(BoardSpace.SpaceType.WHITE);
        black.getPlayerOwnedSpacesSpaces().add(board[0][0]);
        white.getPlayerOwnedSpacesSpaces().add(board[0][1]);

        Minimax ai = new Minimax();
        BoardSpace choice = ai.chooseMove(board, black, white);

        assertNotNull(choice,       "Expected a move, not null");
        assertEquals(0, choice.getX());
        assertEquals(2, choice.getY());
    }

    /**
     * 2) No legal moves → chooseMove returns null.
     */
    @Test
    void chooseMove_noMoves() {
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);

        board[4][4].setType(BoardSpace.SpaceType.BLACK);
        TestPlayer black = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer white = new TestPlayer(BoardSpace.SpaceType.WHITE);
        black.getPlayerOwnedSpacesSpaces().add(board[4][4]);

        Minimax ai = new Minimax();
        assertNull(ai.chooseMove(board, black, white),
                "With no legal moves, AI should return null");
    }

    /**
     * 3) “Pass‐then‐move” scenario: force the recursion branch
     *    where player has moves, opponent has none.
     */
    @Test
    void chooseMove_passesWhenOpponentHasNoMoves() {
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);

        // Black at (3,3), White at (3,4)
        board[3][3].setType(BoardSpace.SpaceType.BLACK);
        board[3][4].setType(BoardSpace.SpaceType.WHITE);

        // Black also at (4,4) so that White will have no flips
        board[4][4].setType(BoardSpace.SpaceType.BLACK);

        TestPlayer black = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer white = new TestPlayer(BoardSpace.SpaceType.WHITE);
        black.getPlayerOwnedSpacesSpaces().add(board[3][3]);
        black.getPlayerOwnedSpacesSpaces().add(board[4][4]);
        white.getPlayerOwnedSpacesSpaces().add(board[3][4]);

        Minimax ai = new Minimax();
        BoardSpace choice = ai.chooseMove(board, black, white);

        // Black still has exactly one flip (at (3,5)), and white has none
        assertNotNull(choice);
        assertEquals(3, choice.getX());
        assertEquals(5, choice.getY());
    }

    /**
     * 4) exercise private evaluate(...) loop via reflection
     */
    @Test
    void evaluate_weightsDifference() throws Exception {
        // place one BLACK in a  corner weight(0,0)=200 and one WHITE at (0,1) weight=-70
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);

        board[0][0].setType(BoardSpace.SpaceType.BLACK);
        board[0][1].setType(BoardSpace.SpaceType.WHITE);

        Minimax ai = new Minimax();
        Method eval = Minimax.class.getDeclaredMethod(
                "evaluate", BoardSpace[][].class, BoardSpace.SpaceType.class);
        eval.setAccessible(true);

        // expected = +200 (black) – (–70) from white = 270
        int score = (int) eval.invoke(ai, board, BoardSpace.SpaceType.BLACK);
        assertEquals(200 - (-70), score);
    }

    /**
     * 5) exercise private gameOver(...) via reflection for full‐board case
     */
    @Test
    void gameOver_detectsFullBoard() throws Exception {
        BoardSpace[][] full = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                full[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.BLACK);

        Minimax ai = new Minimax();
        Method over = Minimax.class.getDeclaredMethod(
                "gameOver",
                BoardSpace[][].class,
                Player.class,
                Player.class
        );
        over.setAccessible(true);

        // both players have no moves on a full board
        TestPlayer p1 = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer p2 = new TestPlayer(BoardSpace.SpaceType.WHITE);

        Object result = over.invoke(ai, full, p1, p2);
        assertTrue((Boolean) result, "Full board → gameOver must return true");
    }
}
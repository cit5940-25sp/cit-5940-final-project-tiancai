package AITest;


import othello.ai.DummyPlayer;
import othello.ai.MCTS;
import othello.gamelogic.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MCTSTest {

    @Test
    public void testChooseMoveReturnsNonNull() {
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }
        // Set up initial Othello positions
        board[3][3].setType(BoardSpace.SpaceType.WHITE);
        board[3][4].setType(BoardSpace.SpaceType.BLACK);
        board[4][3].setType(BoardSpace.SpaceType.BLACK);
        board[4][4].setType(BoardSpace.SpaceType.WHITE);

        Player player = new DummyPlayer(BoardSpace.SpaceType.BLACK);
        Player opponent = new DummyPlayer(BoardSpace.SpaceType.WHITE);
        // Populate playerOwnedSpaces so getAvailableMoves works
        for (BoardSpace[] row : board) {
            for (BoardSpace space : row) {
                if (space.getType() == BoardSpace.SpaceType.BLACK) {
                    player.getPlayerOwnedSpacesSpaces().add(space);
                } else if (space.getType() == BoardSpace.SpaceType.WHITE) {
                    opponent.getPlayerOwnedSpacesSpaces().add(space);
                }
            }
        }
        MCTS mcts = new MCTS();
        BoardSpace move = mcts.chooseMove(board, player, opponent);
        assertNotNull(move, "MCTS should return a non-null move on a valid board");
    }

    @Test
    public void testChooseMoveOnFullBoardReturnsNull() {
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.BLACK);
            }
        }
        Player player = new DummyPlayer(BoardSpace.SpaceType.BLACK);
        Player opponent = new DummyPlayer(BoardSpace.SpaceType.WHITE);

        MCTS mcts = new MCTS();
        BoardSpace move = mcts.chooseMove(board, player, opponent);
        assertNull(move, "MCTS should return null if the board is full");
    }
}

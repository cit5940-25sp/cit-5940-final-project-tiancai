import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import othello.gamelogic.*;

import java.util.*;

import static javafx.beans.binding.Bindings.when;
import static javax.management.Query.eq;

import static org.junit.jupiter.api.Assertions.*;


public class OthelloGameTest {

    private Player blackPlayer;
    private Player whitePlayer;
    private OthelloGame game;

    @BeforeEach
    public void setUp() {
        blackPlayer = new HumanPlayer();
        blackPlayer.setColor(BoardSpace.SpaceType.BLACK);
        whitePlayer = new HumanPlayer();
        whitePlayer.setColor(BoardSpace.SpaceType.WHITE);
        game = new OthelloGame(blackPlayer, whitePlayer);
    }

    @Test
    public void testBoardInitialization() {
        BoardSpace[][] board = game.getBoard();
        assertEquals(8, board.length);
        assertEquals(8, board[0].length);

        // mid four check
        assertEquals(BoardSpace.SpaceType.BLACK, board[3][3].getType());
        assertEquals(BoardSpace.SpaceType.BLACK, board[4][4].getType());
        assertEquals(BoardSpace.SpaceType.WHITE, board[3][4].getType());
        assertEquals(BoardSpace.SpaceType.WHITE, board[4][3].getType());
    }


    @Test
    public void testInvalidPlayerColorsThrows() {
        Player black1 = new ComputerPlayer("cnn");
        black1.setColor(BoardSpace.SpaceType.BLACK);
        Player black2 = new ComputerPlayer("cnn");
        black2.setColor(BoardSpace.SpaceType.BLACK);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new OthelloGame(black1, black2);
        });
        assertTrue(ex.getMessage().contains("Players have to have different colors"));
    }

    @Test
    public void testInvalidPlayerColorsThrows2() {
        Player black1 = new ComputerPlayer("cnn");
        black1.setColor(BoardSpace.SpaceType.EMPTY);
        Player black2 = new ComputerPlayer("cnn");
        black2.setColor(BoardSpace.SpaceType.BLACK);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new OthelloGame(black1, black2);
        });
        assertTrue(ex.getMessage().contains("Players have to choose colors"));
    }

    @Test
    public void testInvalidPlayerColorsThrows3() {
        Player black1 = new ComputerPlayer("cnn");
        black1.setColor(BoardSpace.SpaceType.WHITE);
        Player black2 = new ComputerPlayer("cnn");
        black2.setColor(BoardSpace.SpaceType.BLACK);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new OthelloGame(black1, black2);
        });
        assertTrue(ex.getMessage().contains("Player1 have to use Black"));
    }


    @Test
    public void testTakeSpaceAddsToActingPlayer() {
        game.takeSpace(blackPlayer, whitePlayer, 2, 2);
        BoardSpace space = game.getBoard()[2][2];

        assertEquals(BoardSpace.SpaceType.BLACK, space.getType());
        assertTrue(blackPlayer.getPlayerOwnedSpacesSpaces().contains(space));
        assertFalse(whitePlayer.getPlayerOwnedSpacesSpaces().contains(space));
    }

    @Test
    public void testTakeSpaceDoesNothingIfAlreadyOwned() {
        // (3,4) 初始为 BLACK（owned by black）
        game.takeSpace(blackPlayer, whitePlayer, 3, 4);
        // 不应重复添加
        long count = blackPlayer.getPlayerOwnedSpacesSpaces().stream()
                .filter(space -> space.getX() == 3 && space.getY() == 4).count();
        assertEquals(1, count);
    }

    @Test
    public void testTakeSpacesFlipsCorrectly() {
        // 造一个简单局面：
        // B W . → 黑在 (0,0)，白在 (0,1)，我们准备让黑下 (0,2) 翻转 (0,1)

        BoardSpace[][] board = game.getBoard();
        board[0][0].setType(BoardSpace.SpaceType.BLACK);
        board[0][1].setType(BoardSpace.SpaceType.WHITE);
        board[0][2].setType(BoardSpace.SpaceType.EMPTY);

        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[0][0]);
        whitePlayer.getPlayerOwnedSpacesSpaces().add(board[0][1]);

        Map<BoardSpace, List<BoardSpace>> availableMoves = new HashMap<>();
        availableMoves.put(board[0][2], List.of(board[0][0]));

        game.takeSpaces(blackPlayer, whitePlayer, availableMoves, board[0][2]);

        // 断言 (0,1) 被翻转成黑色，且 ownership 更新
        assertEquals(BoardSpace.SpaceType.BLACK, board[0][1].getType());
        assertTrue(blackPlayer.getPlayerOwnedSpacesSpaces().contains(board[0][1]));
        assertFalse(whitePlayer.getPlayerOwnedSpacesSpaces().contains(board[0][1]));
    }

    @Test
    public void testComputerDecisionDelegatesToComputer() {
        // fake move
        BoardSpace expectedMove = new BoardSpace(2, 2, BoardSpace.SpaceType.BLACK);

        // over write the choose move method to return the fake move
        ComputerPlayer computer = new ComputerPlayer("cnn") {
            @Override
            public BoardSpace chooseMove(BoardSpace[][] board, Player opponent) {
                return expectedMove;
            }
        };
        computer.setColor(BoardSpace.SpaceType.BLACK);

        Player opponent = new HumanPlayer();
        opponent.setColor(BoardSpace.SpaceType.WHITE);

        //computer as player1 and opponent as play2
        OthelloGame game = new OthelloGame(computer, opponent);

        // make decision
        BoardSpace move = game.computerDecision(computer);

        // if the chooseMove is as expected
        assertEquals(expectedMove, move);
    }




}

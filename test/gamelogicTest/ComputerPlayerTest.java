package gamelogicTest;

import othello.ai.AIStrategy;
import org.junit.jupiter.api.Test;
import othello.gamelogic.BoardSpace;
import othello.gamelogic.ComputerPlayer;
import othello.gamelogic.Player;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerPlayerTest {

    // manual AIStrategy
    static class DummyStrategy implements AIStrategy {
        private final BoardSpace fixedMove;

        public DummyStrategy(BoardSpace move) {
            this.fixedMove = move;
        }

        @Override
        public BoardSpace chooseMove(BoardSpace[][] board, Player self, Player opponent) {
            return fixedMove;
        }
    }


    @Test
    public void testChooseMoveReturnsStrategyDecision() {
        BoardSpace expectedMove = new BoardSpace(4, 5, BoardSpace.SpaceType.BLACK);
        AIStrategy dummy = new DummyStrategy(expectedMove);

        // dummy computer player
        ComputerPlayer computer = new ComputerPlayer("cnn") {
            @Override
            public BoardSpace chooseMove(BoardSpace[][] board, Player opponent) {
                return dummy.chooseMove(board, this, opponent);
            }
        };
        computer.setColor(BoardSpace.SpaceType.BLACK);
        assertEquals(computer.getColor(), BoardSpace.SpaceType.BLACK);

        Player opponent = new Player() {
            { setColor(BoardSpace.SpaceType.WHITE); }
        };

        BoardSpace[][] dummyBoard = new BoardSpace[8][8];
        BoardSpace result = computer.chooseMove(dummyBoard, opponent);
        assertEquals(expectedMove, result);
    }

    @Test
    public void testUnknownStrategyThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ComputerPlayer("unknown");
        });
        assertTrue(exception.getMessage().contains("Unknown AI strategy"));
    }

    static class TestPlayer extends Player {
        public TestPlayer(BoardSpace.SpaceType color) {
            setColor(color);
        }
    }

    @Test
    public void testAvailableMovesSimpleScenario() {
        // construct a 8x8 board manually
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }

        // set the board
        board[3][3].setType(BoardSpace.SpaceType.BLACK);
        board[3][4].setType(BoardSpace.SpaceType.WHITE);

        TestPlayer blackPlayer = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer whitePlayer = new TestPlayer(BoardSpace.SpaceType.WHITE);
        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[3][3]);
        whitePlayer.getPlayerOwnedSpacesSpaces().add(board[3][4]);

        // then get moves
        Map<BoardSpace, List<BoardSpace>> moves = blackPlayer.getAvailableMoves(board);
        BoardSpace expectedDest = board[3][5];
        assertTrue(moves.containsKey(expectedDest), "Should have move at (3,5)");
        List<BoardSpace> origins = moves.get(expectedDest);
        assertEquals(1, origins.size(), "Only one origin expected for this move");
        assertEquals(board[3][3], origins.get(0), "Origin should be (3,3)");
    }


    @Test
    public void testAvailableMovesMultipleScenario() {
        // construct a 8x8 board manually
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }

        // set the board
        /*  o x
            x o
            x o
          x o
         */
        board[3][3].setType(BoardSpace.SpaceType.BLACK);
        board[5][2].setType(BoardSpace.SpaceType.BLACK);
        board[4][3].setType(BoardSpace.SpaceType.BLACK);
        board[2][4].setType(BoardSpace.SpaceType.BLACK);
        board[2][3].setType(BoardSpace.SpaceType.WHITE);
        board[4][4].setType(BoardSpace.SpaceType.WHITE);
        board[3][4].setType(BoardSpace.SpaceType.WHITE);
        board[5][3].setType(BoardSpace.SpaceType.WHITE);

        TestPlayer blackPlayer = new TestPlayer(BoardSpace.SpaceType.BLACK);
        TestPlayer whitePlayer = new TestPlayer(BoardSpace.SpaceType.WHITE);
        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[3][3]);
        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[5][2]);
        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[4][3]);
        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[2][4]);
        whitePlayer.getPlayerOwnedSpacesSpaces().add(board[3][4]);
        whitePlayer.getPlayerOwnedSpacesSpaces().add(board[4][4]);
        whitePlayer.getPlayerOwnedSpacesSpaces().add(board[2][3]);
        whitePlayer.getPlayerOwnedSpacesSpaces().add(board[5][3]);

        // then get moves
        Map<BoardSpace, List<BoardSpace>> moves = blackPlayer.getAvailableMoves(board);

        assertTrue(moves.containsKey(board[3][5]), "Should have move at (3,5)");
        assertTrue(moves.containsKey(board[4][5]), "Should have move at (4,5)");
        assertTrue(moves.containsKey(board[2][2]), "Should have move at (2,2)");
        assertTrue(moves.containsKey(board[5][4]), "Should have move at (5,4)");
        List<BoardSpace> origins = moves.get(board[5][4]);

        assertEquals(2, origins.size(), "2 space");
        assertEquals(board[5][2], origins.get(0), "should be (5,2)");
        assertEquals(board[2][4], origins.get(1), "should be (2,4)");
    }

    @Test
    public void testNoAvailableMoves() {
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }

        // only a black one
        board[4][4].setType(BoardSpace.SpaceType.BLACK);
        TestPlayer blackPlayer = new TestPlayer(BoardSpace.SpaceType.BLACK);
        blackPlayer.getPlayerOwnedSpacesSpaces().add(board[4][4]);

        Map<BoardSpace, List<BoardSpace>> moves = blackPlayer.getAvailableMoves(board);
        assertTrue(moves.isEmpty(), "Should have no available moves");
    }
}
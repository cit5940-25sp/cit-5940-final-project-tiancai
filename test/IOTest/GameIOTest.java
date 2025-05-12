package othello.io;

import othello.gamelogic.BoardSpace;
import othello.gamelogic.GameMemento;
import othello.gamelogic.HumanPlayer;
import othello.gamelogic.OthelloGame;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercises save/load of a GameMemento via GameIO.
 */
class GameIOTest {

    private Path tempFile;

    @BeforeEach
    void createTempFile() throws IOException {
        tempFile = Files.createTempFile("othello-save-", ".otsav");
    }

    @AfterEach
    void deleteTempFile() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSaveAndLoadGameMemento() throws Exception {
        // --- set up a simple board with one move made ---
        HumanPlayer black = new HumanPlayer();
        black.setColor(BoardSpace.SpaceType.BLACK);
        HumanPlayer white = new HumanPlayer();
        white.setColor(BoardSpace.SpaceType.WHITE);

        OthelloGame game = new OthelloGame(black, white);

        // make exactly one legal move so state mutates
        Map<BoardSpace, List<BoardSpace>> movesForBlack = game.getAvailableMoves(black);
        assertFalse(movesForBlack.isEmpty(), "Should have at least one move");
        BoardSpace dest = movesForBlack.keySet().iterator().next();
        game.takeSpaces(black, white, movesForBlack, dest);

        // snapshot
        GameMemento before = game.save();

        // write to disk
        GameIO.saveToFile(before, tempFile);

        // read back
        GameMemento after = GameIO.loadFromFile(tempFile);

        // board arrays should match
        BoardSpace[][] b1 = before.getBoard();
        BoardSpace[][] b2 = after.getBoard();
        assertEquals(b1.length, b2.length, "board height");
        assertEquals(b1[0].length, b2[0].length, "board width");
        for (int i = 0; i < b1.length; i++) {
            for (int j = 0; j < b1[i].length; j++) {
                assertEquals(b1[i][j].getType(), b2[i][j].getType(),
                        "space at ("+i+","+j+")");
            }
        }

        // owned‐spaces lists should match
        List<BoardSpace> p1Before = before.getP1Spaces();
        List<BoardSpace> p1After  = after .getP1Spaces();
        assertEquals(p1Before.size(), p1After.size(), "player1 owned count");
        for (int k = 0; k < p1Before.size(); k++) {
            assertEquals(p1Before.get(k).getX(), p1After.get(k).getX(),
                    "p1 space["+k+"].x");
            assertEquals(p1Before.get(k).getY(), p1After.get(k).getY(),
                    "p1 space["+k+"].y");
        }

        List<BoardSpace> p2Before = before.getP2Spaces();
        List<BoardSpace> p2After  = after .getP2Spaces();
        assertEquals(p2Before.size(), p2After.size(), "player2 owned count");
        for (int k = 0; k < p2Before.size(); k++) {
            assertEquals(p2Before.get(k).getX(), p2After.get(k).getX(),
                    "p2 space["+k+"].x");
            assertEquals(p2Before.get(k).getY(), p2After.get(k).getY(),
                    "p2 space["+k+"].y");
        }
    }

    @Test
    void saveToNonExistingDirectoryThrows() throws Exception {
        Path badPath = tempFile.getParent()
                .resolve("no-such-dir")
                .resolve("file.otsav");
        // create a valid empty board
        BoardSpace[][] board = new BoardSpace[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }
        GameMemento dummy = new GameMemento(board, List.of(), List.of());
        assertThrows(IOException.class, () -> GameIO.saveToFile(dummy, badPath));
    }
}
package gamelogicTest;

import org.junit.jupiter.api.Test;
import othello.gamelogic.BoardSpace;

import javafx.scene.paint.Color;
import static org.junit.jupiter.api.Assertions.*;

public class BoardSpaceTest {

    @Test
    public void testConstructorAndGetters() {
        //diff size of board space
        BoardSpace space = new BoardSpace(2, 3, BoardSpace.SpaceType.BLACK);
        assertEquals(2, space.getX());
        assertEquals(3, space.getY());
        assertEquals(BoardSpace.SpaceType.BLACK, space.getType());
    }

    @Test
    public void testCopyConstructorAndGetters() {
        //diff size of board space
        BoardSpace space = new BoardSpace(1, 22, BoardSpace.SpaceType.WHITE);
        BoardSpace copy = new BoardSpace(space);
        assertEquals(1, copy.getX());
        assertEquals(22, copy.getY());
        assertEquals(BoardSpace.SpaceType.WHITE, space.getType());
    }

    @Test
    public void testSetType() {
        //set type
        BoardSpace space = new BoardSpace(0, 0, BoardSpace.SpaceType.EMPTY);
        space.setType(BoardSpace.SpaceType.WHITE);
        assertEquals(BoardSpace.SpaceType.WHITE, space.getType());
    }

    @Test
    public void testEquals() {
        //set type
        BoardSpace space1 = new BoardSpace(0, 0, BoardSpace.SpaceType.EMPTY);
        BoardSpace space = space1;
        BoardSpace space2 = new BoardSpace(0, 0, BoardSpace.SpaceType.WHITE);
        assertEquals(space1, space);
        assertEquals(space1, space2);
    }



    @Test
    public void testCopyConstructor() {
        BoardSpace original = new BoardSpace(4, 5, BoardSpace.SpaceType.BLACK);
        BoardSpace copy = new BoardSpace(original);
        assertEquals(4, copy.getX());
        assertEquals(5, copy.getY());
        assertEquals(BoardSpace.SpaceType.BLACK, copy.getType());
    }

    @Test
    public void testDeepCopy() {
        // Create original board
        BoardSpace[][] original = new BoardSpace[2][2];
        original[0][0] = new BoardSpace(0,0,BoardSpace.SpaceType.WHITE);
        original[0][1] = new BoardSpace(0,1,BoardSpace.SpaceType.BLACK);
        original[1][0] = new BoardSpace(1,0,BoardSpace.SpaceType.WHITE);
        original[1][1] = new BoardSpace(1,1,BoardSpace.SpaceType.EMPTY);

        // Deep copy
        BoardSpace[][] copy = BoardSpace.deepCopy(original);

        // Check values are equal
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                assertEquals(original[i][j].getType(), copy[i][j].getType(),
                        "Copied element should have the same type");
            }
        }

        // Check it's a deep copy (different object references)
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                assertNotSame(original[i][j], copy[i][j],
                        "Copied element should be a different object");
            }
        }

        // Modify copy and check original is unaffected
        copy[0][0] = new BoardSpace(0,0,BoardSpace.SpaceType.EMPTY);
        assertEquals(BoardSpace.SpaceType.WHITE, original[0][0].getType(), "Original should remain unchanged");
    }

//spacetype test
    @Test
    public void testEnumColorMapping() {
        assertNotNull(BoardSpace.SpaceType.BLACK.fill());
        assertNotNull(BoardSpace.SpaceType.WHITE.fill());
        assertNotNull(BoardSpace.SpaceType.EMPTY.fill());
    }

    @Test
    public void testEnumValuesExist() {
        assertNotNull(BoardSpace.SpaceType.EMPTY);
        assertNotNull(BoardSpace.SpaceType.BLACK);
        assertNotNull(BoardSpace.SpaceType.WHITE);
    }

    @Test
    public void testFillColor() {
        assertEquals(Color.GRAY, BoardSpace.SpaceType.EMPTY.fill(), "EMPTY should map to GRAY");
        assertEquals(Color.BLACK, BoardSpace.SpaceType.BLACK.fill(), "BLACK should map to BLACK");
        assertEquals(Color.WHITE, BoardSpace.SpaceType.WHITE.fill(), "WHITE should map to WHITE");
    }

    @Test
    public void testEnumName() {
        assertEquals("EMPTY", BoardSpace.SpaceType.EMPTY.name());
        assertEquals("BLACK", BoardSpace.SpaceType.BLACK.name());
        assertEquals("WHITE", BoardSpace.SpaceType.WHITE.name());
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, BoardSpace.SpaceType.EMPTY.ordinal());
        assertEquals(1, BoardSpace.SpaceType.BLACK.ordinal());
        assertEquals(2, BoardSpace.SpaceType.WHITE.ordinal());
    }


}
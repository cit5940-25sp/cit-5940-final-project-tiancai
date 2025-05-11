package gamelogicTest;

import org.junit.jupiter.api.Test;
import othello.gamelogic.BoardSpace;

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
    public void testSetType() {
        //set type
        BoardSpace space = new BoardSpace(0, 0, BoardSpace.SpaceType.EMPTY);
        space.setType(BoardSpace.SpaceType.WHITE);
        assertEquals(BoardSpace.SpaceType.WHITE, space.getType());
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
    public void testEnumColorMapping() {
        assertNotNull(BoardSpace.SpaceType.BLACK.fill());
        assertNotNull(BoardSpace.SpaceType.WHITE.fill());
        assertNotNull(BoardSpace.SpaceType.EMPTY.fill());
    }


}
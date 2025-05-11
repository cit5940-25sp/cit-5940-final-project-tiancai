package othello.ai;
import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;

//for testing
public class DummyPlayer extends Player {
    public DummyPlayer(BoardSpace.SpaceType color) {
        setColor(color); // required so getColor() works
    }
}

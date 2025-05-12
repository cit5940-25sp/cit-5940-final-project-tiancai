package othello.gamelogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A serializable snapshot of the entire game state.
 */
public class GameMemento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final BoardSpace[][] board;
    private final List<BoardSpace> p1Spaces, p2Spaces;

    public GameMemento(BoardSpace[][] origBoard,
                       List<BoardSpace> origP1,
                       List<BoardSpace> origP2) {
        // deep‐copy the board
        this.board = BoardSpace.deepCopy(origBoard);

        // rebind owned‐spaces into the new copy
        this.p1Spaces = new ArrayList<>(origP1.size());
        for (BoardSpace bs : origP1) {
            this.p1Spaces.add(board[bs.getX()][bs.getY()]);
        }
        this.p2Spaces = new ArrayList<>(origP2.size());
        for (BoardSpace bs : origP2) {
            this.p2Spaces.add(board[bs.getX()][bs.getY()]);
        }
    }

    public BoardSpace[][] getBoard() {
        return BoardSpace.deepCopy(board);
    }
    public List<BoardSpace> getP1Spaces() {
        return new ArrayList<>(p1Spaces);
    }
    public List<BoardSpace> getP2Spaces() {
        return new ArrayList<>(p2Spaces);
    }
}
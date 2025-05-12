package othello.gamelogic;

import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a logical space on the Othello Board.
 * Keeps track of coordinates and the type of the current space.
 */
public class BoardSpace implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int x;
    private final int y;
    private SpaceType type;

    public BoardSpace(int x, int y, SpaceType type) {
        this.x = x;
        this.y = y;
        setType(type);
    }

    // Copy constructor
    public BoardSpace(BoardSpace other) {
        this.x = other.x;
        this.y = other.y;
        this.type = other.type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public SpaceType getType() { return type; }
    public void setType(SpaceType type) { this.type = type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardSpace)) return false;
        BoardSpace that = (BoardSpace) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Makes a deep copy of an entire 2D board.
     */
    public static BoardSpace[][] deepCopy(BoardSpace[][] board) {
        int n = board.length;
        BoardSpace[][] copy = new BoardSpace[n][];
        for (int i = 0; i < n; i++) {
            copy[i] = new BoardSpace[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                copy[i][j] = new BoardSpace(board[i][j]);
            }
        }
        return copy;
    }

    public enum SpaceType {
        EMPTY(Color.GRAY),
        BLACK(Color.BLACK),
        WHITE(Color.WHITE);

        private final Color fill;
        SpaceType(Color fill) { this.fill = fill; }
        public Color fill() { return fill; }
    }
}
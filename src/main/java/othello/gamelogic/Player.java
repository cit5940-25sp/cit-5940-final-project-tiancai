package othello.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Player class for representing a player within the game.
 * All types of Players have a color and a set of owned spaces on the game board.
 */
public abstract class Player {
    private final List<BoardSpace> playerOwnedSpaces = new ArrayList<>();
    public List<BoardSpace> getPlayerOwnedSpacesSpaces() {
        return playerOwnedSpaces;
    }

    private BoardSpace.SpaceType color;
    public void setColor(BoardSpace.SpaceType color) {
        this.color = color;
    }
    public BoardSpace.SpaceType getColor() {
        return color;
    }

    /**
     * PART 1
     * TODO: Implement this method
     * Gets the available moves for this player given a certain board state.
     * This method will find destinations, empty spaces that are valid moves,
     * and map them to a list of origins that can traverse to those destinations.
     * @param board the board that will be evaluated for possible moves for this player
     * @return a map with a destination BoardSpace mapped to a List of origin BoardSpaces.
     */
    public Map<BoardSpace, List<BoardSpace>> getAvailableMoves(BoardSpace[][] board) {
        Map<BoardSpace, List<BoardSpace>> result = new HashMap<>();
        BoardSpace.SpaceType oppColor = getOpponentColor();

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        for (BoardSpace origin : playerOwnedSpaces) {
            int x = origin.getX();
            int y = origin.getY();
            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];
                int nx = x + dx;
                int ny = y + dy;

                boolean hasOpponentBetween = false;

                //walk until find empty or out of boundary
                while (inBounds(nx, ny, board)) {
                    BoardSpace current = board[nx][ny];
                    if (current.getType() == oppColor) {
                        hasOpponentBetween = true;
                    } else if (current.getType() == BoardSpace.SpaceType.EMPTY && hasOpponentBetween) {
                        if (!result.containsKey(current)) {
                            result.put(current, new ArrayList<>());
                        }
                        result.get(current).add(origin);
                        break;
                    } else {
                        break;
                    }
                    nx += dx;
                    ny += dy;
                }
            }
        }
        for (Map.Entry<BoardSpace, List<BoardSpace>> entry : result.entrySet()) {
            BoardSpace destination = entry.getKey();
            List<BoardSpace> origins = entry.getValue();

            System.out.printf("Move at (%d, %d):\n", destination.getX(), destination.getY());
            for (BoardSpace origin : origins) {
                System.out.printf("    From origin (%d, %d)\n", origin.getX(), origin.getY());
            }
        }
        return result;
    }

    private boolean inBounds(int x, int y, BoardSpace[][] board) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }

    private BoardSpace.SpaceType getOpponentColor() {
        return getColor() == BoardSpace.SpaceType.BLACK
                ? BoardSpace.SpaceType.WHITE
                : BoardSpace.SpaceType.BLACK;
    }

}

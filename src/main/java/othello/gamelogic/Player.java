package othello.gamelogic;

import java.util.*;

import othello.gamelogic.BoardSpace.SpaceType;

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
        Map<BoardSpace, List<BoardSpace>> result = new LinkedHashMap<>();
        BoardSpace.SpaceType myColor   = getColor();
        SpaceType oppColor  = (myColor == SpaceType.BLACK
                ? SpaceType.WHITE
                : SpaceType.BLACK);

        // all eight directions
        int[][] dirs = {
                {-1,-1},{-1,0},{-1,1},
                {0,-1},       {0,1},
                {1,-1}, {1,0}, {1,1}
        };

        // scan the *entire* board for YOUR discs
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                BoardSpace origin = board[x][y];
                if (origin.getType() != myColor) continue;

                // from each of your discs, march out along each direction
                for (int[] d : dirs) {
                    int dx = d[0], dy = d[1];
                    int nx = x + dx, ny = y + dy;
                    boolean seenOpponent = false;

                    // walk until you hit edge, your own disc, or an empty
                    while (nx >= 0 && nx < board.length
                            && ny >= 0 && ny < board[nx].length) {

                        SpaceType t = board[nx][ny].getType();
                        if (t == oppColor) {
                            seenOpponent = true;
                        }
                        else if (t == SpaceType.EMPTY && seenOpponent) {
                            // found a valid move!
                            BoardSpace dest = board[nx][ny];
                            result.computeIfAbsent(dest, k -> new ArrayList<>())
                                    .add(origin);
                            break;
                        }
                        else {
                            // either your own disc with no opponent in between,
                            // or empty with no opponent in between — stop.
                            break;
                        }

                        nx += dx;
                        ny += dy;
                    }
                }
            }
        }

        for (Map.Entry<BoardSpace,List<BoardSpace>> e : result.entrySet()) {
            BoardSpace dest = e.getKey();
            List<BoardSpace> origins = e.getValue();
            origins.sort(Comparator.comparingInt(o ->
                    Math.abs(dest.getX() - o.getX()) +
                            Math.abs(dest.getY() - o.getY())
            ));
        }

        return result;
    }


    private BoardSpace.SpaceType getOpponentColor() {
        return getColor() == BoardSpace.SpaceType.BLACK
                ? BoardSpace.SpaceType.WHITE
                : BoardSpace.SpaceType.BLACK;
    }

}

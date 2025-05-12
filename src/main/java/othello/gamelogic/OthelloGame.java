package othello.gamelogic;

import java.util.*;

/**
 * Models a board of Othello.
 * Includes methods to get available moves and take spaces.
 */
public class OthelloGame {
    public static final int GAME_BOARD_SIZE = 8;

    private BoardSpace[][] board;
    private final Player playerOne;
    private final Player playerTwo;

    public OthelloGame(Player playerOne, Player playerTwo) {

        if (playerOne.getColor().equals(playerTwo.getColor())) {
            throw new IllegalArgumentException("Players have to have different colors");
        }
        if (playerOne.getColor() == null || playerTwo.getColor() == null) {
            throw new IllegalArgumentException("Players have to choose colors");
        }
        if (!playerOne.getColor().equals(BoardSpace.SpaceType.BLACK)) {
            throw new IllegalArgumentException("Player1 have to use Black");
        }
        if (!playerTwo.getColor().equals(BoardSpace.SpaceType.WHITE)) {
            throw new IllegalArgumentException("Player2 have to use White");
        }
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        initBoard();

    }

    public BoardSpace[][] getBoard() {
        return board;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return  playerTwo;
    }

    /**
     * Returns the available moves for a player.
     * Used by the GUI to get available moves each turn.
     * @param player player to get moves for
     * @return the map of available moves,that maps destination to list of origins
     */
    public Map<BoardSpace, List<BoardSpace>> getAvailableMoves(Player player) {
        return player.getAvailableMoves(board);
    }

    /**
     * Initializes the board at the start of the game with all EMPTY spaces.
     */
    public void initBoard() {
        board = new BoardSpace[GAME_BOARD_SIZE][GAME_BOARD_SIZE];
        int mid1 = GAME_BOARD_SIZE/2;       // 4
        int mid2 = mid1 - 1;                // 3

        for (int i = 0; i < GAME_BOARD_SIZE; i++) {
            for (int j = 0; j < GAME_BOARD_SIZE; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
                if ((i == mid2 && j == mid2) || (i == mid1 && j == mid1)) {
                    // top‑left and bottom‑right of center are WHITE
                    board[i][j].setType(BoardSpace.SpaceType.WHITE);
                    playerTwo.getPlayerOwnedSpacesSpaces().add(board[i][j]);
                } else if ((i == mid2 && j == mid1) || (i == mid1 && j == mid2)) {
                    // top‑right and bottom‑left of center are BLACK
                    board[i][j].setType(BoardSpace.SpaceType.BLACK);
                    playerOne.getPlayerOwnedSpacesSpaces().add(board[i][j]);
                }
            }
        }
    }

    /**
     * PART 1
     * TODO: Implement this method
     * Claims the specified space for the acting player.
     * Should also check if the space being taken is already owned by the acting player,
     * should not claim anything if acting player already owns space at (x,y)
     * @param actingPlayer the player that will claim the space at (x,y)
     * @param opponent the opposing player, will lose a space if their space is at (x,y)
     * @param x the x-coordinate of the space to claim
     * @param y the y-coordinate of the space to claim
     */
    public void takeSpace(Player actingPlayer, Player opponent, int x, int y) {
        BoardSpace target = board[x][y];
        BoardSpace.SpaceType currentType = target.getType();
        BoardSpace.SpaceType actingColor = actingPlayer.getColor();
        BoardSpace.SpaceType opponentColor = opponent.getColor();

        // space is actingPlayer, do nothing
        if (currentType == actingColor) {
            return;
        } else {
            actingPlayer.getPlayerOwnedSpacesSpaces().add(target);
        }
        // remove from opponent space if the current color is opponent color
        if (currentType == opponentColor) {
            opponent.getPlayerOwnedSpacesSpaces().remove(target);
        }

        // else, set to acting color,
        target.setType(actingColor);

    }

    /**
     * PART 1
     * TODO: Implement this method
     * Claims spaces from all origins that lead to a specified destination.
     * This is called when a player, human or computer, selects a valid destination.
     * @param actingPlayer the player that will claim spaces
     * @param opponent the opposing player, that may lose spaces
     * @param availableMoves map of the available moves, that maps destination to list of origins
     * @param selectedDestination the specific destination that a HUMAN player selected
     */
    public void takeSpaces(Player actingPlayer, Player opponent,
                           Map<BoardSpace, List<BoardSpace>> availableMoves,
                           BoardSpace selectedDestination) {
        if (!availableMoves.containsKey(selectedDestination)) {
            return;
        }

        // Step 1: take space of dest first
        takeSpace(actingPlayer, opponent,
                selectedDestination.getX(),
                selectedDestination.getY());

        // Step 2: go through from origin → destination, flip
        List<BoardSpace> origins = availableMoves.get(selectedDestination);


        for (BoardSpace origin : origins) {
            int x0 = origin.getX();
            int y0 = origin.getY();
            int x1 = selectedDestination.getX();
            int y1 = selectedDestination.getY();

            // direction
            int dx = Integer.compare(x1 - x0, 0);
            int dy = Integer.compare(y1 - y0, 0);
            //initialize step,
            int cx = x0 + dx;
            int cy = y0 + dy;

            while (cx != x1 || cy != y1) {
                takeSpace(actingPlayer, opponent, cx, cy);
                cx += dx;
                cy += dy;
            }
        }
    }

    /**
     * PART 2
     * TODO: Implement this method
     * Gets the computer decision for its turn.
     * Should call a method within the ComputerPlayer class that returns a BoardSpace using a specific strategy.
     * @param computer computer player that is deciding their move for their turn
     * @return the BoardSpace that was decided upon
     */
    public BoardSpace computerDecision(ComputerPlayer computer) {
        Player opponent = (computer == playerOne ? playerTwo : playerOne);
        // delegate to ComputerPlayer.chooseMove(...)
        return computer.chooseMove(board, opponent);
    }

    public GameMemento save() {
        return new GameMemento(board,
                playerOne.getPlayerOwnedSpacesSpaces(),
                playerTwo.getPlayerOwnedSpacesSpaces());
    }

    public void restore(GameMemento m) {
        this.board = m.getBoard();
        playerOne.getPlayerOwnedSpacesSpaces().clear();
        playerOne.getPlayerOwnedSpacesSpaces().addAll(m.getP1Spaces());
        playerTwo.getPlayerOwnedSpacesSpaces().clear();
        playerTwo.getPlayerOwnedSpacesSpaces().addAll(m.getP2Spaces());
    }

}

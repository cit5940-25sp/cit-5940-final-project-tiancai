package othello.ai;

import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;
import othello.gui.Constants;   // for BOARD_WEIGHTS

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Minimax implements AIStrategy {
    private static final int MAX_DEPTH = 4;  // tweak for performance vs. strength

    @Override
    public BoardSpace chooseMove(BoardSpace[][] board,
                                 Player self,
                                 Player opponent) {
        // Remember who the “maximizer” is
        BoardSpace.SpaceType rootColor = self.getColor();

        // All legal moves for 'self'
        Map<BoardSpace, List<BoardSpace>> avail = self.getAvailableMoves(board);
        if (avail.isEmpty()) return null;

        // Pick the move with the highest minimax score
        BoardSpace bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        for (BoardSpace move : avail.keySet()) {
            BoardSpace[][] copy = deepCopy(board);
            applyMove(copy, move, rootColor);

            int score = minimax(
                    copy,
                    opponent,       // now opponent’s turn
                    self,           // then back to you
                    MAX_DEPTH - 1,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    rootColor
            );

            if (score > bestScore) {
                bestScore = score;
                bestMove  = move;
            }
        }

        return bestMove;
    }

    /**
     * Minimax recursion with alpha-beta pruning.
     *
     * @param board      current board state
     * @param player     whose turn it is _now_
     * @param opponent   the other player
     * @param depth      remaining depth
     * @param alpha      best already explored option along path to maximizer
     * @param beta       best already explored option along path to minimizer
     * @param rootColor  which color is the original maximizer
     * @return heuristic score
     */
    private int minimax(BoardSpace[][] board,
                        Player player,
                        Player opponent,
                        int depth,
                        int alpha,
                        int beta,
                        BoardSpace.SpaceType rootColor) {
        // Base case: depth exhausted or terminal position
        if (depth == 0 || gameOver(board, player, opponent)) {
            return evaluate(board, player.getColor());
        }

        // Generate moves
        Map<BoardSpace, List<BoardSpace>> avail = player.getAvailableMoves(board);
        if (avail.isEmpty()) {
            // No moves → pass turn
            return minimax(board, opponent, player, depth - 1, alpha, beta, rootColor);
        }

        // Are we maximizing or minimizing?
        boolean isMaximizer = (player.getColor() == rootColor);

        if (isMaximizer) {
            int maxEval = Integer.MIN_VALUE;
            for (BoardSpace move : avail.keySet()) {
                BoardSpace[][] copy = deepCopy(board);
                applyMove(copy, move, player.getColor());
                int eval = minimax(copy, opponent, player, depth - 1,
                        alpha, beta, rootColor);
                maxEval = Math.max(maxEval, eval);
                alpha   = Math.max(alpha, eval);
                if (beta <= alpha) break;  // α–β cutoff
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (BoardSpace move : avail.keySet()) {
                BoardSpace[][] copy = deepCopy(board);
                applyMove(copy, move, player.getColor());
                int eval = minimax(copy, opponent, player, depth - 1,
                        alpha, beta, rootColor);
                minEval = Math.min(minEval, eval);
                beta    = Math.min(beta, eval);
                if (beta <= alpha) break;  // α–β cutoff
            }
            return minEval;
        }
    }

    /**
     * Terminal check:
     *   1) no EMPTY squares remain, OR
     *   2) neither player can move.
     */
    private boolean gameOver(BoardSpace[][] board,
                             Player p1,
                             Player p2) {
        // 1) any empty?
        boolean anyEmpty = false;
        for (BoardSpace[] row : board) {
            for (BoardSpace s : row) {
                if (s.getType() == BoardSpace.SpaceType.EMPTY) {
                    anyEmpty = true;
                    break;
                }
            }
            if (anyEmpty) break;
        }

        // 2) neither can move?
        boolean noMoves =
                p1.getAvailableMoves(board).isEmpty() &&
                        p2.getAvailableMoves(board).isEmpty();

        return !anyEmpty || noMoves;
    }

    /**
     * Heuristic: weighted disc count for `me` minus opponent.
     */
    private int evaluate(BoardSpace[][] board,
                         BoardSpace.SpaceType me) {
        int[][] w = Constants.BOARD_WEIGHTS;
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                BoardSpace.SpaceType t = board[i][j].getType();
                if (t == me) {
                    score += w[i][j];
                } else if (t != BoardSpace.SpaceType.EMPTY) {
                    score -= w[i][j];
                }
            }
        }
        return score;
    }

    /** Deep‐copy the board and its spaces so simulations are isolated. */
    private BoardSpace[][] deepCopy(BoardSpace[][] board) {
        int n = board.length;
        BoardSpace[][] copy = new BoardSpace[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < board[i].length; j++) {
                copy[i][j] = new BoardSpace(board[i][j]);
            }
        }
        return copy;
    }

    /**
     * Place a piece and flip any outflanked discs.
     */
    private void applyMove(BoardSpace[][] board,
                           BoardSpace move,
                           BoardSpace.SpaceType clr) {
        // 1) Place the new disc
        board[move.getX()][move.getY()].setType(clr);

        // 2) Flip outflanked discs in all 8 directions
        BoardSpace.SpaceType opp =
                (clr == BoardSpace.SpaceType.BLACK)
                        ? BoardSpace.SpaceType.WHITE
                        : BoardSpace.SpaceType.BLACK;

        int[][] dirs = {
                {-1,  0}, {-1, +1}, { 0, +1}, {+1, +1},
                {+1,  0}, {+1, -1}, { 0, -1}, {-1, -1}
        };

        for (int[] d : dirs) {
            int x = move.getX() + d[0], y = move.getY() + d[1];
            List<BoardSpace> candidates = new ArrayList<>();

            // collect opponent discs
            while (x >= 0 && x < board.length
                    && y >= 0 && y < board[x].length
                    && board[x][y].getType() == opp) {
                candidates.add(board[x][y]);
                x += d[0];
                y += d[1];
            }

            // if “capped” by a friend, flip them
            if (x >= 0 && x < board.length
                    && y >= 0 && y < board[x].length
                    && board[x][y].getType() == clr) {
                for (BoardSpace bs : candidates) {
                    bs.setType(clr);
                }
            }
        }
    }
}
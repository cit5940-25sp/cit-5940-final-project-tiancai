package othello.ai;

import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;

public interface AIStrategy {
    BoardSpace chooseMove(BoardSpace[][] board, Player self, Player opponent);
}
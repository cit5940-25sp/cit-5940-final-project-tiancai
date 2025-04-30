package othello.gamelogic;

public interface AIStrategy {
    BoardSpace chooseMove(BoardSpace[][] board, Player self, Player opponent);
}

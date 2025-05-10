package AITest;

import othello.ai.NeuralNetworkStrategy;
import othello.gamelogic.BoardSpace;
import othello.gamelogic.ComputerPlayer;
import othello.gamelogic.HumanPlayer;
import othello.gamelogic.Player;

public class NeuralNetworkStrategyTest {
    public static void main(String[] args) throws Exception {
        // 创建棋盘
        BoardSpace[][] board = new BoardSpace[8][8];
        Player black = new HumanPlayer();
        black.setColor(BoardSpace.SpaceType.BLACK);
        Player white = new ComputerPlayer("cnn");
        white.setColor(BoardSpace.SpaceType.WHITE);

        // 初始化棋盘（默认 EMPTY）
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new BoardSpace(i, j, BoardSpace.SpaceType.EMPTY);
            }
        }

        // 设置初始四个子
        board[3][3].setType(BoardSpace.SpaceType.WHITE);
        board[3][4].setType(BoardSpace.SpaceType.BLACK);
        board[4][3].setType(BoardSpace.SpaceType.BLACK);
        board[4][4].setType(BoardSpace.SpaceType.WHITE);

        // 创建模型策略对象
        NeuralNetworkStrategy strategy = new NeuralNetworkStrategy();
        BoardSpace move = strategy.chooseMove(board, black, white);

        // 输出模型建议
        if (move == null) {
            System.out.println("Model：PASS");
        } else {
            System.out.printf("Suggest：(%d, %d)\n", move.getX(), move.getY());
        }
    }
}
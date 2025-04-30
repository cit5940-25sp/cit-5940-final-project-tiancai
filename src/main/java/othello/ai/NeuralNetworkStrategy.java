package othello.ai;

import ai.onnxruntime.*;
import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;
import othello.ai.AIStrategy;

import java.util.Map;
public class NeuralNetworkStrategy implements AIStrategy {

    private static OrtEnvironment env;
    private static OrtSession session;

    static {
        try {
            env = OrtEnvironment.getEnvironment();
            session = env.createSession("Othello.onnx", new OrtSession.SessionOptions());
        } catch (OrtException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BoardSpace chooseMove(BoardSpace[][] board, Player self, Player opponent) {
        try {
            float[][][][] input = new float[1][1][board.length][board[0].length];

            // input
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].getType() == self.getColor()) {
                        input[0][0][i][j] = 1.0f;
                    } else if (board[i][j].getType() == opponent.getColor()) {
                        input[0][0][i][j] = -1.0f;
                    } else {
                        input[0][0][i][j] = 0.0f;
                    }
                }
            }

            // predict
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, input);
            Map<String, OnnxTensor> inputs = Map.of("board_in", inputTensor);
            OrtSession.Result result = session.run(inputs);

            float[][] bestMoveScores = (float[][]) result.get(0).getValue(); // [1][65]

            // optimized
            int bestIndex = 0;
            float maxScore = bestMoveScores[0][0];
            for (int i = 1; i < 65; i++) {
                if (bestMoveScores[0][i] > maxScore) {
                    maxScore = bestMoveScores[0][i];
                    bestIndex = i;
                }
            }

            // if pass pass
            if (bestIndex == 64) return null;

            int row = bestIndex / 8;
            int col = bestIndex % 8;
            return board[row][col];

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

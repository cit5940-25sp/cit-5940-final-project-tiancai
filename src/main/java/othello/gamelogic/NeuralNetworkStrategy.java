package othello.gamelogic;

public class NeuralNetworkStrategy implements AIStrategy {



    public BoardSpace chooseMove(BoardSpace[][] board, Player self, Player opponent) {
        // 1. get input：float[8][8]
        float[][] boardInput = new float[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardSpace s = board[i][j];
                if (s.getType() == self.getColor()) {
                    boardInput[i][j] = 1f;
                }
            }
        }

        try {
            float[] probs = predictor.predictMoveProbabilities(boardInput);
            Map<BoardSpace, List<BoardSpace>> availableMoves = self.getAvailableMoves(board);

            BoardSpace best = null;
            float bestProb = -1f;

            for (BoardSpace move : availableMoves.keySet()) {
                int index = move.getX() * 8 + move.getY();
                if (probs[index] > bestProb) {
                    bestProb = probs[index];
                    best = move;
                }
            }

            return best;
        } catch (OrtException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() throws OrtException {
        predictor.close();
    }
}

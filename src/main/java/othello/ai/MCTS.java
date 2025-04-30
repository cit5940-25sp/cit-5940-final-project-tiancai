package othello.ai;

import othello.gamelogic.BoardSpace;
import othello.gamelogic.Player;

import java.util.*;

public class MCTS implements AIStrategy {
    private static final int MAX_ITERATIONS = 1000;
    private static final double EXPLORATION_PARAM = Math.sqrt(2);
    private final Random random = new Random();

    private class Node {
        BoardSpace[][] board;
        Node parent;
        List<Node> children = new ArrayList<>();
        int wins = 0, visits = 0;
        BoardSpace.SpaceType playerColor;
        BoardSpace move;

        Node(BoardSpace[][] board, Node parent, BoardSpace move, BoardSpace.SpaceType playerColor) {
            this.board = deepCopy(board);
            this.parent = parent;
            this.move = move;
            this.playerColor = playerColor;
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        void expand(List<BoardSpace> validMoves, BoardSpace.SpaceType nextPlayer) {
            for (BoardSpace move : validMoves) {
                BoardSpace[][] next = deepCopy(board);
                next[move.getX()][move.getY()].setType(nextPlayer);
                children.add(new Node(next, this, move, nextPlayer));
            }
        }

        Node bestChild(double c) {
            int total = this.visits;
            return children.stream()
                    .max(Comparator.comparing(n ->
                            n.visits == 0 ? Double.POSITIVE_INFINITY :
                                    (double) n.wins / n.visits + c * Math.sqrt(Math.log(total) / n.visits)))
                    .orElseThrow();
        }
    }

    @Override
    public BoardSpace chooseMove(BoardSpace[][] board, Player self, Player opponent) {
        BoardSpace.SpaceType playerColor = self.getColor();
        Node root = new Node(board, null, null, playerColor);

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Node node = select(root);
            if (node.visits == 0) {
                boolean win = simulate(node.board, playerColor);
                backpropagate(node, win, playerColor);
            } else {
                List<BoardSpace> moves = getValidDestinations(node.board, opposite(node.playerColor));
                if (moves.isEmpty()) continue;
                node.expand(moves, opposite(node.playerColor));
                Node randomChild = node.children.get(random.nextInt(node.children.size()));
                boolean win = simulate(randomChild.board, playerColor);
                backpropagate(randomChild, win, playerColor);
            }
        }

        Node best = root.children.stream()
                .max(Comparator.comparingInt(n -> n.visits))
                .orElse(null);
        return best == null ? null : best.move;
    }

    private Node select(Node node) {
        while (!node.isLeaf() && !node.children.isEmpty()) {
            node = node.bestChild(EXPLORATION_PARAM);
        }
        return node;
    }

    private boolean simulate(BoardSpace[][] board, BoardSpace.SpaceType rootPlayer) {
        BoardSpace.SpaceType current = rootPlayer;
        int passes = 0;

        while (passes < 2) {
            List<BoardSpace> moves = getValidDestinations(board, current);
            if (moves.isEmpty()) {
                passes++;
                current = opposite(current);
                continue;
            }
            passes = 0;
            BoardSpace move = moves.get(random.nextInt(moves.size()));
            board[move.getX()][move.getY()].setType(current);
            current = opposite(current);
        }

        int rootCount = countDiscs(board, rootPlayer);
        int oppCount = countDiscs(board, opposite(rootPlayer));
        return rootCount > oppCount;
    }

    private void backpropagate(Node node, boolean win, BoardSpace.SpaceType rootPlayer) {
        while (node != null) {
            node.visits++;
            if (node.playerColor == rootPlayer && win) {
                node.wins++;
            }
            node = node.parent;
        }
    }

    private BoardSpace.SpaceType opposite(BoardSpace.SpaceType color) {
        return (color == BoardSpace.SpaceType.BLACK) ? BoardSpace.SpaceType.WHITE : BoardSpace.SpaceType.BLACK;
    }

    private int countDiscs(BoardSpace[][] board, BoardSpace.SpaceType color) {
        int count = 0;
        for (BoardSpace[] row : board) {
            for (BoardSpace space : row) {
                if (space.getType() == color) count++;
            }
        }
        return count;
    }

    private BoardSpace[][] deepCopy(BoardSpace[][] board) {
        BoardSpace[][] copy = new BoardSpace[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                copy[i][j] = new BoardSpace(board[i][j]);
            }
        }
        return copy;
    }

    // Stub: replace with actual move logic later
    private List<BoardSpace> getValidDestinations(BoardSpace[][] board, BoardSpace.SpaceType color) {
        List<BoardSpace> list = new ArrayList<>();
        for (BoardSpace[] row : board) {
            for (BoardSpace space : row) {
                if (space.getType() == BoardSpace.SpaceType.EMPTY) {
                    list.add(space);
                }
            }
        }
        return list;
    }
}
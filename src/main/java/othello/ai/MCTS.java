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
        BoardSpace.SpaceType me = self.getColor();
        Node root = new Node(board, null, null, me);

        // 1) Expand root with your moves immediately
        List<BoardSpace> myMoves = getValidDestinations(board, me);
        if (myMoves.isEmpty()) return null;           // no moves at all
        root.expand(myMoves, me);

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // 2) Selection: find a leaf to explore
            Node leaf = select(root);

            // 3) Expansion: if leaf belongs to me, expand my moves; else expand opponent's
            BoardSpace.SpaceType turn = leaf.playerColor;
            List<BoardSpace> moves = getValidDestinations(leaf.board, turn);
            if (!moves.isEmpty()) {
                leaf.expand(moves, turn);
                leaf = leaf.children.get(random.nextInt(leaf.children.size()));
            }

            // 4) Simulation
            boolean win = simulate(leaf.board, me);

            // 5) Backpropagation
            backpropagate(leaf, win, me);
        }

        // 6) Return the child of root with the highest visit count
        Node bestChild = root.children.stream()
                .max(Comparator.comparingInt(n -> n.visits))
                .orElseThrow();
        return bestChild.move;
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

    //for testing
    private List<BoardSpace> getValidDestinations(BoardSpace[][] board, BoardSpace.SpaceType color) {
        DummyPlayer dummy = new DummyPlayer(color);

        // Populate playerOwnedSpaces from the board
        for (BoardSpace[] row : board) {
            for (BoardSpace space : row) {
                if (space.getType() == color) {
                    dummy.getPlayerOwnedSpacesSpaces().add(space);
                }
            }
        }

        Map<BoardSpace, List<BoardSpace>> moveMap = dummy.getAvailableMoves(board);
        return new ArrayList<>(moveMap.keySet());
    }
}
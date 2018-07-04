package game;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import static game.Board.*;
import static game.PieceColor.*;

/**
 * An AI player, can automatically compute the best piece in
 * the current board.
 * @author Victor
 */
public class AI extends Player {

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    /**
     * Returns the best piece from an AI. If current board is an empty board,
     * it always returns the central point on the board, if it only has a piece
     * on the board, randomly returns an opposite piece near that one. For other
     * cases, the piece depends on the SCORE function or if game is over.
     */
    @Override
    Piece next() {
        Board b = new Board(board());
        if (b.numberOfPieces() == 0) {
            int cent = SIDE / 2 + 1;
            return Piece.create(BLACK, cent, cent);
        } else if (b.numberOfPieces() == 1) {
            Random ran = new Random();
            Set<Piece> options = b.getPotentialPieces(false);
            int random_piece = ran.nextInt(options.size());
            return (Piece) options.toArray()[random_piece];
        } else {
            if (myColor() == BLACK) {
                findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, true, true);
            } else {
                findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, false, true);
            }
            return _lastPiece;
        }
    }

    /**
     * Try to predict the best piece to play.
     * @param board Current working board.
     * @param depth Used to compute depth of inference.
     * @param alpha Best score so far.
     * @param beta Worst score so far.
     * @param MaximizingPlayer True iff it is a maximizing player.
     * @param last Last piece returned by function.
     * @return A heuristic score or winning score.
     */
    private int findPiece(Board board, int depth, int alpha, int beta, boolean MaximizingPlayer, boolean last) {
        Piece bestPiece = null;
        if (board.gameOver(true)) {
            return board.whoseMove() == WHITE ? WINNING_VALUE : -WINNING_VALUE;
        }
        if (depth == 0) {
            return score(board);
        }
        if (MaximizingPlayer) {
            int response;
            for (Piece p : board.getPotentialPieces(false)) {
                Board temp = new Board(board);
                temp.play(p);
                response = findPiece(temp, depth - 1, alpha, beta, false, false);
                if (response > alpha) {
                    alpha = response;
                    bestPiece = p;
                }
                if (alpha == INFINITY - 1 || beta <= alpha) {
                    break;
                }
            }
            if (last) {
                _lastPiece = bestPiece;
            }
            return alpha;
        } else {
            int response;
            for (Piece p : board.getPotentialPieces(false)) {
                Board temp = new Board(board);
                temp.play(p);
                response = findPiece(temp, depth - 1, alpha, beta, true, false);
                if (response < beta) {
                    beta = response;
                    bestPiece = p;
                }
                if (beta == -INFINITY + 1 || beta <= alpha) {
                    break;
                }
            }
            if (last) {
                _lastPiece = bestPiece;
            }
            return beta;
        }
    }

    /**
     * Used to count heuristic score on the board to determine which piece
     * is best.
     */
    private int score(Board board) {
        int blackscore = 0, whitescore = 0;
        for (int i = 0; i < MAX_INDEX; i++) {
            if (board.get(i) != EMPTY) {
                PieceColor[][] colors = board.count(i);
                for (int j = 0; j < 4; j++) {
                    if (board.get(i) == BLACK) {
                        blackscore += single_score(colors[j]);
                    } else {
                        whitescore += single_score(colors[j]);
                    }
                }
            }
        }
        return blackscore - whitescore;
    }

    /** Helper function used to count score of a single array. */
    private int single_score(PieceColor[] array) {
        PieceColor Mycolor = array[HALF];
        boolean block1 = false, block2 = false;
        int half1 = 0, half2 = 0;
        for (int i = 1; i <= HALF; i++) {
            if (!block1 && array[HALF - i] == Mycolor) {
                half1++;
            } else if (array[HALF - i] == Mycolor.opposite()) {
                block1 = true;
            }
            if (!block2 && array[HALF + i] == Mycolor) {
                half2++;
            } else if (array[HALF + i] == Mycolor.opposite()) {
                block2 = true;
            }
            if (half1 + half2 > HALF || (block1 && block2)) {
                break;
            }
        }
        int number = half1 + half2 > HALF ? HALF :half1 + half2;
        if (block1 || block2) {
            return _score.get(number + HALF + 1);
        } else {
            return _score.get(number);
        }
    }

    /** Score for different cases. */
    private final HashMap<Integer, Integer> _score = new HashMap<>(); {
        _score.put(0, 10);       /* Live one. */
        _score.put(1, 100);      /* Live two. */
        _score.put(2, 1000);     /* Live three. */
        _score.put(3, 10000);    /* Live four. */
        _score.put(4, 100000);   /* Live five. */
        _score.put(5, 1);        /* Dead one. */
        _score.put(6, 10);       /* Dead two. */
        _score.put(7, 100);      /* Dead three. */
        _score.put(8, 1000);     /* Dead four. */
        _score.put(9, 100000);   /* Dead five. */
    }

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;

    /** Half index of two quintuples, including myself. */
    private static final int HALF = 4;

    /** A magnitude greater than a normal value. */
    private static final int INFINITY = Integer.MAX_VALUE;

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;

    /** The piece found by the last call to findMove method. */
    private Piece _lastPiece;
}

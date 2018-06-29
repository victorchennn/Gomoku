package game;

import java.util.Random;
import java.util.Set;

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
        if (b.numberOfPieces() == 1) {
            Random ran = new Random();
            Set<Piece> options = b.getPotentialPieces(false);
            int random_piece = ran.nextInt(options.size());
            return (Piece) options.toArray()[random_piece];
        }
        if (myColor() == WHITE) {
            findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, true);
        } else {
            findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, false);
        }
        return b.whoseMove() == WHITE ? _lastWhite : _lastBlack;
    }

    /**
     * Try to predict the best piece to play.
     * @param board Current working board.
     * @param depth Used to compute depth of inference.
     * @param alpha Best score so far.
     * @param beta Worst score so far.
     * @param MaxmizingPlayer True iff it is a maxmizing player.
     * @return A heuristic score or winning score.
     */
    private int findPiece(Board board, int depth, int alpha, int beta, boolean MaxmizingPlayer) {
        if (board.gameOver()) {
            return board.whoseMove().opposite() == WHITE ? WINNING_VALUE : -WINNING_VALUE;
        }
        if (depth == 0) {
            return score(board);
        }
        if (MaxmizingPlayer) {
            int v = -INFINITY;
            int response;
            for (Piece p : board.getPotentialPieces(false)) {
                Board temp = new Board(board);
                temp.play(p);
                response = findPiece(temp, depth - 1, alpha, beta, false);
                if (response > v) {
                    v = response;
                    _lastWhite = p;
                }
                alpha = Math.max(alpha, v);
                if (v == WINNING_VALUE || beta <= alpha) {
                    break;
                }
            }
            return v;
        } else {
            int v = INFINITY;
            int response;
            for (Piece p : board.getPotentialPieces(false)) {
                Board temp = new Board(board);
                temp.play(p);
                response = findPiece(temp, depth - 1, alpha, beta, true);
                if (response < v) {
                    v = response;
                    _lastBlack = p;
                }
                beta = Math.min(beta, v);
                if (v == -WINNING_VALUE || beta <= alpha) {
                    break;
                }
            }
            return v;
        }
    }

    /**
     * Used to count heuristic score on the board to determine which piece
     * is best.
     */
    private int score(Board board) {
        int me = 0, op = 0;
        int[] my_score = board.chainOfPieces(board.whoseMove());
        int[] op_score = board.chainOfPieces(board.whoseMove().opposite());
        for (int i = 1; i <= 5; i++) {
            me += my_score[i - 1] * i;
            op += op_score[i - 1] * i;
            me -= op_score[i - 1] * i;
            op -= my_score[i - 1] * i;
        }
        return me - op;
    }

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;

    /** A magnitude greater than a normal value. */
    private static final int INFINITY = Integer.MAX_VALUE;

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;

    /** The piece found by the last call to findMove method. */
    private Piece _lastWhite;

    /** The piece found by the last call to findMove method. */
    private Piece _lastBlack;
}

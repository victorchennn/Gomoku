package game;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static game.PieceColor.*;
import static game.Board.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UnitTest {

    @Test
    public void test_pattern() {
        String s = "  auto white black  ";
        String s1 = s.trim();
        assertNotEquals(s1, s);
        Pattern p = Pattern.compile("(?i)auto\\s+(white|black)\\s+(white|black)");
        Matcher mat = p.matcher(s1);
        assertEquals(mat.matches(), true);
        assertEquals(2, mat.groupCount());
        assertEquals("white", mat.group(1));
        assertEquals("black", mat.group(2));
    }

    @Test
    public void test_piecePattern() {
        Pattern p = Pattern.compile("([1-9]|(1[0-5])),([1-9]|(1[0-5]))");
        String s1 = "12,10";
        String s2 = "1,4";
        String s3 = "112";
        String s4 = "5, 6";
        Matcher mat1 = p.matcher(s1);
        Matcher mat2 = p.matcher(s2);
        Matcher mat3 = p.matcher(s3);
        Matcher mat4 = p.matcher(s4);
        assertEquals(mat1.matches(), true);
        assertEquals(mat2.matches(), true);
        assertEquals(mat3.matches(), false);
        assertEquals(mat4.matches(), false);
    }

    @Test
    public void board_SetAndGet() {
        Board b = new Board();
        b.set(6, 6, BLACK);
        b.set(48, WHITE);
        assertEquals(b.get(6,6), BLACK);
        assertEquals(b.get(48), WHITE);
        b.set(15,15, WHITE);
        b.set(1,15, BLACK);
        b.set(15,1, BLACK);
        b.set(1,1, WHITE);
        assertEquals(b.get(1,1), WHITE);
        assertEquals(b.get(1,15), BLACK);
        assertEquals(b.get(15,1), BLACK);
        assertEquals(b.get(15,15), WHITE);
        for (int i = 1, x = 1, y = 15; i < SIDE; i++) {
            b.set(i, i + 1, BLACK);
            b.set(i + 1, i, WHITE);
            assertEquals(b.get(y), WHITE);
            assertEquals(b.get(x), BLACK);
            x += SIDE + 1;
            y += SIDE + 1;
        }
    }

    @Test
    public void board_SetAndGet2() {
        Board b1 = new Board();
        Board b2 = new Board();
        Board b3 = new Board();
        Board b4 = new Board();
        for (int i = 0, r = 1; i < SIDE * SIDE && r <= SIDE; r++) {
            for (int c = 1; c <= SIDE; c++, i++) {
                b1.set(i, BLACK);
                assertEquals(b1.get(r, c), BLACK);
                b2.set(r, c, WHITE);
                assertEquals(b2.get(i), WHITE);
                b3.set(i, WHITE);
                assertEquals(b3.get(i), WHITE);
                b4.set(r, c, BLACK);
                assertEquals(b4.get(r, c), BLACK);
            }
        }
    }

    @Test
    public void test_Undo() {
        Board b = new Board();
        b.play(Piece.create(BLACK, 7, 12));
        b.play(Piece.create(WHITE, 10, 6));
        assertEquals(BLACK, b.get(index(7, 12)));
        assertEquals(WHITE, b.get(index(10, 6)));
        b.undo();
        assertEquals(WHITE, b.whoseMove());
        assertEquals(EMPTY, b.get(10, 6));
        assertEquals(EMPTY, b.get(6, 10));
        assertEquals(BLACK, b.get(7, 12));
        b.undo();
        assertEquals(BLACK, b.whoseMove());
        assertEquals(EMPTY, b.get(7, 12));
        assertEquals(0, b.log().size());
    }

    @Test
    public void test_GameOver1() {
        String s = "--------------- --------------- --------------- " +
                "----------bb-w- ----------bw--- -----------w--- " +
                "-----------w--- ------bb---w--- -----------w--- " +
                "--------------- ------b-------- --------------- " +
                "--------------- --------------- ---------------";
        Board b = new Board();
        b.setPieces(s, WHITE);
        assertEquals(b.gameOver(false), true); // Up
        b.set(6, 12, EMPTY);
        assertEquals(b.gameOver(false), false);
        b.set(11, 7, EMPTY);
        assertEquals(b.gameOver(false), false);
        b.set(6,10, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(7, 9, BLACK);
        assertEquals(b.gameOver(false), true); // UpLeft
        b.set(5, 11, EMPTY);
        assertEquals(b.gameOver(false), false);
        b.set(4, 10, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(4, 9, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(4, 8, BLACK);
        assertEquals(b.gameOver(false), true); // Right
        b.set(4, 8, WHITE);
        assertEquals(b.gameOver(false), false);
        b.set(6, 10, WHITE);
        assertEquals(b.gameOver(false), false);
        b.set(5, 9, WHITE);
        assertEquals(b.gameOver(false), false);
        b.set(7, 11, WHITE);
        assertEquals(b.gameOver(false), true); // UpRight
    }

    @Test
    public void test_GameOver2() {
        Board b = new Board();
        b.set(3, 15, BLACK);
        b.set(4, 1, BLACK);
        b.set(4, 2, BLACK);
        b.set(4, 3, BLACK);
        b.set(4, 4, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(3, 14, BLACK);
        b.set(3, 13, BLACK);
        b.set(3, 12, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(4, 13, BLACK);
        b.set(5, 14, BLACK);
        b.set(6, 15, BLACK);
        b.set(8 , 1, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(7 , 1, BLACK);
        b.set(8, 2, BLACK);
        b.set(9, 3, BLACK);
        b.set(10, 4, BLACK);
        b.set(5 , 15, BLACK);
        assertEquals(b.gameOver(false), false);
        b.set(3, 5, BLACK);
        b.set(2, 6, BLACK);
        b.set(15, 7, BLACK);
        assertEquals(b.gameOver(false), false);
    }

    @Test
    public void test_GameOver3() {
        Board b = new Board();
        b.play(Piece.create(BLACK, 7, 7));
        assertEquals(b.gameOver(true), false);
        b.play(Piece.create(BLACK, 7, 8));
        assertEquals(b.gameOver(true), false);
        b.play(Piece.create(BLACK, 7, 9));
        assertEquals(b.gameOver(true), false);
        b.play(Piece.create(BLACK, 7, 10));
        assertEquals(b.gameOver(true), false);
        b.play(Piece.create(BLACK, 7, 11));
        assertEquals(b.gameOver(true), true);
        System.out.println(b);
    }

    @Test
    public void test_Copy() {
        String s = "--------------- --------------- --------------- " +
                "----------bb-w- ----------bw--- -----------w--- " +
                "-----------w--- ------bb---w--- -----------w--- " +
                "--------------- ------b-------- --------------- " +
                "--------------- --------------- ---------------";
        Board b = new Board();
        b.setPieces(s, WHITE);
        Board copy_b = new Board(b);
        b.set(1, 1, WHITE);
        assertEquals(b.get(1, 1), WHITE);
        assertEquals(copy_b.get(1, 1), EMPTY);
        copy_b.set(15, 15, BLACK);
        assertEquals(copy_b.get(15, 15), BLACK);
        assertEquals(b.get(15, 15), EMPTY);
    }

    @Test
    public void test_AdjacentIndex() {
        Board b = new Board();
        assertEquals(b.getAdjacentIndex(0, true).size(), 8);
        assertEquals(b.getAdjacentIndex(14, true).size(), 8);
        assertEquals(b.getAdjacentIndex(210, true).size(), 8);
        assertEquals(b.getAdjacentIndex(224, true).size(), 8);
        assertEquals(b.getAdjacentIndex(1, true).size(), 11);
        assertEquals(b.getAdjacentIndex(13, true).size(), 11);
        assertEquals(b.getAdjacentIndex(209, true).size(), 11);
        assertEquals(b.getAdjacentIndex(223, true).size(), 11);
        assertEquals(b.getAdjacentIndex(15, true).size(), 11);
        assertEquals(b.getAdjacentIndex(29, true).size(), 11);
        assertEquals(b.getAdjacentIndex(195, true).size(), 11);
        assertEquals(b.getAdjacentIndex(211, true).size(), 11);
        assertEquals(b.getAdjacentIndex(7, true).size(), 14);
        assertEquals(b.getAdjacentIndex(105, true).size(), 14);
        assertEquals(b.getAdjacentIndex(119, true).size(), 14);
        assertEquals(b.getAdjacentIndex(216, true).size(), 14);
        assertEquals(b.getAdjacentIndex(16, true).size(), 15);
        assertEquals(b.getAdjacentIndex(28, true).size(), 15);
        assertEquals(b.getAdjacentIndex(196, true).size(), 15);
        assertEquals(b.getAdjacentIndex(208, true).size(), 15);
        assertEquals(b.getAdjacentIndex(22, true).size(), 19);
        assertEquals(b.getAdjacentIndex(106, true).size(), 19);
        assertEquals(b.getAdjacentIndex(118, true).size(), 19);
        assertEquals(b.getAdjacentIndex(201, true).size(), 19);
        assertEquals(b.getAdjacentIndex(112, true).size(), 24);
    }

    @Test
    public void test_AvailablePieces() {
        Board b = new Board();
        assertEquals(0, b.getPotentialPieces(true).size());
        for (Piece p : b.getPotentialPieces(true)) {
            assertEquals(8, p.col());
            assertEquals(8, p.row());
            assertEquals(BLACK, p.color());
        }
        b.set(8, 8, BLACK);
        assertEquals(24, b.getPotentialPieces(true).size());
        for (Piece p : b.getPotentialPieces(true)) {
            assertEquals(p.col() < 11 && p.col() > 5, true);
            assertEquals(p.row() < 11 && p.row() > 5, true);
        }
        b.set(8, 8, EMPTY);
        b.set(1, 2, WHITE);
        assertEquals(11, b.getPotentialPieces(true).size());
        for (Piece p : b.getPotentialPieces(true)) {
            assertEquals(p.col() < 5 && p.col() > 0, true);
            assertEquals(p.row() < 4 && p.row() > 0, true);
        }
        b.set(1, 2, EMPTY);
        b.set(10, 14, BLACK);
        assertEquals(19, b.getPotentialPieces(true).size());
        for (Piece p : b.getPotentialPieces(true)) {
            assertEquals(p.col() < 16 && p.col() > 11, true);
            assertEquals(p.row() < 13 && p.row() > 7, true);
        }
    }

    @Test
    public void test_NonAdvanceIndexAndPieces() {
        Board b = new Board();
        assertEquals(b.getAdjacentIndex(112, false).size(), 8);
        assertEquals(b.getAdjacentIndex(0, false).size(), 3);
        assertEquals(b.getAdjacentIndex(224, false).size(), 3);
        assertEquals(b.getAdjacentIndex(13, false).size(), 5);
        assertEquals(b.getAdjacentIndex(195, false).size(), 5);
        b.set(4, 6, BLACK);
        assertEquals(b.getPotentialPieces(false).size(), 8);
        for (Piece p : b.getPotentialPieces(false)) {
            assertEquals(p.col() < 8 && p.col() > 4, true);
            assertEquals(p.row() < 6 && p.row() > 2, true);
        }
        b.set(4, 6, EMPTY);
        b.set(15, 8, BLACK);
        assertEquals(b.getPotentialPieces(false).size(), 5);
        for (Piece p : b.getPotentialPieces(false)) {
            assertEquals(p.col() < 10 && p.col() > 6, true);
            assertEquals(p.row() < 16 && p.row() > 13, true);
        }
    }

    @Test
    public void test_Count() {
        String s = "--------------- --------------- --------------- " +
                "-----bbb------- ------bb------- -----bb-b------ " +
                "------b-b--b--- ---b--bb-b-b--- ------b-------- " +
                "--------------- ------b-------- --------------- " +
                "--------------- --------------- ---------------";
        Board b = new Board();
        b.setPieces(s, BLACK);
        Piece p = Piece.create(WHITE, 4,8);
        b.set(index(p.row(), p.col()), p.color());
        System.out.println(b);
        for (PieceColor[] colors : b.count(index(5, 7))) {
            for (int i = 0; i < colors.length; i++) {
                System.out.print(colors[i].shortName());
            }
            System.out.println();
        }
    }

    @Test
    public void test_Score() {
        String s = "--------------- --------------- --------------- " +
                "-----bbb------- ------bb------- -----bb-b------ " +
                "----wbbbbw-b--- ---b--bb-b-b--- ------b-------- " +
                "------w-------- ------b-------- --------------- " +
                "--------------- --------------- ---------------";
        Board b = new Board();
        b.setPieces(s, BLACK);
        System.out.println(b);
        assertEquals(100000, score(b.count(index(9,7))[0]));
        assertEquals(100000, score(b.count(index(8,7))[0]));
        assertEquals(100000, score(b.count(index(7,7))[0]));
        assertEquals(100000, score(b.count(index(6,7))[0]));
        assertEquals(1000, score(b.count(index(7,6))[1]));
        assertEquals(1000, score(b.count(index(7,7))[1]));
        assertEquals(1000, score(b.count(index(7,8))[1]));
        assertEquals(1000, score(b.count(index(7,9))[1]));
        assertEquals(100, score(b.count(index(4,7))[2]));
        assertEquals(100, score(b.count(index(5,8))[2]));
        assertEquals(100, score(b.count(index(6,9))[2]));
        assertEquals(1000, score(b.count(index(7,6))[3]));
        assertEquals(1000, score(b.count(index(6,7))[3]));
        assertEquals(1000, score(b.count(index(5,8))[3]));
    }


    @Test
    public void test_AI() throws IOException {
        String s2 = "--------------- --------------- --------------- " +
                "--------------- --------------- -------b------- " +
                "------www------ -------b-b----- --------------- " +
                "--------------- --------------- --------------- " +
                "--------------- --------------- ---------------";
        Board b = new Board();
        b.setPieces(s2, WHITE);
        b.play(Piece.create(WHITE, 1, 1));
        System.out.println(b);
        System.out.println(findPiece(b, 5, -INFINITY, INFINITY, true, true));
        System.out.println(_last);
    }

    /** Used for testing AI. */
    private int findPiece(Board board, int depth, int alpha, int beta, boolean MaxmizingPlayer, boolean last) {
        Piece best = null;
        if (board.gameOver(true)) {
            return board.whoseMove() == WHITE ? INFINITY - 1 : -INFINITY + 1;
        }
        if (depth == 0) {
            return test_score(board);
        }
        if (MaxmizingPlayer) {
            int response;
            for (Piece p : board.getPotentialPieces(false)) {
//                Board temp = new Board(board);
                board.play(p);
                response = findPiece(board, depth - 1, alpha, beta, false, false);
                board.undo();
                if (response > alpha) {
                    alpha = response;
                    best = p;
                }
                if (alpha == INFINITY - 1 || beta <= alpha) {
                    break;
                }
            }
            if (last) {
                _last = best;
            }
            return alpha;
        } else {
            int response;
            for (Piece p : board.getPotentialPieces(false)) {
//                Board temp = new Board(board);
                board.play(p);
                response = findPiece(board, depth - 1, alpha, beta, true, false);
                board.undo();
                if (response < beta) {
                    beta = response;
                    best = p;
                }
//                beta = Math.min(beta, beta);
                if (beta == -INFINITY + 1 || beta <= alpha) {
                    break;
                }

            }
            if (last) {
                _last = best;
            }
            return beta;
        }
    }

    /** Used for testing. */
    private int test_score(Board board) {
        int blackscore = 0, whitescore = 0;
        for (int i = 0; i < MAX_INDEX; i++) {
            if (board.get(i) != EMPTY) {
                PieceColor[][] colors = board.count(i);
                for (int j = 0; j < 4; j++) {
                    if (board.get(i) == BLACK) {
                        blackscore += score(colors[j]);
                    } else {
                        whitescore += score(colors[j]);
                    }
                }
            }
        }
        return blackscore - whitescore;
    }


    /** Problem about marginal score blocked or not need to be fixed. */
    private int score(PieceColor[] array) {
        PieceColor Mycolor = array[4];
        boolean block1 = false, block2 = false;
        int half1 = 0, half2 = 0;
        for (int i = 1; i <= 4; i++) {
            if (!block1 && array[4 - i] == Mycolor) {
                half1++;
            } else if (array[4 - i] == Mycolor.opposite()) {
                block1 = true;
            }
            if (!block2 && array[4 + i] == Mycolor) {
                half2++;
            } else if (array[4 + i] == Mycolor.opposite()) {
                block2 = true;
            }
            if (half1 + half2 > 4 || (block1 && block2)) {
                break;
            }
        }
        int number = half1 + half2 > 4 ? 4 :half1 + half2;
        if (block1 || block2) {
            return _score1.get(number + 5);
        } else {
            return _score1.get(number);
        }
    }

    private final HashMap<Integer, Integer> _score1 = new HashMap<>(); {
        _score1.put(0, 10);       /* Live one. */
        _score1.put(1, 100);      /* Live two. */
        _score1.put(2, 1000);     /* Live three. */
        _score1.put(3, 10000);    /* Live four. */
        _score1.put(4, 100000);   /* Live five. */
        _score1.put(5, 1);        /* Dead one. */
        _score1.put(6, 10);       /* Dead two. */
        _score1.put(7, 100);      /* Dead three. */
        _score1.put(8, 1000);     /* Dead four. */
        _score1.put(9, 100000);   /* Dead five. */
    }

    /** A magnitude greater than a normal value. */
    private static final int INFINITY = Integer.MAX_VALUE;

    private Piece _last;
}

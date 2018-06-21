package game;

import org.junit.Test;

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
    public void test_GameOver1() {
        String s = "--------------- --------------- --------------- " +
                "----------bb-w- ----------bw--- -----------w--- " +
                "-----------w--- ------bb---w--- -----------w--- " +
                "--------------- ------b-------- --------------- " +
                "--------------- --------------- ---------------";
        Board b = new Board();
        b.setPieces(s, WHITE);
        assertEquals(b.gameOver(), true); // Up
        b.set(6, 12, EMPTY);
        assertEquals(b.gameOver(), false);
        b.set(11, 7, EMPTY);
        assertEquals(b.gameOver(), false);
        b.set(6,10, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(7, 9, BLACK);
        assertEquals(b.gameOver(), true); // UpLeft
        b.set(5, 11, EMPTY);
        assertEquals(b.gameOver(), false);
        b.set(4, 10, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(4, 9, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(4, 8, BLACK);
        assertEquals(b.gameOver(), true); // Right
        b.set(4, 8, WHITE);
        assertEquals(b.gameOver(), false);
        b.set(6, 10, WHITE);
        assertEquals(b.gameOver(), false);
        b.set(5, 9, WHITE);
        assertEquals(b.gameOver(), false);
        b.set(7, 11, WHITE);
        assertEquals(b.gameOver(), true); // UpRight
    }

    @Test
    public void test_GameOver2() {
        Board b = new Board();
        b.set(3, 15, BLACK);
        b.set(4, 1, BLACK);
        b.set(4, 2, BLACK);
        b.set(4, 3, BLACK);
        b.set(4, 4, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(3, 14, BLACK);
        b.set(3, 13, BLACK);
        b.set(3, 12, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(4, 13, BLACK);
        b.set(5, 14, BLACK);
        b.set(6, 15, BLACK);
        b.set(8 , 1, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(7 , 1, BLACK);
        b.set(8, 2, BLACK);
        b.set(9, 3, BLACK);
        b.set(10, 4, BLACK);
        b.set(5 , 15, BLACK);
        assertEquals(b.gameOver(), false);
        b.set(3, 5, BLACK);
        b.set(2, 6, BLACK);
        b.set(15, 7, BLACK);
        assertEquals(b.gameOver(), false);
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

}

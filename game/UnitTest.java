package game;

import org.junit.Test;

import java.util.Formatter;
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
            assertEquals(b.get(x), WHITE);
            assertEquals(b.get(y), BLACK);
            x += SIDE + 1;
            y += SIDE + 1;
        }
        System.out.println(b);
    }
}

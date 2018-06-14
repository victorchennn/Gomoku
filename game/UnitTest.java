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

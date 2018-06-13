package game;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}

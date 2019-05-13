import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    CharacterComparator offByN = new OffByN(5);

    @Test
    public void testOffByN() {
        assertTrue(offByN.equalChars('a', 'f'));
        assertTrue(offByN.equalChars('b', 'g'));
        assertTrue(offByN.equalChars('c', 'h'));
        assertFalse(offByN.equalChars('1', '2'));
        assertFalse(offByN.equalChars('a', 'b'));
        assertFalse(offByN.equalChars('c', 'f'));
    }
}

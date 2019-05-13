import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testEqualChars() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('b', 'c'));
        assertTrue(offByOne.equalChars('1', '2'));
        assertFalse(offByOne.equalChars('a', 'z'));
        assertFalse(offByOne.equalChars('b', 'b'));
        assertFalse(offByOne.equalChars('1', '&'));
    }
}

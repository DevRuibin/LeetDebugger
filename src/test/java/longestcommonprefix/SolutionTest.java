package longestcommonprefix;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SolutionTest {

    @Test
    public void testLongestCommonPrefixExample1() {
        Solution solution = new Solution();
        String[] input = {"flower", "flow", "flight"};
        String expected = "fl";
        String result = solution.longestCommonPrefix(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testLongestCommonPrefixExample2() {
        Solution solution = new Solution();
        String[] input = {"dog", "racecar", "car"};
        String expected = "";
        String result = solution.longestCommonPrefix(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testLongestCommonPrefixNoCommonPrefix() {
        Solution solution = new Solution();
        String[] input = {"abc", "def", "ghi"};
        String expected = "";
        String result = solution.longestCommonPrefix(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testLongestCommonPrefixAllSameWords() {
        Solution solution = new Solution();
        String[] input = {"apple", "apple", "apple"};
        String expected = "apple";
        String result = solution.longestCommonPrefix(input);
        Assertions.assertEquals(expected, result);
    }
}
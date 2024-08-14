package devruibin.github.com.leetdebugger.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void extractHelperClass() {
        String input = """
                /**
                 * Definition for singly-linked list.
                 * public class ListNode {
                 *     int val;
                 *     ListNode next;
                 *     ListNode() {}
                 *     ListNode(int val) { this.val = val; }
                 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
                 * }
                 */
                class Solution {
                    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
                                
                    }
                }
                """;
        String expected = """
                public class ListNode {
                    int val;
                    ListNode next;
                    ListNode() {}
                    ListNode(int val) { this.val = val; }
                    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
                }
                """;
        System.out.println(Utils.extractHelperClass(input));
        assertEquals(expected, Utils.extractHelperClass(input));
    }

}
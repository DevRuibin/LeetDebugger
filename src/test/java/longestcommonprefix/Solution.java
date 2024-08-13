package longestcommonprefix;

class Solution {
    public String longestCommonPrefix(String[] strs) {
        StringBuilder commanPrefix = new StringBuilder();
        int numberOfStrings = strs.length;
        breakLoop:
        for (int i = 0; i < strs[0].length(); i++) {
            char currentChar = strs[0].charAt(i);
            for(int j = 1; j < numberOfStrings; j++){
                if( i >= strs[j].length() || currentChar != strs[j].charAt(i)){
                    break breakLoop;
                }
            }
            commanPrefix.append(currentChar);
        }

        return commanPrefix.toString();
    }
}

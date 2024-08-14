package devruibin.github.com.leetdebugger.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static ClassNameAndCode extractHelperClass(String input) {

        Pattern classDeclarationLine = Pattern.compile("^\\s*\\*?\\s*(public\\s+class\\s+(\\w+)\\s*\\{)\\s*$");

        String className = null;
        StringBuilder extractedCode = new StringBuilder();
        String[] lines = input.split("\n");
        boolean insideCommentBlock = false;
        boolean insideCodeBlock = false;
        for(String line : lines){
            if(line.contains("/*")){
                insideCommentBlock = true;
                continue;
            }
            if(line.contains("*/")){
                break;
            }
            if(insideCommentBlock){
                Matcher matcher = classDeclarationLine.matcher(line);
                if(matcher.matches()){
                    insideCodeBlock = true;
                    extractedCode.append(matcher.group(1)).append("\n");
                    className = matcher.group(2);
                    continue;
                }

            }
            if(insideCodeBlock){
                String substring = line.substring(line.indexOf("*") + 2);
                extractedCode.append(substring).append("\n");
            }
        }

        return new ClassNameAndCode(className, extractedCode.toString());
    }

    public static class ClassNameAndCode {
        String className;
        String code;

        public ClassNameAndCode(String className, String code) {
            this.className = className;
            this.code = code;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}

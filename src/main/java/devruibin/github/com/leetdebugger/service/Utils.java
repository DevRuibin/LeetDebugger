package devruibin.github.com.leetdebugger.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String extractHelperClass(String input) {

        Pattern classDeclarationLine = Pattern.compile("^\\s*\\*?\\s*(public\\s+class\\s+\\w+\\s*\\{)\\s*$");


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
                    continue;
                }

            }
            if(insideCodeBlock){
                String substring = line.substring(line.indexOf("*") + 2);
                extractedCode.append(substring).append("\n");
            }
        }

        return extractedCode.toString();
    }
}

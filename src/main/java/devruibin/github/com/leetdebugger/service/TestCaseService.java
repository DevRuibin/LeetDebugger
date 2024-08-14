package devruibin.github.com.leetdebugger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Service
public class TestCaseService {
    private static final Logger log = LoggerFactory.getLogger(TestCaseService.class);
    private final ChatClient chatClient;
    private final QuestionService questionService;

    public TestCaseService(ChatClient.Builder chatClientBuilder,
                           QuestionService questionService) {
        this.chatClient = chatClientBuilder.build();
        this.questionService = questionService;
    }

    public String generateTestCase(String titleSlug,
                                   String snippet,
                                   String packageName) {
        log.info("Generating test case for {}", titleSlug);
        String prompt = """
                I have received a problem statement that includes a code snippet where I need to implement a method. The problem statement includes examples, constraints, and other details.
                Given the problem and the method signature provided, can you help me generate Java test cases?
                Please generate a Java class for testing that includes:
                	1.	A package named after the packageName.
                	2.	A test class that can call the public method of the Solution class.
                	3.	Test methods that use the examples provided in the problem statement as well as any other necessary test cases.
                	4.	Any necessary assertions to validate the correctness of the method implementation.
                	5. 	Please return only the Java code without any additional text. you don't need to wrap it up using backticks.
                	6.  Please don't give me the solution code. only the test cases. Each test case should be a separate method. The Test class should be named SolutionTest.
                	7. Please don't give me the solution code, please remember that.
                	8. Import any necessary classes. You can only use classes from spring-boot-starter-test or java built-in classes.
                I will provide the problem content and the code snippet, and I would like the test cases generated based on that.
                """;
        String content = Objects.requireNonNull(questionService
                .getQuestionContentByTitleSlug(titleSlug)
                .block())
                .content();


        String finalPrompt = prompt + "\n" +
                "Problem: " + titleSlug + "\n" +
                "PackageName: " + packageName + "\n" +
                "Content: " + content + "\n" +
                "Code Snippet: " + snippet;



        return this.chatClient.prompt().user(finalPrompt).call().content();
    }

    public String generateFilesByTitleSlug(String titleSlug){
        String snippet = Objects.requireNonNull(questionService
                .getQuestionCodeSnippetByTitleSlug(titleSlug)
                .block())
                .code();
        String path = "src/test/java";
        String packageName = titleSlug.replaceAll("-", "");
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        String testClassName = "SolutionTest";
        String solutionClassName = "Solution";
        String SolutionClass = """
                package %s;
                
                %s
                """.formatted(packageName, snippet);

        String testClass = generateTestCase(titleSlug, snippet, packageName);

        // make package directory
        File packageDir = new File(absolutePath + "/" + packageName);
        if(!packageDir.exists()){
            boolean mkdir = packageDir.mkdir();
            if(!mkdir){
                return "Failed to create package directory";
            }
        }


        // write Solution class
        File solutionFile = new File(packageDir, solutionClassName + ".java");
        boolean successWriteSolution = writeToFile(solutionFile, SolutionClass);
        if (!successWriteSolution) {
            return "Failed to write Solution class";
        }
        // write Test class
        File testFile = new File(packageDir, testClassName + ".java");
        boolean successWriteTest = writeToFile(testFile, testClass);
        if (!successWriteTest) {
            return "Failed to write Test class";
        }
        return "Files generated successfully";

    }

    public String generateFiles(String frontendId){

        String titleSlug = Objects.requireNonNull(questionService
                .getSlugById(frontendId));
        return generateFilesByTitleSlug(titleSlug);
    }

    private boolean writeToFile(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {

            throw new RuntimeException("Failed to write to file: " + file.getAbsolutePath());
        }

    }
}

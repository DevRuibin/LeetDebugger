package devruibin.github.com.leetdebugger.service;

import devruibin.github.com.leetdebugger.config.PromptConfig;
import jakarta.annotation.Nullable;
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
    private final PromptConfig promptConfig;

    public TestCaseService(ChatClient.Builder chatClientBuilder,
                           QuestionService questionService, PromptConfig promptConfig) {
        this.chatClient = chatClientBuilder.build();
        this.questionService = questionService;
        this.promptConfig = promptConfig;
    }

    public String generateTestCase(String titleSlug,
                                   String snippet,
                                   String packageName,
                                   String language) {
        log.info("Generating test case for {}", titleSlug);
        String prompt = promptConfig.getPrompt(language.toLowerCase());
        String content = Objects.requireNonNull(questionService
                .getQuestionContentByTitleSlug(titleSlug)
                .block())
                .content();


        String finalPrompt = prompt + "\n" +
                "Problem: " + titleSlug + "\n" +
                "PackageName: " + packageName + "\n" +
                "Content: " + content + "\n" +
                "Code Snippet: " + snippet;

        String generatedTestFile = this.chatClient.prompt().user(finalPrompt).call().content();
        return formatTestFile(generatedTestFile);
    }

    public String formatTestFile(String content){
        if(content == null || content.isEmpty()){
            throw new IllegalArgumentException("Failed to generate test case");
        }
        if(content.contains("```")){
            content = content.substring(content.indexOf("package"));
            content = content.substring(0, content.lastIndexOf("```"));
        }
        return content;
    }

    public String generateFilesByTitleSlug(String titleSlug, String language) {
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

        String testClass = generateTestCase(titleSlug, snippet, packageName, language);

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

    public String generateFiles(String frontendId, @Nullable String language) {
        if(language == null){
            language = "java";
        }
        String titleSlug = Objects.requireNonNull(questionService
                .getSlugById(frontendId));
        return generateFilesByTitleSlug(titleSlug, language);
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

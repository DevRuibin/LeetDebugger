package devruibin.github.com.leetdebugger.service;

import devruibin.github.com.leetdebugger.config.LanguageConfig;
import devruibin.github.com.leetdebugger.config.PromptConfig;
import devruibin.github.com.leetdebugger.model.QuestionContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestCaseServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private TestCaseService testCaseService;

    @Mock
    LanguageConfig languageConfig;

    @Mock
    private PromptConfig promptConfig;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the builder to return the chatClient mock
        Mockito.when(chatClientBuilder.build()).thenReturn(chatClient);

        // Inject the mocks into the TestCaseService
        testCaseService = new TestCaseService(chatClientBuilder, questionService, promptConfig, languageConfig);
    }

    @Test
    void generateTestCase() {
        String titleSlug = "two-sum";
        String snippet = "";
        String packageName = "twosum";
        String mockResponse = """
                ```java
                package twosum;
                import org.junit.jupiter.api.Assertions;
                import org.junit.jupiter.api.Test;
                class SolutionTest {
                    @Test
                    void example1Test() {
                        Solution solution = new Solution();
                        int[] nums = {2, 7, 11, 15};
                        int target = 9;
                        int[] expected = {0, 1};
                        int[] result = solution.twoSum(nums, target);
                        Assertions.assertArrayEquals(expected, result);
                    }
                }
                ```
                """;


        String Prompt = """
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



        // Mocking the ChatClient interaction
        ChatClient.ChatClientRequest prompt = Mockito.mock(ChatClient.ChatClientRequest.class);
        ChatClient.ChatClientRequest.CallResponseSpec callResponseSpec = Mockito.mock(ChatClient.ChatClientRequest.CallResponseSpec.class);

        // Set up the mock behavior
        Mockito.when(chatClient.prompt()).thenReturn(prompt);
        Mockito.when(prompt.user(Mockito.anyString())).thenReturn(prompt);
        Mockito.when(prompt.call()).thenReturn(callResponseSpec);
        Mockito.when(callResponseSpec.content()).thenReturn(mockResponse);
        Mockito.when(promptConfig.getPrompt(Mockito.anyString())).thenReturn(Prompt);

        // Mock the QuestionService response
        Mockito.when(questionService.getQuestionContentByTitleSlug(titleSlug)).thenReturn(
                Mono.just(new QuestionContent("content"))
        );

        // Ensure the service is properly initialized and used
        assertNotNull(testCaseService);

        // Call the method under test
        String result = testCaseService.generateTestCase(titleSlug, snippet, packageName, "java");

        // Print the result for debugging
        System.out.println(result);

        // Add assertions here as needed
    }
}
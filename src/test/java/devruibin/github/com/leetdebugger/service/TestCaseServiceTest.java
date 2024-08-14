package devruibin.github.com.leetdebugger.service;

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

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the builder to return the chatClient mock
        Mockito.when(chatClientBuilder.build()).thenReturn(chatClient);

        // Inject the mocks into the TestCaseService
        testCaseService = new TestCaseService(chatClientBuilder, questionService);
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

        // Mocking the ChatClient interaction
        ChatClient.ChatClientRequest prompt = Mockito.mock(ChatClient.ChatClientRequest.class);
        ChatClient.ChatClientRequest.CallResponseSpec callResponseSpec = Mockito.mock(ChatClient.ChatClientRequest.CallResponseSpec.class);

        // Set up the mock behavior
        Mockito.when(chatClient.prompt()).thenReturn(prompt);
        Mockito.when(prompt.user(Mockito.anyString())).thenReturn(prompt);
        Mockito.when(prompt.call()).thenReturn(callResponseSpec);
        Mockito.when(callResponseSpec.content()).thenReturn(mockResponse);

        // Mock the QuestionService response
        Mockito.when(questionService.getQuestionContentByTitleSlug(titleSlug)).thenReturn(
                Mono.just(new QuestionContent("content"))
        );

        // Ensure the service is properly initialized and used
        assertNotNull(testCaseService);

        // Call the method under test
        String result = testCaseService.generateTestCase(titleSlug, snippet, packageName);

        // Print the result for debugging
        System.out.println(result);

        // Add assertions here as needed
    }
}
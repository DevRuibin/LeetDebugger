package devruibin.github.com.leetdebugger.controller;

import devruibin.github.com.leetdebugger.service.TestCaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCaseController{
    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping("test-case/{frontendId}")
    public String getTestCase(
            @PathVariable String frontendId,
            @RequestParam(required = false) String language)
    {
        return testCaseService.generateFiles(frontendId,language);

    }

    @GetMapping("test-case-by-title-slug/{titleSlug}")
    public String getTestCaseByTitleSlug(@PathVariable String titleSlug) {
        return testCaseService.generateFilesByTitleSlug(titleSlug, "java");

    }


}

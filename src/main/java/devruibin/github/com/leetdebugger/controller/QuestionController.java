package devruibin.github.com.leetdebugger.controller;

import devruibin.github.com.leetdebugger.model.Question;
import devruibin.github.com.leetdebugger.model.QuestionIdSlugPair;
import devruibin.github.com.leetdebugger.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService leetCodeService;
    private final QuestionIdSlugPair questionIdSlugPair;

    @GetMapping("/question")
    public Mono<Question> getQuestion(@RequestParam String titleSlug) {
        return leetCodeService.getQuestion(titleSlug);
    }


    @GetMapping("/id-slug-pairs")
    public Map<String, String> getIdSlugPairs() {
        return questionIdSlugPair.getQuestionIdSlugMap();
    }

    @GetMapping("/get-slug-by-id/{id}")
    public Mono<String> getSlugById(@PathVariable("id") String frontendId) {
        return Mono.just(leetCodeService.getSlugById(frontendId));
    }
}

package devruibin.github.com.leetdebugger.config;

import devruibin.github.com.leetdebugger.model.QuestionIdSlugPair;
import devruibin.github.com.leetdebugger.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadIdSlugPair {
    private final QuestionService questionService;
    private final QuestionIdSlugPair questionIdSlugPair;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        questionService.getAllIdSlugPair()
                .subscribe(questionIdSlugPair::setQuestionIdSlugMap);
    }
}

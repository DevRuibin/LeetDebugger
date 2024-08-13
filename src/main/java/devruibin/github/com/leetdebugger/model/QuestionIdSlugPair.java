package devruibin.github.com.leetdebugger.model;

import lombok.Data;

import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class QuestionIdSlugPair {
    Map<String, String> questionIdSlugMap;
}

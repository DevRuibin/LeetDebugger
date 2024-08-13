package devruibin.github.com.leetdebugger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stat {
    @JsonProperty("frontend_question_id")
    private String frontendQuestionId;

    @JsonProperty("question__title_slug")
    private String questionTitleSlug;
}

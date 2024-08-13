package devruibin.github.com.leetdebugger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CodeSnippet(
        String lang,
        String langSlug,
        String code
) {
}

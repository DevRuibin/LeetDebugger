package devruibin.github.com.leetdebugger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("prompt")
public record PromptConfig(
    /**
     * The key is the language code, and the value is the prompt.
     * */
     Map<String, String> prompts
) {
    public String getPrompt(String key) {
        return prompts.get(key);
        }
}

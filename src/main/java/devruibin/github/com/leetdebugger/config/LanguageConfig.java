package devruibin.github.com.leetdebugger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("language")
public record LanguageConfig(
        /**
         * The key is the language code, and the value is the suffix.
         * */
        Map<String, String> suffix
) {
    public String getSuffix(String key) {
        return suffix.get(key);
    }
}

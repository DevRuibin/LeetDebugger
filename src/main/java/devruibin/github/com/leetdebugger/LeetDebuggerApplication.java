package devruibin.github.com.leetdebugger;

import devruibin.github.com.leetdebugger.config.PromptConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PromptConfig.class)
public class LeetDebuggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeetDebuggerApplication.class, args);
    }




}

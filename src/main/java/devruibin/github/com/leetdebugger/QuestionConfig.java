package devruibin.github.com.leetdebugger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class QuestionConfig {
    @Bean
    public HttpGraphQlClient getGraphqlClient() {
        WebClient client = WebClient.builder()
                .baseUrl("https://leetcode.cn/graphql/")
                .build();
        return HttpGraphQlClient.builder(client).build();
    }

    @Bean
    WebClient webClient(WebClient.Builder webClientBuilder) {
        String leetCodeBaseUrl = "https://leetcode.com";
        return webClientBuilder.baseUrl(leetCodeBaseUrl)
                .build();
    }


}

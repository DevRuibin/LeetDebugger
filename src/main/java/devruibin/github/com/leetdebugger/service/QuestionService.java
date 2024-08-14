package devruibin.github.com.leetdebugger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import devruibin.github.com.leetdebugger.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final HttpGraphQlClient graphQlClient;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final QuestionIdSlugPair questionIdSlugPair;


    public Mono<CodeSnippet> getQuestionCodeSnippet(String frontendId) {
        String titleSlug = questionIdSlugPair.getQuestionIdSlugMap().get(frontendId);
        log.info("titleSlug: {}", titleSlug);
        return getQuestionCodeSnippetByTitleSlug(titleSlug);
    }

    Mono<CodeSnippet> getQuestionCodeSnippetByTitleSlug(String titleSlug) {
        //language=GraphQL
        String document = """
               query questionEditorData($titleSlug: String!){
                question(titleSlug: $titleSlug){
                codeSnippets {
                lang langSlug code }
                 }
                }
               """;

        return graphQlClient.document(document)
                .variable("titleSlug", titleSlug)
                .retrieve("question")
                .toEntity(CodeSnippetList.class)
                .flatMap(codeSnippetList -> {
                    List<CodeSnippet> codeSnippets = codeSnippetList.codeSnippets();
                    return Flux.fromIterable(codeSnippets)
                            .filter(codeSnippet -> "java".equalsIgnoreCase(codeSnippet.langSlug()))
                            .next();
                });
    }

    public Mono<QuestionContent> getQuestionContent(String frontendId) {
        String titleSlug = questionIdSlugPair.getQuestionIdSlugMap().get(frontendId);
        return getQuestionContentByTitleSlug(titleSlug);
    }


    Mono<QuestionContent> getQuestionContentByTitleSlug(String titleSlug) {
        //language=GraphQL
        String document = """
                 query questionContent($titleSlug: String!) {
                     question(titleSlug: $titleSlug) {
                         content
                     }
                 }
                """;

        return graphQlClient.document(document)
                .variable("titleSlug", titleSlug)
                .retrieve("question")
                .toEntity(QuestionContent.class);
    }

    public Mono<Question> getQuestion(String titleSlug) {
        //language=GraphQL
        String document = """
                query questionTitle($titleSlug: String!) {
                    question(titleSlug: $titleSlug) {
                        title
                        titleSlug
                    }
                }
                """;

        return graphQlClient.document(document)
                .variable("titleSlug", titleSlug)
                .retrieve("question")
                .toEntity(Question.class);
    }

    public String getSlugById(String frontendId) {
        return questionIdSlugPair.getQuestionIdSlugMap().get(frontendId);

    }

    public Mono<Map<String, String>> getAllIdSlugPair() {
        return webClient.get()
                .uri("/api/problems/all/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(this::parseJsonResponse);
    }

    private Mono<Map<String, String>> parseJsonResponse(String jsonResponse) {
        try {
            LeetCodeResponse leetCodeResponse = objectMapper.readValue(jsonResponse, LeetCodeResponse.class);
            Map<String, String> idSlugMap = leetCodeResponse.getStatStatusPairs().stream()
                    .collect(Collectors.toMap(
                            statStatusPair -> statStatusPair.getStat().getFrontendQuestionId(),
                            statStatusPair -> statStatusPair.getStat().getQuestionTitleSlug()
                    ));
            return Mono.just(idSlugMap);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to parse JSON response", e));
        }
    }
}

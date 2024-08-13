package devruibin.github.com.leetdebugger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeetCodeResponse {
    @JsonProperty("stat_status_pairs")
    private List<StatStatusPair> statStatusPairs;
}

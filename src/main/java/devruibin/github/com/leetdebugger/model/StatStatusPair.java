package devruibin.github.com.leetdebugger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatStatusPair {
    private Stat stat;
}
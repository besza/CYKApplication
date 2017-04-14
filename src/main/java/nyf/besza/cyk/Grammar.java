package nyf.besza.cyk;

import lombok.Value;

import java.util.Set;

@Value
public class Grammar {
    public static final String STARTING_SYMBOL = "S";

    private final Set<String> terminals;

    private final Set<String> nonTerminals;

    private final Set<ProductionRule> rules;

}

package nyf.besza.cyk;

import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
class ProductionRule {

    private final String lhs;

    private final List<String> rhs;

    @Override
    public String toString() {
        return lhs + "->" + rhs.stream().collect(Collectors.joining("|"));
    }
}

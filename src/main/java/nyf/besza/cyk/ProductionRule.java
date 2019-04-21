package nyf.besza.cyk;

import lombok.Value;

import java.util.List;

@Value
class ProductionRule {

    private final String lhs;

    private final List<String> rhs;

    @Override
    public String toString() {
        return lhs + "->" + String.join("|", rhs);
    }
}

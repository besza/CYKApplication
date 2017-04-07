package nyf.besza.cyk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@EqualsAndHashCode
class ProductionRule {
    @Getter
    private final String lhs;
    @Getter
    private final List<String> rhs;

    @Override
    public String toString() {
        return lhs + "->" + rhs.stream().collect(Collectors.joining("|"));
    }
}

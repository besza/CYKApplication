package nyf.besza.cyk;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the LHS and RHS of a production rule of a given formal grammar.
 * @author szabolcs
 */
class ProductionRule {
    private final String lhs;
    private final List<String> rhs;
    
    public ProductionRule(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
   
    @Override
    public String toString() {
        return lhs + "->" + rhs.stream().collect(Collectors.joining("|"));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        ProductionRule rule = (ProductionRule) other;
        return (lhs.equals(rule.lhs) && Objects.deepEquals(rhs, rule.rhs));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.lhs);
        hash = 19 * hash + Objects.hashCode(this.rhs);
        return hash;
    }

    public String getLhs() {
        return lhs;
    }

    public List<String> getRhs() {
        return rhs;
    }
    
    
    
    
}

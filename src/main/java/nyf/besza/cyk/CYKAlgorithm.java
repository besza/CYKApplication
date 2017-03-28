package nyf.besza.cyk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * *
 *
 * @author szabolcs
 */
class CYKAlgorithm {

    private final String grammar;
    private final List<ProductionRule> productionRules;
    private boolean[][][] p;
    private int r;
    private int n;

    public CYKAlgorithm(String grammar) {
        this.grammar = Objects.requireNonNull(grammar, "Cannot be null!");
        productionRules = new ArrayList<>();
        parseGrammarText();
    }

    private List<String> getNonterminalSymbols() {
        List<String> nonTerminalSymbols = new ArrayList<>();
        productionRules.forEach((prod) -> {
            nonTerminalSymbols.add(prod.getLhs());
        });
        return nonTerminalSymbols
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void parseGrammarText() {
        //TODO: check if grammar is in CNF, otherwise give an error msg
        for (String line : grammar.split("\r?\n")) {
            String[] symbols = line.split("->");
            String lhs = symbols[0];
            List<String> rhs = new ArrayList<>();
            rhs.addAll(Arrays.asList(symbols[1].split("\\|")));
            productionRules.add(new ProductionRule(lhs, rhs));
        }
    }

    public boolean executeAlgorithm(String input) {
        List<String> nonterminals = getNonterminalSymbols();
        
        n = input.length();
        r = nonterminals.size();
        p = new boolean[n][n][r];

        //find the rules R_j -> a_i and flip the corresponding bool
        for (int i = 0; i < n; i++) {
            for (ProductionRule rule : productionRules) {
                for (String symbol : rule.getRhs()) {
                    if (symbol.equals(input.substring(i, i + 1))) {
                        int j = nonterminals.indexOf(rule.getLhs());
                        p[0][i][j] = true;
                    }
                }
            }
        }

        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= n - i + 1; j++) {
                for (int k = 1; k <= i - 1; k++) {
                    for (ProductionRule rule : productionRules) {
                        for (String rhs : rule.getRhs()) {
                            // R_A -> R_BR_C
                            if (rhs.length() == 2) {

                                if (p[k - 1][j - 1][nonterminals.indexOf(rhs.substring(0, 1))]
                                        && p[i - k - 1][k + j - 1][nonterminals.indexOf(rhs.substring(1, 2))]) {
                                    p[i - 1][j - 1][nonterminals.indexOf(rule.getLhs())] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return (p[n - 1][0][0]);
    }

    @Override
    public String toString() {
        return "CYKAlgorithm{" + "grammar=" + grammar + ", productionRules=" + productionRules + '}';
    }



    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    public boolean[][][] getRecognitionMatrix() {
        return p;
    }

    public int getInputLength() {
        return n;
    }

    /**
     * *
     *
     * @param i
     * @param j
     * @return Returns the concatenated grammar rules which can generate the
     * subsequence of length i starting from j.
     */
    public String getSubsequenceRules(int i, int j) {
        StringBuilder sb = new StringBuilder("");
        List<String> nonterminals = getNonterminalSymbols(); //DRY !!
        for (int k = 0; k < p[i][j].length; k++) {
            if (p[i][j][k]) {
                sb.append(nonterminals.get(k));
            }
        }
        return sb.toString();
    }

    private void debugPrint() {
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                System.out.println("(" + i + " " + j + ")");
                for (int k = 0; k < p[i][j].length; k++) {
                    System.out.print(p[i][j][k] + " ");
                }
                System.out.println("");
            }
        }
        productionRules.forEach(System.out::println);
    }

}

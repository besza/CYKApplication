package nyf.besza.cyk;

import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


class CYKAlgorithm {

    @Getter
    private final Grammar grammar;

    @Getter
    private boolean[][][] recognitionMatrix;

    private final List<String> nonTerminalsList;

    CYKAlgorithm(Grammar grammar) {
        this.grammar = Objects.requireNonNull(grammar, "A nyelvtan nem lehet üres!");
        nonTerminalsList = grammar.getNonTerminals().stream().collect(Collectors.toList());
    }

    boolean executeAlgorithm(String input) {
        final int length = input.length();
        recognitionMatrix = new boolean[length][length][grammar.getNonTerminals().size()];
        for (int i = 0; i < length; i++) {
            for (ProductionRule rule : grammar.getRules()) {
                for (String symbol : rule.getRhs()) {
                    if (symbol.equals(input.substring(i, i + 1))) {
                        int j = nonTerminalsList.indexOf(rule.getLhs());
                        recognitionMatrix[0][i][j] = true;
                    }
                }
            }
        }

        for (int i = 2; i <= length; i++) {
            for (int j = 1; j <= length - i + 1; j++) {
                for (int k = 1; k <= i - 1; k++) {
                    for (ProductionRule rule : grammar.getRules()) {
                        for (String rhs : rule.getRhs()) {
                            if (rhs.length() == 2) {
                                if (recognitionMatrix[k - 1][j - 1][nonTerminalsList.indexOf(rhs.substring(0, 1))]
                                        && recognitionMatrix[i - k - 1][k + j - 1][nonTerminalsList.indexOf(rhs.substring(1, 2))]) {
                                    recognitionMatrix[i - 1][j - 1][nonTerminalsList.indexOf(rule.getLhs())] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        //returns true iff the first array of the last row of the matrix contains the starting symbol
        return recognitionMatrix[length - 1][0][nonTerminalsList.indexOf(Grammar.STARTING_SYMBOL)];
    }

    String getSubsequenceRules(int i, int j) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < recognitionMatrix[i][j].length; k++) {
            if (recognitionMatrix[i][j][k]) {
                sb.append(nonTerminalsList.get(k));
            }
        }
        return sb.toString();
    }
}

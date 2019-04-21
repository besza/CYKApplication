package nyf.besza.cyk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

class GrammarParser {

    private static final String EMPTY_STRING = "";

    private static final Pattern TERMINAL_PATTERN = Pattern.compile("[a-z]");

    private static final Pattern NONTERMINAL_PATTERN = Pattern.compile("[A-Z]");

    private static final Pattern CHOMSKY_NORMAL_FORM_PATTERN = Pattern.compile("[A-Z]{2}");

    static Grammar parseGrammar(String rawInput) throws GrammarParserException {
        if (rawInput == null) {
            throw new GrammarParserException("A nyelvtan bemenete nem lehet a null sztring!");
        }

        Set<String> terminals = new HashSet<>();
        Set<String> nonTerminals = new HashSet<>();
        Set<ProductionRule> rules = new HashSet<>();

        for (String line : rawInput.split("\r?\n")) {
            String trimmed = line.trim();
            String whiteSpaceFree = trimmed.replaceAll("\\s+", EMPTY_STRING);
            String[] rule = whiteSpaceFree.split("->");
            if (rule.length == 2) {
                String lhs = rule[0];
                if (NONTERMINAL_PATTERN.matcher(lhs).matches()) {
                    nonTerminals.add(lhs);
                } else {
                    throw new GrammarParserException("Levezetési szabály hibás baloldallal: " + lhs + " !");
                }

                String rhs = rule[1];
                String[] alternatives = rhs.split("\\|");
                if (alternatives.length != 0) {
                    List<String> ruleAlternatives = new ArrayList<>();
                    for (String alt : alternatives) {
                        if (TERMINAL_PATTERN.matcher(alt).matches()) {
                            terminals.add(alt);
                            ruleAlternatives.add(alt);
                        } else if (CHOMSKY_NORMAL_FORM_PATTERN.matcher(alt).matches()) {
                            ruleAlternatives.add(alt);
                        } else {
                            throw new GrammarParserException("Levezetési szabály jobb oldala nem CNF: " + alt + " !");
                        }
                    }
                    rules.add(new ProductionRule(lhs, ruleAlternatives));
                } else {
                    throw new GrammarParserException("Levezetési szabály jobb oldala hiányzik!");
                }
            } else {
                throw new GrammarParserException("Hiba a levezetési szabály alakjában: " + whiteSpaceFree + " !");
            }
        }

        return new Grammar(terminals, nonTerminals, rules);
    }
}

package nyf.besza.cyk;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GrammarParserTest {

    @Test
    public void non_CNF_right_hand_side_test() {
        var input = "S->aB";

        Exception exception = assertThrows(GrammarParserException.class, () -> GrammarParser.parseGrammar(input));
        assertTrue(exception.getMessage().contains("CNF"));
    }

    @Test
    public void not_formal_grammar_rule_test() {
        var input = "AS->C";

        Exception exception = assertThrows(GrammarParserException.class, () -> GrammarParser.parseGrammar(input));
        assertTrue(exception.getMessage().contains("baloldallal"));
    }

    @Test
    public void not_a_grammar_rule_test() {
        var input = "asdf";

        Exception exception = assertThrows(GrammarParserException.class, () -> GrammarParser.parseGrammar(input));
        assertTrue(exception.getMessage().contains("alakjában"));
    }

    @Test
    public void parser_should_ignore_whitespaces() throws GrammarParserException {
        //Given
        var input = "S- > \tA B|c\n";

        var expectedTerminals = new HashSet<>(Collections.singletonList("c"));
        var expectedNonterminals = new HashSet<>(Collections.singletonList("S"));
        var expectedProductionRules = new HashSet<ProductionRule>();
        expectedProductionRules.add(new ProductionRule("S", Arrays.asList("AB", "c")));
        var expectedGrammar = new Grammar(expectedTerminals, expectedNonterminals, expectedProductionRules);

        //When
        var resultingGrammar = GrammarParser.parseGrammar(input);

        //Then
        assertEquals(expectedGrammar, resultingGrammar);
    }
}

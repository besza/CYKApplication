package nyf.besza.cyk;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GrammarParserTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"S->aB", "AS->C", "asdf"})
    public void invalid_grammar_should_throw_exception(String rule) {
        assertThrows(GrammarParserException.class, () -> GrammarParser.parseGrammar(rule));
    }

    @Test
    public void parser_should_ignore_whitespaces() throws GrammarParserException {
        //Given
        var input = "S- > \tA B|c\n";

        var expectedTerminals = Set.of("c");
        var expectedNonterminals = Set.of("S");
        var expectedProductionRules = Set.of(new ProductionRule("S", List.of("AB", "c")));
        var expectedGrammar = new Grammar(expectedTerminals, expectedNonterminals, expectedProductionRules);

        //When
        var resultingGrammar = GrammarParser.parseGrammar(input);

        //Then
        assertEquals(expectedGrammar, resultingGrammar);
    }
}

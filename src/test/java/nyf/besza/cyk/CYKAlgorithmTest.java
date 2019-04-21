package nyf.besza.cyk;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CYKAlgorithmTest {

    @Test
    public void word_should_be_in_the_language() throws GrammarParserException {
        var grammar = String.join("\n", List.of("S->AS|a", "S->AB", "A->AB|SA|b", "B->AS|a"));
        assertTrue(new CYKAlgorithm(GrammarParser.parseGrammar(grammar)).executeAlgorithm("babba"));
    }

    @Test
    public void word_should_not_be_in_the_language() throws GrammarParserException {
        var grammar = String.join("\n", List.of("S->AS|a", "S->AB", "A->AB|SA|b", "B->AS|a"));
        assertFalse(new CYKAlgorithm(GrammarParser.parseGrammar(grammar)).executeAlgorithm("abbab"));
    }

    @RepeatedTest(10)
    public void the_algorithm_should_be_quick() {
        var grammar = String.join("\n", List.of("S->AB|BC", "A->XA|a", "B->UV|VW", "C->YC|c", "X->a", "Y->c",
                "Z->b", "U->XX", "V->ZZ", "W->YY"));

        assertTimeout(Duration.ofMillis(100),
                () -> new CYKAlgorithm(GrammarParser.parseGrammar(grammar)).executeAlgorithm("aabbcc"));
    }
}

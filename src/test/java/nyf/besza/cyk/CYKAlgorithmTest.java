package nyf.besza.cyk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CYKAlgorithmTest {

    private static final String grammar =
            "S->AS|a\n" +
                    "S->AB\n" +
                    "A->AB|SA|b\n" +
                    "B->AS|a\n";

    private static CYKAlgorithm cyk;

    @BeforeAll
    public static void beforeAll() throws GrammarParserException {
        cyk = new CYKAlgorithm(GrammarParser.parseGrammar(grammar));
    }

    @Test
    public void word_should_be_in_the_language() {
        assertTrue(cyk.executeAlgorithm("babba"));
    }

    @Test
    public void word_should_not_be_in_the_language() {
        assertFalse(cyk.executeAlgorithm("abbab"));
    }


}

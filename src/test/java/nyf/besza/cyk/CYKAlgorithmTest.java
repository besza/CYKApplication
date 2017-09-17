package nyf.besza.cyk;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CYKAlgorithmTest {

    private final String grammar =
            "S->AS|a\n" +
                    "S->AB\n" +
                    "A->AB|SA|b\n" +
                    "B->AS|a\n";

    private CYKAlgorithm cyk;

    public CYKAlgorithmTest() throws GrammarParserException {
        cyk = new CYKAlgorithm(GrammarParser.parseGrammar(grammar));
    }

    @Test
    public void wordShouldBeInTheLanguage() {
        assertTrue(cyk.executeAlgorithm("babba"));
    }

    @Test
    public void wordShouldNotBeInTheLanguage() {
        assertFalse(cyk.executeAlgorithm("abbab"));
    }


}

package nyf.besza.cyk;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GrammarParserTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void nonCnfGrammarTest() throws GrammarParserException {
        String input = "S->aB";

        expected.expect(GrammarParserException.class);
        expected.expectMessage("CNF");

        GrammarParser.parseGrammar(input);
    }

    @Test
    public void notFormalGrammarRuleTest() throws GrammarParserException {
        String input = "AS->C";

        expected.expect(GrammarParserException.class);
        expected.expectMessage("baloldallal");

        GrammarParser.parseGrammar(input);
    }

    @Test
    public void notARuleTest() throws GrammarParserException {
        String input = "asdf";

        expected.expect(GrammarParserException.class);
        expected.expectMessage("alakjában");

        GrammarParser.parseGrammar(input);
    }

    @Test
    public void parserShouldIgnoreWhiteSpaces() throws GrammarParserException {
        //Given
        String input = "S- > \tA B|c\n";

        Set<String> expectedTerminals = new HashSet<>(Collections.singletonList("c"));
        Set<String> expectedNonterminals = new HashSet<>(Collections.singletonList("S"));
        Set<ProductionRule> expectedProductionRules = new HashSet<>();
        expectedProductionRules.add(new ProductionRule("S", Arrays.asList("AB", "c")));
        Grammar expectedGrammar = new Grammar(expectedTerminals, expectedNonterminals, expectedProductionRules);

        //When
        Grammar resultingGrammar = GrammarParser.parseGrammar(input);

        //Then
        assertEquals(expectedGrammar, resultingGrammar);
    }
}

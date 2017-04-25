package nyf.besza.cyk;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GrammarParserTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Test
    public void nonCnfGrammarTest() throws GrammarParserException {
        String input = "S->aB";
        expected.expect(GrammarParserException.class);
        expected.expectMessage("CNF");
        Grammar grammar = GrammarParser.parseGrammar(input);
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
}

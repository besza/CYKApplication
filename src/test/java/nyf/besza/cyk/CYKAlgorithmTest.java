/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nyf.besza.cyk;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class CYKAlgorithmTest {
    
    private final String grammar1 = 
            "S->AS|a\n" +
            "S->AB\n" +
            "A->AB|SA|b\n" +
            "B->AS|a\n";
    
    private CYKAlgorithm cyk;
    
    public CYKAlgorithmTest() {
        cyk = new CYKAlgorithm(grammar1);
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testIfWordIsInTheLanguage() {
        assertTrue(cyk.executeAlgorithm("babba"));
    }
    
    @Test
    public void testIfWordIsNotInTheLanguage() {
        assertFalse(cyk.executeAlgorithm("abbab"));
    }

    
}

package osmedile.intellij.stringmanip.styles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ToConstantStyleCaseActionTest {

    protected ToConstantStyleCaseAction action;

    @Test
    public void testTransform() throws Exception {
        action = new ToConstantStyleCaseAction(false);
		assertEquals("!@#$%^&*)(*&|+!!!!!foo!!!!", action.transform("!@#$%^&*)(*&|+!!!!!FOO!!!!"));
        assertEquals("public", action.transform("PUBLIC"));
        assertEquals("PUBLIC", action.transform("public"));
        assertEquals("THIS_IS_A_TEXT", action.transform("ThisIsAText"));
        assertEquals("WHOAH_A_TEST", action.transform("WhoahATest"));
        assertEquals("WHOAH_A_TEST", action.transform("Whoah ATest"));
        assertEquals("WHOAH_A_TEST_AGAIN", action.transform("Whoah  A   Test, again"));
        assertEquals("ANOTHER_T_EST", action.transform("anotherTEst"));
        assertEquals("ANOTHER_T_EST", action.transform("Another      t_Est"));
        assertEquals("TEST_AGAIN_TEST", action.transform("test again     _    _    test"));
        assertEquals("TEST_AGAIN_TEST", action.transform("TestAgain_   _    Test"));
        assertEquals("V2_COUNTER", action.transform("v2Counter"));
        assertEquals("2_V2_COUNTER2", action.transform("2v2Counter2"));
        assertEquals("2_C_2", action.transform("2_c_2"));
        assertEquals("ORGAN_VIZEPRAESIDENT_1", action.transform("organ-vizepraesident-1"));
        assertEquals("FOO_BAR", action.transform("FOO-BAR"));
        assertEquals("2_V2_COUNTER_3", action.transform("_2_v2_counter_3"));
        assertEquals("2v2Counter3", action.transform("2_V2_COUNTER_3"));
//        
//        
//        
        assertEquals("THIS_IS_A_TEXT", action.transform("ThisIsAText"));
        assertEquals("WHOAH_A_TEST", action.transform("Whoah A Test"));

        //todo new insensitive converter?
//        assertEquals("WHOAH_A_TEST", action.transform("Whoah    a tESt")); //WHOAH_A_T_E_ST
//        assertEquals("_ANOTHER_TEXT_", action.transform("_ANOTHER     TExT_")); //ANOTHER_T_EX_T
//        assertEquals("TEST_AGAIN_____TEST",
//                action.transform("test agaIN     _    _    _    _    _    test")); //TEST_AGA_IN_TEST
//        assertEquals("TEST_AGAIN_____TEST",
//                action.transform("test agaIN_    _    _    _    _    Test"));//TEST_AGA_IN_TEST
//        assertEquals("TEST_AGAIN", action.transform(" test agaIN"));//TEST_AGA_IN
//        assertEquals("_TEST_AGAIN", action.transform("_  test agaIN")); //TEST_AGA_IN
//        assertEquals("_TEST_AGAIN", action.transform("   _  test agaIN")); //TEST_AGA_IN
    }
}
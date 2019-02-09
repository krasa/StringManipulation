package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToConstantStyleCaseActionTest {

    protected ToConstantStyleCaseAction action;

    @Test
    public void testTransform() throws Exception {
        action = new ToConstantStyleCaseAction(false);
		assertEquals("11_FOO22_FOO_BAR33_BAR44_FOO55_X6_Y7_Z",
				action.transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
		assertEquals("!@#$%^&*)(*&|+!!!!!FOO!!!!", action.transformByLine("!@#$%^&*)(*&|+!!!!!foo!!!!"));
		assertEquals("!@#$%^&*)(*&|+!!!!!foo!!!!", action.transformByLine("!@#$%^&*)(*&|+!!!!!FOO!!!!"));
		assertEquals("public", action.transformByLine("PUBLIC"));
		assertEquals("PUBLIC", action.transformByLine("public"));
		assertEquals("THIS_IS_ATEXT", action.transformByLine("ThisIsAText"));
		assertEquals("WHOAH_ATEST", action.transformByLine("WhoahATest"));
		assertEquals("WHOAH_ATEST", action.transformByLine("Whoah ATest"));
//		assertEquals("WHOAH_A_TEST_AGAIN", action.transformByLine("Whoah  A   Test, again")); //TODO edge case
        assertEquals("ANOTHER_TEST", action.transformByLine("anotherTEst"));
		assertEquals("ANOTHER_T_EST", action.transformByLine("Another      t_Est"));
		assertEquals("TEST_AGAIN_TEST", action.transformByLine("test again     _    _    test"));
		assertEquals("TEST_AGAIN_TEST", action.transformByLine("TestAgain_   _    Test"));
		assertEquals("V2_COUNTER", action.transformByLine("v2Counter"));
		assertEquals("2_V2_COUNTER2", action.transformByLine("2v2Counter2"));
		assertEquals("2_C_2", action.transformByLine("2_c_2"));
		assertEquals("ORGAN_VIZEPRAESIDENT_1", action.transformByLine("organ-vizepraesident-1"));
		assertEquals("FOO_BAR", action.transformByLine("FOO-BAR"));
		assertEquals("2_V2_COUNTER_3", action.transformByLine("_2_v2_counter_3"));
		assertEquals("2v2Counter3", action.transformByLine("2_V2_COUNTER_3"));
//        
//        
//        
		assertEquals("THIS_IS_ATEXT", action.transformByLine("ThisIsAText"));
		assertEquals("WHOAH_A_TEST", action.transformByLine("Whoah A Test"));

        //todo new insensitive converter?
		// assertEquals("WHOAH_A_TEST", action.transformByLine("Whoah a tESt")); //WHOAH_A_T_E_ST
		// assertEquals("_ANOTHER_TEXT_", action.transformByLine("_ANOTHER TExT_")); //ANOTHER_T_EX_T
//        assertEquals("TEST_AGAIN_____TEST",
		// action.transformByLine("test agaIN _ _ _ _ _ test")); //TEST_AGA_IN_TEST
//        assertEquals("TEST_AGAIN_____TEST",
		// action.transformByLine("test agaIN_ _ _ _ _ Test"));//TEST_AGA_IN_TEST
		// assertEquals("TEST_AGAIN", action.transformByLine(" test agaIN"));//TEST_AGA_IN
		// assertEquals("_TEST_AGAIN", action.transformByLine("_ test agaIN")); //TEST_AGA_IN
		// assertEquals("_TEST_AGAIN", action.transformByLine(" _ test agaIN")); //TEST_AGA_IN
    }
}
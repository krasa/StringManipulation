package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToSnakeCaseActionTest {
    protected ToSnakeCaseAction action;

    @Test
    public void testTransform() throws Exception {
        action = new ToSnakeCaseAction(false);
        assertEquals("2_c_2", action.transform("2_C_2"));
        assertEquals("organ_vizepraesident_1", action.transform("organ-vizepraesident-1"));
        assertEquals("foo_bar", action.transform("FOO-BAR"));
        assertEquals("2_v2_counter_3", action.transform("2-v2-counter-3"));
        assertEquals("2_v2_counter_3", action.transform("2_v2_Counter_3"));
        assertEquals("2_v2_counter_3", action.transform("2_V2_COUNTER_3"));
    }
}
package osmedile.intellij.stringmanip.styles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToSnakeCaseActionTest extends CaseSwitchingTest {
    protected ToSnakeCaseAction action;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		action = new ToSnakeCaseAction(false);
	}

	@Test
    public void testTransform() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(false);
		
		assertEquals("11_foo22_foo_bar33_bar44_foo55_x6_y7_z",
				action.transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
		assertEquals("transform_db", action.transformByLine("transformDB"));
		assertEquals("2_c_2", action.transformByLine("2_C_2"));
		assertEquals("organ_vizepraesident_1", action.transformByLine("organ-vizepraesident-1"));
		assertEquals("foo_bar", action.transformByLine("FOO-BAR"));
		assertEquals("2_v2_counter_3", action.transformByLine("2-v2-counter-3"));
		assertEquals("2_v2_counter_3", action.transformByLine("2_v2_Counter_3"));
		assertEquals("2_v2_counter_3", action.transformByLine("2_V2_COUNTER_3"));
		assertEquals("\\my\\app_bundle\\app\\twig\\google_tag_manager_data_layer", action.transformByLine("\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer"));
	}

	@Test
	public void testTransform2() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(true);

		assertEquals("11_foo_22_foo_bar_33_bar_44_foo_55_x_6_y_7_z",
			action.transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
	}
}
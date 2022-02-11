package osmedile.intellij.stringmanip.styles.switching;

import org.junit.Before;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.CaseSwitchingTest;

import static org.junit.Assert.assertEquals;

public class ToSnakeCaseOrCamelCaseActionTest extends CaseSwitchingTest {
	protected ToSnakeCaseOrCamelCaseAction action;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		action = new ToSnakeCaseOrCamelCaseAction(false);
	}

	@Test
	public void testTransform() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(false);

		assertEquals("11_foo22_foo_bar33_bar44_foo55_x6_y7_z",
				action.test_transform("11foo22fooBAR33BAR44foo55x6Y7Z"));
		assertEquals("transform_db", action.test_transform("transformDB"));
		assertEquals("2_c_2", action.test_transform("2_C_2"));
		assertEquals("organ_vizepraesident_1", action.test_transform("organ-vizepraesident-1"));
		assertEquals("foo_bar", action.test_transform("FOO-BAR"));
		assertEquals("2_v2_counter_3", action.test_transform("2-v2-counter-3"));
		assertEquals("2_v2_counter_3", action.test_transform("2_v2_Counter_3"));
		assertEquals("2_v2_counter_3", action.test_transform("2_V2_COUNTER_3"));
		assertEquals("\\my\\app_bundle\\app\\twig\\google_tag_manager_data_layer", action.test_transform("\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer"));


	}

	@Test
	public void testTransform2() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(true);

		assertEquals("11_foo_22_foo_bar_33_bar_44_foo_55_x_6_y_7_z",
				action.test_transform("11foo22fooBAR33BAR44foo55x6Y7Z"));
	}

	@Test
	public void testTransform3() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(true);

		String actual = action.test_transform("currency\n" +
				"code\n" +
				"Official Rate\n" +
				"rate_buy\n" +
				"sym\n" +
				"symInt\n" +
				"inFront\n" +
				"coin");

		assertEquals("currency\n" +
				"code\n" +
				"official_rate\n" +
				"rate_buy\n" +
				"sym\n" +
				"sym_int\n" +
				"in_front\n" +
				"coin", actual);

		actual = action.test_transform(actual);
		assertEquals("currency\n" +
				"code\n" +
				"officialRate\n" +
				"rateBuy\n" +
				"sym\n" +
				"symInt\n" +
				"inFront\n" +
				"coin", actual);

	}
}
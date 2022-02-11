package osmedile.intellij.stringmanip.styles.switching;

import org.junit.Test;
import osmedile.intellij.stringmanip.styles.CaseSwitchingTest;

import static org.junit.Assert.assertEquals;

public class ToDotStyleOrCamelCaseActionTest extends CaseSwitchingTest {
	protected ToDotStyleOrCamelCaseAction action;

	@Test
	public void testTransform() throws Exception {
		action = new ToDotStyleOrCamelCaseAction(false);
		assertEquals("11.foo.22.foo.bar.33.bar.44.foo.55.x.6.y.7.z", action.test_transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
		assertEquals("\\my\\app.bundle\\app\\twig\\google.tag.manager.data.layer", action.test_transformByLine("\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer"));
		assertEquals("\"submitted.by\", \"owner\", \"creation.date\", \"root.cause\",", action.test_transformByLine("\"Submitted By\", \"Owner\", \"Creation Date\", \"Root Cause\","));
	}
}
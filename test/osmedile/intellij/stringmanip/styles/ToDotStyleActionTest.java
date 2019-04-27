package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToDotStyleActionTest extends CaseSwitchingTest {
	protected ToDotStyleAction action;

	@Test
	public void testTransform() throws Exception {
		action = new ToDotStyleAction(false);
		assertEquals("11.foo.22.foo.bar.33.bar.44.foo.55.x.6.y.7.z", action.transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
		assertEquals("\\my\\app.bundle\\app\\twig\\google.tag.manager.data.layer", action.transformByLine("\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer"));
	}
}
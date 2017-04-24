package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CamelCaseToHyphenLowerCaseActionTest {
	protected CamelCaseToHyphenLowerCaseAction action;

	@Test
	public void testTransform() throws Exception {
		action = new CamelCaseToHyphenLowerCaseAction(false);
		assertEquals("11-foo22-foo-bar33-bar44-foo55-x6-y7-z", action.transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
		assertEquals("\\my\\app-bundle\\app\\twig\\google-tag-manager-data-layer", action.transformByLine("\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer"));
	}
}
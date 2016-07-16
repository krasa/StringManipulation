package osmedile.intellij.stringmanip.styles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ToHyphenCaseActionTest {
	protected ToHyphenCaseAction action;

	@Test
	public void testTransform() throws Exception {
		action = new ToHyphenCaseAction(false);
		assertEquals("11-foo22-foo-bar33-bar44-foo55-x6-y7-z",
				action.transformByLine("11foo22fooBAR33BAR44foo55x6Y7Z"));
	}
}
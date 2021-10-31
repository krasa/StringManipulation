package osmedile.intellij.stringmanip.swap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwapperTest {

	@Test
	public void swap() {
		assertEquals(" scaled width", new Swapper(" width scaled", false).swap());
//
		assertEquals("kuk - foo - bar", new Swapper("foo - bar - kuk", false).swap());

		assertEquals(" [  scaledMaxWidth  ] ", new Swapper(" [  maxWidthScaled  ] ", false).swap());
		assertEquals(" [  ScaledMaxWidth  ] ", new Swapper(" [  MaxWidthScaled  ] ", false).swap());
	}


	@Test
	public void swap2() {
		assertEquals("OLD_STATE CASE_ID", new Swapper("CASE_ID OLD_STATE", false).swap());
		assertEquals("[ OLD_STATE - CASE_ID ]", new Swapper("[ CASE_ID - OLD_STATE ]", false).swap());
		assertEquals("[OLD_STATE - CASE_ID]", new Swapper("[CASE_ID - OLD_STATE]", false).swap());
		assertEquals("[OLD_STATE_ - _CASE_ID]", new Swapper("[_CASE_ID - OLD_STATE_]", false).swap());
		assertEquals("[_STATE_CASE-ID_OLD_]", new Swapper("[_CASE_ID-OLD_STATE_]", false).swap());
	}
}
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
}
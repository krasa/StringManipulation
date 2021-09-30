package osmedile.intellij.stringmanip.swap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwapWordsExecutorTest {
	@Test
	public void testSwapWords() throws Exception {
		SwapWordsExecutor support = new SwapWordsExecutor();

		assertEquals("scaled-width", support.swapWords("width-scaled"));
		assertEquals("SCALED-WIDTH", support.swapWords("WIDTH-SCALED"));
		assertEquals("SCALED-width", support.swapWords("width-SCALED"));

		assertEquals("scaled_width", support.swapWords("width_scaled"));
		assertEquals("SCALED_WIDTH", support.swapWords("WIDTH_SCALED"));
		assertEquals("SCALED_width", support.swapWords("width_SCALED"));

		assertEquals("scaled.width", support.swapWords("width.scaled"));
		assertEquals("SCALED.WIDTH", support.swapWords("WIDTH.SCALED"));
		assertEquals("SCALED.width", support.swapWords("width.SCALED"));

		assertEquals("scaled width", support.swapWords("width scaled"));
		assertEquals("SCALED WIDTH", support.swapWords("WIDTH SCALED"));
		assertEquals("SCALED width", support.swapWords("width SCALED"));

		assertEquals("  scaled  width   ", support.swapWords("  width  scaled   "));
		assertEquals("  SCALED  WIDTH   ", support.swapWords("  WIDTH  SCALED   "));
		assertEquals("  SCALED  width   ", support.swapWords("  width  SCALED   "));
		assertEquals("  width  SCALED   ", support.swapWords("  SCALED  width   "));


		assertEquals("scaledWidth", support.swapWords("widthScaled"));
		assertEquals("ScaledWidth", support.swapWords("WidthScaled"));

		assertEquals("scaledMaxWidth", support.swapWords("maxWidthScaled"));
		assertEquals("ScaledMaxWidth", support.swapWords("MaxWidthScaled"));
		assertEquals("  scaledMaxWidth  ", support.swapWords("  maxWidthScaled  "));
		assertEquals("  ScaledMaxWidth  ", support.swapWords("  MaxWidthScaled  "));
		assertEquals(" [  scaledMaxWidth  ] ", support.swapWords(" [  maxWidthScaled  ] "));
		assertEquals(" [  ScaledMaxWidth  ] ", support.swapWords(" [  MaxWidthScaled  ] "));

		assertEquals("  scaledWidth  ", support.swapWords("  widthScaled  "));
		assertEquals("  ScaledWidth  ", support.swapWords("  WidthScaled  "));


	}

}
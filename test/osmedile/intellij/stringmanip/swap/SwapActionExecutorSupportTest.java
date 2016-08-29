package osmedile.intellij.stringmanip.swap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapActionExecutorSupportTest {
	@Test
	public void testSwapTokens() throws Exception {
		SwapActionExecutorSupport support = new SwapActionExecutorSupport();
		assertEquals("bar,foo", support.swapTokens(",", "foo,bar"));
		assertEquals("bar, foo", support.swapTokens(",", "foo, bar"));
		assertEquals(",foo,bar", support.swapTokens(",", "foo,bar,"));
		assertEquals(", foo, bar ", support.swapTokens(",", "foo, bar, "));
		assertEquals("bar,,foo", support.swapTokens(",", ",foo,bar"));
		assertEquals("bar foo", support.swapTokens(" ", "foo bar"));
		assertEquals("bar foo", support.swapTokens(" ", " foo bar"));
		assertEquals("bar foo", support.swapTokens(" ", " foo    bar    "));
	}

}
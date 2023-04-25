package osmedile.intellij.stringmanip.swap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwapActionExecutorTest {
	@Test
	public void testSwapTokens() throws Exception {
		SwapActionExecutor support = new SwapActionExecutor();
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
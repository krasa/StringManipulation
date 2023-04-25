package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToPascalCaseActionTest extends CaseSwitchingTest {
    protected ToPascalCaseAction action;

    @Test
    public void testTransform() throws Exception {
		action = new ToPascalCaseAction(false);
		assertEquals("HelloWorld", action.test_transformByLine("hello-world"));
		assertEquals("HelloWorld", action.test_transformByLine("HELLO-WORLD"));
		assertEquals("HelloWorld", action.test_transformByLine("hello_world"));
		assertEquals("HelloWorld", action.test_transformByLine("HELLO_WORLD"));
		assertEquals("HelloWorld", action.test_transformByLine("hello.world"));
		assertEquals("HelloWorld", action.test_transformByLine("hello world"));
		assertEquals("HelloWorld", action.test_transformByLine("Hello World"));
		assertEquals("HelloWorld", action.test_transformByLine("HelloWorld"));
		assertEquals("HelloWorld", action.test_transformByLine("helloWorld"));
		assertEquals("Foo", action.test_transformByLine("FOO"));
		assertEquals("Foo", action.test_transformByLine("foo"));
	}
}
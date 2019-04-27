package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToPascalCaseActionTest extends CaseSwitchingTest {
    protected ToPascalCaseAction action;

    @Test
    public void testTransform() throws Exception {
        action = new ToPascalCaseAction(false);
		assertEquals("HelloWorld", action.transformByLine("hello-world"));
		assertEquals("HelloWorld", action.transformByLine("HELLO-WORLD"));
		assertEquals("HelloWorld", action.transformByLine("hello_world"));
		assertEquals("HelloWorld", action.transformByLine("HELLO_WORLD"));
		assertEquals("HelloWorld", action.transformByLine("hello.world"));
		assertEquals("HelloWorld", action.transformByLine("hello world"));
		assertEquals("HelloWorld", action.transformByLine("Hello World"));
		assertEquals("helloWorld", action.transformByLine("HelloWorld"));
		assertEquals("HelloWorld", action.transformByLine("helloWorld"));
		assertEquals("Foo", action.transformByLine("FOO"));
		assertEquals("foo", action.transformByLine("Foo"));
    }
}
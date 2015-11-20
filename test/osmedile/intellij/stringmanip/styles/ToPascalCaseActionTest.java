package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToPascalCaseActionTest {
    protected ToPascalCaseAction action;

    @Test
    public void testTransform() throws Exception {
        action = new ToPascalCaseAction(false);
        assertEquals("HelloWorld", action.transform("hello-world"));
        assertEquals("HelloWorld", action.transform("HELLO-WORLD"));
        assertEquals("HelloWorld", action.transform("hello_world"));
        assertEquals("HelloWorld", action.transform("HELLO_WORLD"));
        assertEquals("HelloWorld", action.transform("hello.world"));
        assertEquals("HelloWorld", action.transform("hello world"));
        assertEquals("HelloWorld", action.transform("Hello World"));
        assertEquals("helloWorld", action.transform("HelloWorld"));
        assertEquals("HelloWorld", action.transform("helloWorld"));
        assertEquals("Foo", action.transform("FOO"));
        assertEquals("foo", action.transform("Foo"));
    }
}
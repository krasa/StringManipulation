package osmedile.intellij.stringmanip.styles.switching;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToSentenceCaseOrCamelCaseActionTest {
	ToSentenceCaseOrCamelCaseAction action;

	@Before
	public void setUp() throws Exception {
		action = new ToSentenceCaseOrCamelCaseAction(false);
	}

	@Test
	public void transformByLine() {
		assertEquals("I a m fine", action.test_transformByLine("i aM.fine"));

		assertEquals("Foo", action.test_transformByLine("foo"));
		assertEquals("Foo", action.test_transformByLine("FOO"));

		assertEquals("Foo bar", action.test_transformByLine("foo bar"));
		assertEquals("Foo bar", action.test_transformByLine("FOO BAR"));

		assertEquals("Foo bar", action.test_transformByLine("foo-bar"));
		assertEquals("Foo bar", action.test_transformByLine("FOO-BAR"));

		assertEquals("Foo bar", action.test_transformByLine("foo.bar"));
		assertEquals("Foo bar", action.test_transformByLine("FOO.BAR"));

		assertEquals("fooBar", action.test_transformByLine("Foo bar"));
		assertEquals("fooBar.", action.test_transformByLine("Foo bar."));
		assertEquals("foo, bar.", action.test_transformByLine("Foo, bar."));
		assertEquals("Foo,bar", action.test_transformByLine("foo,Bar"));
		assertEquals("Foo, bar", action.test_transformByLine("foo, Bar"));
		assertEquals("foo,barBar", action.test_transformByLine("Foo,bar bar"));
	}
}
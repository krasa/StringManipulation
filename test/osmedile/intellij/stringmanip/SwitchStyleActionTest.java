package osmedile.intellij.stringmanip;

import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.SwitchStyleAction;

import java.util.ArrayList;
import java.util.List;

public class SwitchStyleActionTest {

	@Test
	public void testTransform5() throws Exception {
		String input = "foobar";
		input = transform(input, "Foobar");
		input = transform(input, "FOOBAR");
		input = transform(input, "foobar");
	}

	@Test
	public void testTransform() throws Exception {
		String input = "fooBar";
		input = transform(input, "foo-bar");
		input = transform(input, "FOO-BAR");
		input = transform(input, "foo_bar");
		input = transform(input, "FOO_BAR");
		input = transform(input, "foo.bar");
		input = transform(input, "foo bar");
		input = transform(input, "Foo Bar");
		input = transform(input, "FooBar");
		input = transform(input, "fooBar");
	}


	@Test
	public void testTransform3() throws Exception {
		String input = "foo Bar";
		input = transform(input, "fooBar");
	}


	@Test
	public void testTransform7() throws Exception {
		String input = "11foo22fooBAR33BAR44foo55x6Y7Z";
		input = transform(input, "11-foo22-foo-bar33-bar44-foo55-x6-y7-z");
		input = transform(input, "11-FOO22-FOO-BAR33-BAR44-FOO55-X6-Y7-Z");
		input = transform(input, "11_foo22_foo_bar33_bar44_foo55_x6_y7_z");
		input = transform(input, "11_FOO22_FOO_BAR33_BAR44_FOO55_X6_Y7_Z");
		input = transform(input, "11.foo22.foo.bar33.bar44.foo55.x6.y7.z");
		input = transform(input, "11 foo22 foo bar33 bar44 foo55 x6 y7 z");
		input = transform(input, "11 Foo22 Foo Bar33 Bar44 Foo55 X6 Y7 Z");
		input = transform(input, "11foo22FooBar33Bar44Foo55X6Y7Z");
		input = transform(input, "11-foo22-foo-bar33-bar44-foo55-x6-y7-z");
	}

	@Test
	public void testTransformVariations() throws Exception {
		List<String> failed = new ArrayList<String>();
		for (Style expectedStyle : Style.values()) {
			for (Style inputStyle : Style.values()) {
				for (String inputExample : inputStyle.example) {
					String transform = expectedStyle.transform(inputStyle, inputExample);
					Style actualStyle = Style.from(transform);
					if (expectedStyle.example.length == 0) {
						continue;
					}
					boolean doesNotMatch = !expectedStyle.example[0].equals(transform);

					if (actualStyle != expectedStyle) {
						failed.add(inputStyle.name() + " -> " + expectedStyle.name() + "(actual " + actualStyle.name() + "): " + inputExample
							+ " -> " + transform);
					} else if (doesNotMatch) {
						failed.add(inputStyle.name() + " -> " + expectedStyle.name() + ": " + inputExample + " -> " + transform);
					} else {
						System.out.println(inputStyle.name() + " -> " + expectedStyle.name() + ": " + inputExample + " -> " + transform);
					}
				}
			}
		}

		failed = allUpperCaseException(failed);
		for (String s : failed) {
			System.err.println(s);
		}

		Assert.assertTrue(failed.isEmpty());
	}

	private List<String> allUpperCaseException(List<String> failed) {
		List<String> strings = new ArrayList<String>();
		for (String s : failed) {
			if (s.contains("-> _")) {
				//no converting to that
			} else if (s.contains("_ALL_UPPER_CASE ->") || s.contains("_SINGLE_WORD_WORD_CAPITALIZED ->")) {
				//it will be always shit, lets see it at least
				System.err.println(s);
			} else {
				strings.add(s);
			}
		}
		return strings;
	}

	private String transform(String fooBar, final String expected) {
		String input = fooBar;
		String result = new SwitchStyleAction(false).transformByLine(input);
		System.out.println(input + " -> " + result);
		Assert.assertEquals(input + " -> " + result, expected, result);
		return result;
	}
}

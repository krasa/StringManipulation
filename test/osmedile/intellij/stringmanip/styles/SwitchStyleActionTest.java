package osmedile.intellij.stringmanip.styles;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;
import osmedile.intellij.stringmanip.align.ColumnAligner;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;
import osmedile.intellij.stringmanip.styles.custom.CustomAction;
import osmedile.intellij.stringmanip.styles.custom.DefaultActions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwitchStyleActionTest extends CaseSwitchingTest {

	protected static final String TEST_TRANSFORM_VARIATIONS_TXT = "testTransformVariations.txt";
	AbstractCaseConvertingAction switchStyleAction;

	@Before
	public void setUp() throws Exception {
//		switchStyleAction = new SwitchStyleAction(false);
		switchStyleAction = new CustomAction(false, DefaultActions.defaultSwitchCase());
	}

	@Test
	public void testTransform5() throws Exception {
		String input = "foobar";
		input = transform(input, "Foobar");
		input = transform(input, "FOOBAR");
		input = transform(input, "foobar");
	}

	@Test
	public void testTransform1() throws Exception {
		String input = "\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer";
		input = transform(input, "\\my\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer");
		input = transform(input, "\\my\\app-bundle\\app\\twig\\google-tag-manager-data-layer");
		input = transform(input, "\\MY\\APP-BUNDLE\\APP\\TWIG\\GOOGLE-TAG-MANAGER-DATA-LAYER");
		input = transform(input, "\\my\\app_bundle\\app\\twig\\google_tag_manager_data_layer");
		input = transform(input, "\\MY\\APP_BUNDLE\\APP\\TWIG\\GOOGLE_TAG_MANAGER_DATA_LAYER");
		input = transform(input, "\\my\\app.bundle\\app\\twig\\google.tag.manager.data.layer");
		input = transform(input, "\\my\\app bundle\\app\\twig\\google tag manager data layer");
		input = transform(input, "\\My\\app bundle\\app\\twig\\google tag manager data layer");
		input = transform(input, "\\My\\App Bundle\\App\\Twig\\Google Tag Manager Data Layer");
		input = transform(input, "\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer");
		input = transform(input, "\\my\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer");
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
		input = transform(input, "Foo bar");
		input = transform(input, "Foo Bar");
		input = transform(input, "FooBar");
		input = transform(input, "fooBar");
	}

	@Test
	public void testTransform_a() throws Exception {
		try {
			CaseSwitchingSettings.getInstance().setSeparatorAfterDigit(false);
			CaseSwitchingSettings.getInstance().setSeparatorBeforeDigit(false);

			String input = "fooBar1a";
			input = transform(input, "foo-bar1a");
			input = transform(input, "FOO-BAR1A");
			input = transform(input, "foo_bar1a");
			input = transform(input, "FOO_BAR1A");
			input = transform(input, "foo.bar1a");
			input = transform(input, "foo bar1a");
			input = transform(input, "Foo bar1a");
			input = transform(input, "Foo Bar1a");
			input = transform(input, "FooBar1A");
			input = transform(input, "fooBar1A");
			input = transform(input, "foo-bar1a");
		} finally {
			CaseSwitchingSettings.getInstance().resetToDefault();
		}
	}


	@Test
	public void testTransform3() throws Exception {
		String input = "foo Bar";
		input = transform(input, "fooBar");
	}


	@Test
	public void testTransform7() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(false);
		String input = "11foo22fooBAR33BAR44foo55x6Y7Z";
		input = transform(input, "11-foo22-foo-bar33-bar44-foo55-x6-y7-z");
		input = transform(input, "11-FOO22-FOO-BAR33-BAR44-FOO55-X6-Y7-Z");
		input = transform(input, "11_foo22_foo_bar33_bar44_foo55_x6_y7_z");
		input = transform(input, "11_FOO22_FOO_BAR33_BAR44_FOO55_X6_Y7_Z");
		input = transform(input, "11.foo22.foo.bar33.bar44.foo55.x6.y7.z");
		input = transform(input, "11 foo22 foo bar33 bar44 foo55 x6 y7 z");
		input = transform(input, "11 Foo22 foo bar33 bar44 foo55 x6 y7 z");
		input = transform(input, "11 Foo22 Foo Bar33 Bar44 Foo55 X6 Y7 Z");
		input = transform(input, "11Foo22FooBar33Bar44Foo55X6Y7Z");
		input = transform(input, "11foo22FooBar33Bar44Foo55X6Y7Z");
	}
	@Test
	public void testTransformVariations() throws Exception {
		int j = 0;
		List<String> ok = new ArrayList<String>();
		List<String> failed = new ArrayList<String>();
		for (Style expectedStyle : Style.values()) {
			for (Style inputStyle : Style.values()) {
				for (String inputExample : inputStyle.example) {
					j++;
					if (j == 3) {
						j = j;     //breakpoint
					}
					String transform = expectedStyle.transform(inputStyle, inputExample);
					Style actualStyle = Style.from(transform);
					if (expectedStyle.example.length == 0) {
						continue;
					}
					boolean doesNotMatch = !expectedStyle.example[0].equals(transform);

					if (actualStyle != expectedStyle) {
						failed.add(j + " " + inputStyle.name() + " -> " + expectedStyle.name() + "(actual " + actualStyle.name() + "): " + inputExample
							+ " -> " + transform);
					} else if (doesNotMatch) {
						failed.add(j + " " + inputStyle.name() + " -> " + expectedStyle.name() + ": " + inputExample + " -> " + transform);
					} else {
						ok.add(j + " " + inputStyle.name() + " -> " + expectedStyle.name() + ": " + inputExample + " -> " + transform);
					}
				}
			}
		}

		failed = allUpperCaseException(failed);
		String text = toText(ok, failed);
		print(text);
		writeToFile(TEST_TRANSFORM_VARIATIONS_TXT, text);
		//from _ALL_UPPER_CASE and _SINGLE_WORD_CAPITALIZED will always fail, is showing them as fail anyway ok? 
		Assert.assertTrue(failed.isEmpty());
	}

	@Test
	public void testEdgeCases() throws Exception {
		int j = 0;
		List<String> ok = new ArrayList<String>();
		List<String> failed = new ArrayList<String>();
		for (Style style : Style.values()) {
			if (style.name().startsWith("_")) {
				continue;
			}
			List<String> inputs = Arrays.asList(
				"foo bar 1 1",
				"foo bar 1.1",
				"foo bar 1_1",
				"foo bar 1,1",

				"foo_bar 1 1",
				"foo_bar 1.1",
				"foo_bar 1_1",
				"foo_bar 1,1",

				"foo.bar 1 1",
				"foo.bar 1.1",
				"foo.bar 1_1",
				"foo.bar 1,1",

				"foo,bar 1 1",
				"foo,bar 1.1",
				"foo,bar 1_1",
				"foo,bar 1,1",

				"FooBar1 1",
				"FooBar1.1",
				"FooBar1_1",
				"FooBar1,1",

				"Foo Bar1 1",
				"Foo Bar1.1",
				"Foo Bar1_1",
				"Foo Bar1,1",

				"foobar",

				"foo,bar",
				"foo, bar",
				"foo , bar",
				"foo.,_bar",
				"dark border350A",
				"dark border35.0A",
				"dark border35,0A",
				"dark border35_0A",
				"dark border35-0A",
				"dark border350a",
				"dark border35.0a",
				"dark border35,0a",
				"dark border35_0a",
				"dark border35-0a",
				"DarkBorder350A",
				"DarkBorder350a",
				"foo350a",
				"foo350",
				"Foo350",
				"\\My\\AppBundle\\App\\Twig\\GoogleTagManagerDataLayer",
				""
			);

			for (String input : inputs) {
				j++;
				if (input == "") {
					continue;
				}
				if (j == 196) {
					j = j;     //breakpoint
				}
				String transform = style.transform(input);
				Style actualStyle = Style.from(transform);
				Style inputStyle = Style.from(input);
				if (style.example.length == 0) {
					continue;
				}

				if (actualStyle != style) {
					ok.add(j + "F " + inputStyle.name() + " -> " + style.name() + "(actual " + actualStyle.name() + "): " + input
						+ " -> " + transform);
				} else {
					ok.add(j + " " + inputStyle.name() + " -> " + style.name() + ": " + input + " -> " + transform);
				}
			}
		}

//		failed = allUpperCaseException(failed);
		String text = toText(ok, failed);
		print(text);

		writeToFile("testEdgeCases.txt", text);

	}

	protected void print(String text) throws InterruptedException {
		String[] split = text.split("\n");
		boolean fail = false;
		for (String s : split) {
			if (s.contains("FAIL")) {
				fail = true;
				Thread.sleep(100);
			}
			if (fail) {
				System.err.println(s);
			} else {
				System.out.println(s);
			}
		}
	}

	protected String toText(List<String> ok, List<String> failed) {
		StringBuilder sb = new StringBuilder();
		for (String s1 : ok) {
			sb.append(s1).append("\n");
		}
		if (!failed.isEmpty()) {
			sb.append("\nFAILED:\n");
			for (String s1 : failed) {
				sb.append(s1).append("\n");
			}
		}

		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel();
		columnAlignerModel.setSequentialProcessing(false);
		columnAlignerModel.setAlignBy(ColumnAlignerModel.Align.SEPARATORS);
		columnAlignerModel.setSeparators(Arrays.asList("->", ":"));
		return new ColumnAligner(columnAlignerModel).align(sb.toString());
	}

	private void writeToFile(String s, String text) {
		File file = new File("test/osmedile/intellij/stringmanip/styles/" + s);
		try {
			FileUtils.writeStringToFile(file, text);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<String> allUpperCaseException(List<String> failed) {
		List<String> strings = new ArrayList<String>();
		for (String s : failed) {
			if (s.contains("-> _")) {
				//no converting to that
			} else if (s.contains("_ALL_UPPER_CASE ->") || s.contains("_SINGLE_WORD_CAPITALIZED ->") || s.contains("_SINGLE_WORD_WORD_CAPITALIZED ->")) {
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
		String result = switchStyleAction.transformByLine(input);
		System.out.println(input + " -> " + result);
		Assert.assertEquals(input + " -> " + result, expected, result);
		return result;
	}

	@Test
	public void test_SENTENCE_CASE() {
		Assert.assertEquals("11 Foo22 foo bar33 bar44 foo55 x6 y7 z", Style.SENTENCE_CASE.transform("11 foo22 foo bar33 bar44 foo55 x6 y7 z"));


	}
}

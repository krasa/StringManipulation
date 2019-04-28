package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StyleTest {

	@Test
	public void isCapitalizedFirstWordOnly() {
		assertEquals(true, Style.isCapitalizedFirstButNotAll("\\My\\app bundle\\app\\twig\\google tag manager data layer"));
		assertEquals(true, Style.isCapitalizedFirstButNotAll("11 Foo22 foo bar33 bar44 foo55 x6 y7 z"));
		assertEquals(true, Style.isCapitalizedFirstButNotAll("11 Foo22 Foo bar33 bar44 foo55 x6 y7 z"));
		assertEquals(false, Style.isCapitalizedFirstButNotAll("11 foo22 Foo bar33 bar44 foo55 x6 y7 z"));
		assertEquals(false, Style.isCapitalizedFirstButNotAll("11 foo22 Foo bar33 bar44 foo55 x6 y7 z"));
		assertEquals(false, Style.isCapitalizedFirstButNotAll("11 foo22 foo bar33 bar44 foo55 x6 y7 z"));
	}

	@Test
	public void name() {
		assertEquals(Style._SINGLE_WORD_CAPITALIZED, Style.from("Foo,bar"));
		assertEquals(Style.PASCAL_CASE, Style.from("Foo,Bar"));
		assertEquals(Style.WORD_CAPITALIZED, Style.from("Foo Bar "));
		assertEquals("FOO_BAR", Style.SCREAMING_SNAKE_CASE.transform("Foo Bar "));
		assertEquals("fooBar1_1", Style.CAMEL_CASE.transform(" foo bar 1_1 "));
	}
}
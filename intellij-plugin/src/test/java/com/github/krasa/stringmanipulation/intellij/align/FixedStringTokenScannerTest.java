package com.github.krasa.stringmanipulation.intellij.align;

import com.github.krasa.stringmanipulation.utils.align.FixedStringTokenScanner;
import org.junit.Assert;
import org.junit.Test;

public class FixedStringTokenScannerTest {


	@Test
	public void limit() {

		Assert.assertArrayEquals(new String[]{
			"foo",
			" ",
			"bar   "
		}, FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("   foo     bar   ", 1, " ").toArray());


		Assert.assertArrayEquals(new String[]{
				"  ",
				",",
				" foo ",
				",",
				" bar ,  "
			},
			FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("  , foo , bar ,  ", 2, ",").toArray());

		Assert.assertArrayEquals(new String[]{
				"  ",
				"|",
				" foo ",
				"|",
				"",
				"|",
				"||  "
			},
			FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("  | foo ||||  ", 3, "|").toArray());

	}

	@Test
	public void splitToFixedStringTokensAndOtherTokens() {
		Assert.assertArrayEquals(new String[]{
				"foo",
				" ",
				"bar"
			},
			FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("foo           bar", -1, " ").toArray());


		Assert.assertArrayEquals(new String[]{
			"foo",
			" ",
			"bar",
			" ",
			""
		}, FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("   foo  bar       ", -1, " ").toArray());

		Assert.assertArrayEquals(new String[]{
				"  ",
				",",
				" foo ",
				",",
				" bar ",
				",",
				"  "
			},
			FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("  , foo , bar ,  ", -1, ",").toArray());

	}
}
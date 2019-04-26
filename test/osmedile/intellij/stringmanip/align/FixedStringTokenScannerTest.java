package osmedile.intellij.stringmanip.align;

import org.junit.Assert;
import org.junit.Test;

public class FixedStringTokenScannerTest {

	@Test
	public void splitToFixedStringTokensAndOtherTokens() {
		Assert.assertEquals("[foo,  , bar]", FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("foo  bar", " ").toString());
		Assert.assertEquals("[foo,  , bar,  , ]", FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("   foo  bar   ", " ").toString());
		Assert.assertEquals("[  , ,,  foo , ,,  bar , ,,   ]", FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens("  , foo , bar ,  ", ",").toString());
	}
}
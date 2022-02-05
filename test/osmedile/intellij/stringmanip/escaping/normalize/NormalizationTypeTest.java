package osmedile.intellij.stringmanip.escaping.normalize;

import org.junit.Test;
import osmedile.intellij.stringmanip.escaping.DiacriticsToAsciiAction;

import static org.junit.Assert.assertEquals;

public class NormalizationTypeTest {

	@Test
	public void normalize() {
		assertEquals("eclair", NormalizationType.STRIP_ACCENTS.normalize("éclair"));
		assertEquals("eclair", DiacriticsToAsciiAction.toPlain("éclair"));

		assertEquals("OE", DiacriticsToAsciiAction.toPlain("Œ"));
		assertEquals("Œ", NormalizationType.STRIP_ACCENTS.normalize("Œ"));

	}
}
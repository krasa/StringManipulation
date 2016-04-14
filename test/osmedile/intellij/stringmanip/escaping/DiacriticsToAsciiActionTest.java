package osmedile.intellij.stringmanip.escaping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DiacriticsToAsciiActionTest {
	public DiacriticsToAsciiAction action;
	private boolean fail;

	@Test
	public void transform() throws Exception {
		action = new DiacriticsToAsciiAction(false);
		assertEquals("Ceska Republika", action.transform("Česká Republika"));
		assertEquals("escrzyaie", action.transform("ěščřžýáíé"));
		assertEquals("Et ca sera sa moitie.", action.transform("Et ça sera sa moitié."));
		assertEquals("oeisoc", action.transform("òéışöç"));
		assertEquals("ldh", action.transform("łđħ"));

		// diacritics from https://docs.oracle.com/cd/E29584_01/webhelp/mdex_basicDev/src/rbdv_chars_mapping.html
		// expected output by http://www.miniwebtool.com/remove-accent/
		checkTransformation("A", "À");
		checkTransformation("A", "Á");
		checkTransformation("A", "Â");
		checkTransformation("A", "Ã");
		checkTransformation("A", "Ä");
		checkTransformation("A", "Å");
		checkTransformation("AE", "Æ");
		checkTransformation("C", "Ç");
		checkTransformation("E", "È");
		checkTransformation("E", "É");
		checkTransformation("E", "Ê");
		checkTransformation("E", "Ë");
		checkTransformation("I", "Ì");
		checkTransformation("I", "Í");
		checkTransformation("I", "Î");
		checkTransformation("I", "Ï");
		checkTransformation("D", "Ð");
		checkTransformation("N", "Ñ");
		checkTransformation("O", "Ò");
		checkTransformation("O", "Ó");
		checkTransformation("O", "Ô");
		checkTransformation("O", "Õ");
		checkTransformation("O", "Ö");
		checkTransformation("O", "Ø");
		checkTransformation("U", "Ù");
		checkTransformation("U", "Ú");
		checkTransformation("U", "Û");
		checkTransformation("U", "Ü");
		checkTransformation("Y", "Ý");
		checkTransformation("TH", "Þ");
		checkTransformation("ss", "ß");
		checkTransformation("a", "à");
		checkTransformation("a", "á");
		checkTransformation("a", "â");
		checkTransformation("a", "ã");
		checkTransformation("a", "ä");
		checkTransformation("a", "å");
		checkTransformation("ae", "æ");
		checkTransformation("c", "ç");
		checkTransformation("e", "è");
		checkTransformation("e", "é");
		checkTransformation("e", "ê");
		checkTransformation("e", "ë");
		checkTransformation("i", "ì");
		checkTransformation("i", "í");
		checkTransformation("i", "î");
		checkTransformation("i", "ï");
		checkTransformation("d", "ð");
		checkTransformation("n", "ñ");
		checkTransformation("o", "ò");
		checkTransformation("o", "ó");
		checkTransformation("o", "ô");
		checkTransformation("o", "õ");
		checkTransformation("o", "ö");
		checkTransformation("o", "ø");
		checkTransformation("u", "ù");
		checkTransformation("u", "ú");
		checkTransformation("u", "û");
		checkTransformation("u", "ü");
		checkTransformation("y", "ý");
		checkTransformation("th", "þ");
		checkTransformation("y", "ÿ");
		checkTransformation("A", "Ā");
		checkTransformation("a", "ā");
		checkTransformation("A", "Ă");
		checkTransformation("a", "ă");
		checkTransformation("A", "Ą");
		checkTransformation("a", "ą");
		checkTransformation("C", "Ć");
		checkTransformation("c", "ć");
		checkTransformation("C", "Ĉ");
		checkTransformation("c", "ĉ");
		checkTransformation("C", "Ċ");
		checkTransformation("c", "ċ");
		checkTransformation("C", "Č");
		checkTransformation("c", "č");
		checkTransformation("D", "Ď");
		checkTransformation("d", "ď");
		checkTransformation("D", "Đ");
		checkTransformation("d", "đ");
		checkTransformation("E", "Ē");
		checkTransformation("e", "ē");
		checkTransformation("E", "Ĕ");
		checkTransformation("e", "ĕ");
		checkTransformation("E", "Ė");
		checkTransformation("e", "ė");
		checkTransformation("E", "Ę");
		checkTransformation("e", "ę");
		checkTransformation("E", "Ě");
		checkTransformation("e", "ě");
		checkTransformation("G", "Ĝ");
		checkTransformation("g", "ĝ");
		checkTransformation("G", "Ğ");
		checkTransformation("g", "ğ");
		checkTransformation("G", "Ġ");
		checkTransformation("g", "ġ");
		checkTransformation("G", "Ģ");
		checkTransformation("g", "ģ");
		checkTransformation("H", "Ĥ");
		checkTransformation("h", "ĥ");
		checkTransformation("H", "Ħ");
		checkTransformation("h", "ħ");
		checkTransformation("I", "Ĩ");
		checkTransformation("i", "ĩ");
		checkTransformation("I", "Ī");
		checkTransformation("i", "ī");
		checkTransformation("I", "Ĭ");
		checkTransformation("i", "ĭ");
		checkTransformation("I", "Į");
		checkTransformation("i", "į");
		checkTransformation("I", "İ");
		checkTransformation("i", "ı");
		checkTransformation("IJ", "Ĳ");
		checkTransformation("ij", "ĳ");
		checkTransformation("J", "Ĵ");
		checkTransformation("j", "ĵ");
		checkTransformation("K", "Ķ");
		checkTransformation("k", "ķ");
		checkTransformation("q", "ĸ");
		checkTransformation("L", "Ĺ");
		checkTransformation("l", "ĺ");
		checkTransformation("L", "Ļ");
		checkTransformation("l", "ļ");
		checkTransformation("L", "Ľ");
		checkTransformation("l", "ľ");
		checkTransformation("L", "Ŀ");
		checkTransformation("l", "ŀ");
		checkTransformation("L", "Ł");
		checkTransformation("l", "ł");
		checkTransformation("N", "Ń");
		checkTransformation("n", "ń");
		checkTransformation("N", "Ņ");
		checkTransformation("n", "ņ");
		checkTransformation("N", "Ň");
		checkTransformation("n", "ň");
		checkTransformation("n", "ŉ");
		checkTransformation("N", "Ŋ");
		checkTransformation("n", "ŋ");
		checkTransformation("O", "Ō");
		checkTransformation("o", "ō");
		checkTransformation("O", "Ŏ");
		checkTransformation("o", "ŏ");
		checkTransformation("O", "Ő");
		checkTransformation("o", "ő");
		checkTransformation("OE", "Œ");
		checkTransformation("oe", "œ");
		checkTransformation("R", "Ŕ");
		checkTransformation("r", "ŕ");
		checkTransformation("R", "Ŗ");
		checkTransformation("r", "ŗ");
		checkTransformation("R", "Ř");
		checkTransformation("r", "ř");
		checkTransformation("S", "Ś");
		checkTransformation("s", "ś");
		checkTransformation("S", "Ŝ");
		checkTransformation("s", "ŝ");
		checkTransformation("S", "Ş");
		checkTransformation("s", "ş");
		checkTransformation("S", "Š");
		checkTransformation("s", "š");
		checkTransformation("T", "Ţ");
		checkTransformation("t", "ţ");
		checkTransformation("T", "Ť");
		checkTransformation("t", "ť");
		checkTransformation("T", "Ŧ");
		checkTransformation("t", "ŧ");
		checkTransformation("U", "Ũ");
		checkTransformation("u", "ũ");
		checkTransformation("U", "Ū");
		checkTransformation("u", "ū");
		checkTransformation("U", "Ŭ");
		checkTransformation("u", "ŭ");
		checkTransformation("U", "Ů");
		checkTransformation("u", "ů");
		checkTransformation("U", "Ű");
		checkTransformation("u", "ű");
		checkTransformation("U", "Ų");
		checkTransformation("u", "ų");
		checkTransformation("W", "Ŵ");
		checkTransformation("w", "ŵ");
		checkTransformation("Y", "Ŷ");
		checkTransformation("y", "ŷ");
		checkTransformation("Y", "Ÿ");
		checkTransformation("Z", "Ź");
		checkTransformation("z", "ź");
		checkTransformation("Z", "Ż");
		checkTransformation("z", "ż");
		checkTransformation("Z", "Ž");
		checkTransformation("z", "ž");
		checkTransformation("s", "ſ");
		failTransformations();
	}

	private void checkTransformation(String expected, String input) {
		String s = action.transform(input);
		if (!expected.equals(s)) {
			System.err.println("\n" + "\t\t\tcase '" + input + "':\n" + "\t\t\t\tsb.append(\"" + expected + "\");\n"
					+ "\t\t\t\tbreak;");
			fail = true;
		}
	}

	private void failTransformations() {
		if (fail) {
			fail();
		}
	}

}
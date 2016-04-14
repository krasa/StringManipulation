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
		assertEquals2("A", "À");
		assertEquals2("A", "Á");
		assertEquals2("A", "Â");
		assertEquals2("A", "Ã");
		assertEquals2("A", "Ä");
		assertEquals2("A", "Å");
		assertEquals2("AE", "Æ");
		assertEquals2("C", "Ç");
		assertEquals2("E", "È");
		assertEquals2("E", "É");
		assertEquals2("E", "Ê");
		assertEquals2("E", "Ë");
		assertEquals2("I", "Ì");
		assertEquals2("I", "Í");
		assertEquals2("I", "Î");
		assertEquals2("I", "Ï");
		assertEquals2("D", "Ð");
		assertEquals2("N", "Ñ");
		assertEquals2("O", "Ò");
		assertEquals2("O", "Ó");
		assertEquals2("O", "Ô");
		assertEquals2("O", "Õ");
		assertEquals2("O", "Ö");
		assertEquals2("O", "Ø");
		assertEquals2("U", "Ù");
		assertEquals2("U", "Ú");
		assertEquals2("U", "Û");
		assertEquals2("U", "Ü");
		assertEquals2("Y", "Ý");
		assertEquals2("TH", "Þ");
		assertEquals2("ss", "ß");
		assertEquals2("a", "à");
		assertEquals2("a", "á");
		assertEquals2("a", "â");
		assertEquals2("a", "ã");
		assertEquals2("a", "ä");
		assertEquals2("a", "å");
		assertEquals2("ae", "æ");
		assertEquals2("c", "ç");
		assertEquals2("e", "è");
		assertEquals2("e", "é");
		assertEquals2("e", "ê");
		assertEquals2("e", "ë");
		assertEquals2("i", "ì");
		assertEquals2("i", "í");
		assertEquals2("i", "î");
		assertEquals2("i", "ï");
		assertEquals2("d", "ð");
		assertEquals2("n", "ñ");
		assertEquals2("o", "ò");
		assertEquals2("o", "ó");
		assertEquals2("o", "ô");
		assertEquals2("o", "õ");
		assertEquals2("o", "ö");
		assertEquals2("o", "ø");
		assertEquals2("u", "ù");
		assertEquals2("u", "ú");
		assertEquals2("u", "û");
		assertEquals2("u", "ü");
		assertEquals2("y", "ý");
		assertEquals2("th", "þ");
		assertEquals2("y", "ÿ");
		assertEquals2("A", "Ā");
		assertEquals2("a", "ā");
		assertEquals2("A", "Ă");
		assertEquals2("a", "ă");
		assertEquals2("A", "Ą");
		assertEquals2("a", "ą");
		assertEquals2("C", "Ć");
		assertEquals2("c", "ć");
		assertEquals2("C", "Ĉ");
		assertEquals2("c", "ĉ");
		assertEquals2("C", "Ċ");
		assertEquals2("c", "ċ");
		assertEquals2("C", "Č");
		assertEquals2("c", "č");
		assertEquals2("D", "Ď");
		assertEquals2("d", "ď");
		assertEquals2("D", "Đ");
		assertEquals2("d", "đ");
		assertEquals2("E", "Ē");
		assertEquals2("e", "ē");
		assertEquals2("E", "Ĕ");
		assertEquals2("e", "ĕ");
		assertEquals2("E", "Ė");
		assertEquals2("e", "ė");
		assertEquals2("E", "Ę");
		assertEquals2("e", "ę");
		assertEquals2("E", "Ě");
		assertEquals2("e", "ě");
		assertEquals2("G", "Ĝ");
		assertEquals2("g", "ĝ");
		assertEquals2("G", "Ğ");
		assertEquals2("g", "ğ");
		assertEquals2("G", "Ġ");
		assertEquals2("g", "ġ");
		assertEquals2("G", "Ģ");
		assertEquals2("g", "ģ");
		assertEquals2("H", "Ĥ");
		assertEquals2("h", "ĥ");
		assertEquals2("H", "Ħ");
		assertEquals2("h", "ħ");
		assertEquals2("I", "Ĩ");
		assertEquals2("i", "ĩ");
		assertEquals2("I", "Ī");
		assertEquals2("i", "ī");
		assertEquals2("I", "Ĭ");
		assertEquals2("i", "ĭ");
		assertEquals2("I", "Į");
		assertEquals2("i", "į");
		assertEquals2("I", "İ");
		assertEquals2("i", "ı");
		assertEquals2("IJ", "Ĳ");
		assertEquals2("ij", "ĳ");
		assertEquals2("J", "Ĵ");
		assertEquals2("j", "ĵ");
		assertEquals2("K", "Ķ");
		assertEquals2("k", "ķ");
		assertEquals2("q", "ĸ");
		assertEquals2("L", "Ĺ");
		assertEquals2("l", "ĺ");
		assertEquals2("L", "Ļ");
		assertEquals2("l", "ļ");
		assertEquals2("L", "Ľ");
		assertEquals2("l", "ľ");
		assertEquals2("L", "Ŀ");
		assertEquals2("l", "ŀ");
		assertEquals2("L", "Ł");
		assertEquals2("l", "ł");
		assertEquals2("N", "Ń");
		assertEquals2("n", "ń");
		assertEquals2("N", "Ņ");
		assertEquals2("n", "ņ");
		assertEquals2("N", "Ň");
		assertEquals2("n", "ň");
		assertEquals2("n", "ŉ");
		assertEquals2("N", "Ŋ");
		assertEquals2("n", "ŋ");
		assertEquals2("O", "Ō");
		assertEquals2("o", "ō");
		assertEquals2("O", "Ŏ");
		assertEquals2("o", "ŏ");
		assertEquals2("O", "Ő");
		assertEquals2("o", "ő");
		assertEquals2("OE", "Œ");
		assertEquals2("oe", "œ");
		assertEquals2("R", "Ŕ");
		assertEquals2("r", "ŕ");
		assertEquals2("R", "Ŗ");
		assertEquals2("r", "ŗ");
		assertEquals2("R", "Ř");
		assertEquals2("r", "ř");
		assertEquals2("S", "Ś");
		assertEquals2("s", "ś");
		assertEquals2("S", "Ŝ");
		assertEquals2("s", "ŝ");
		assertEquals2("S", "Ş");
		assertEquals2("s", "ş");
		assertEquals2("S", "Š");
		assertEquals2("s", "š");
		assertEquals2("T", "Ţ");
		assertEquals2("t", "ţ");
		assertEquals2("T", "Ť");
		assertEquals2("t", "ť");
		assertEquals2("T", "Ŧ");
		assertEquals2("t", "ŧ");
		assertEquals2("U", "Ũ");
		assertEquals2("u", "ũ");
		assertEquals2("U", "Ū");
		assertEquals2("u", "ū");
		assertEquals2("U", "Ŭ");
		assertEquals2("u", "ŭ");
		assertEquals2("U", "Ů");
		assertEquals2("u", "ů");
		assertEquals2("U", "Ű");
		assertEquals2("u", "ű");
		assertEquals2("U", "Ų");
		assertEquals2("u", "ų");
		assertEquals2("W", "Ŵ");
		assertEquals2("w", "ŵ");
		assertEquals2("Y", "Ŷ");
		assertEquals2("y", "ŷ");
		assertEquals2("Y", "Ÿ");
		assertEquals2("Z", "Ź");
		assertEquals2("z", "ź");
		assertEquals2("Z", "Ż");
		assertEquals2("z", "ż");
		assertEquals2("Z", "Ž");
		assertEquals2("z", "ž");
		assertEquals2("s", "ſ");
		fail2();
	}

	private void assertEquals2(String expected, String input) {
		String s = action.transform(input);
		if (!expected.equals(s)) {
			System.err.println("\n" + "\t\t\tcase '" + input + "':\n" + "\t\t\t\tsb.append(\"" + expected + "\");\n"
					+ "\t\t\t\tbreak;");
			fail = true;
		}
	}

	private void fail2() {
		if (fail) {
			fail();
		}
	}

}
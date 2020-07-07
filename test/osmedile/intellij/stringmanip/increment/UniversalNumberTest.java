package osmedile.intellij.stringmanip.increment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UniversalNumberTest {

	@Test
	public void increment() {
		check("-1", "0");
		check("-2", "-1");
		check("-09", "-08");
		check("-10", "-9");
		check("-010", "-009");
		check("-10", "-9");

		check("-1,1", "-1,0");
		check("-2,0", "-1,9");

		check("-1.2", "-1.1");

		check("-100 001", "-100 000");


		check("0", "1");
		check("1", "2");
		check("08", "09");
		check("09", "10");
		check("009", "010");
		check("0009", "0010");
		check("9", "10");

		check("1,0", "1,1");
		check("1,9", "2,0");

		check("1.1", "1.2");
		check("1.9", "2.0");

		check("100 000", "100 001");
		check("999 999", "1 000 000");
	}

	@Test
	public void decrement() {
		checkDecrement("+0", "-1");
		checkDecrement("-0", "-1");
		checkDecrement("-1", "-2");
		checkDecrement("-08", "-09");
		checkDecrement("-09", "-10");
		checkDecrement("-009", "-010");
		checkDecrement("-9", "-10");

		checkDecrement("-1,0", "-1,1");
		checkDecrement("-1,9", "-2,0");

		checkDecrement("-1.1", "-1.2");

		checkDecrement("-100 000", "-100 001");


		checkDecrement("0", "-1");
		checkDecrement("0.0", "-0.1");
		checkDecrement("0.00", "-0.01");
		checkDecrement("1", "0");
		checkDecrement("9", "8");
		checkDecrement("08", "07");
		checkDecrement("008", "007");
		checkDecrement("10", "9");

		checkDecrement("1,0", "0,9");
		checkDecrement("2,0", "1,9");

		checkDecrement("1.1", "1.0");
		checkDecrement("1.9", "1.8");

		checkDecrement("100 000", "99 999");
		checkDecrement("1 000 000", "999 999");
	}

	private void checkDecrement(String input, String expected) {
		UniversalNumber universalNumber = new UniversalNumber(input);
		assertEquals(expected, universalNumber.decrement());
	}

	public void check(String input, String expected) {
		UniversalNumber universalNumber = new UniversalNumber(input);
		assertEquals(expected, universalNumber.increment());
	}
}
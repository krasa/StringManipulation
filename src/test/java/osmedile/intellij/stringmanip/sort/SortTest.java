package osmedile.intellij.stringmanip.sort;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortLines;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortTest {

	//@formatter:off
	private static final String[] INPUT_1 = new String[]{
		"Item 2",
		"Item 02",
		"Item 01",
		"Item 1",
		"Foo",
		"Foo 10",
		"Foo 1",
		"Item 3",
		"Ultra-Foo"};
//@formatter:on

	@Test
	public void test0() throws Exception {
		String[] input = {"a", "A"};
		String[] result = {"A", "a"};
		assertSort(input, Sort.CASE_SENSITIVE_A_Z, result);
	}

	@Test
	public void test1() throws Exception {
//@formatter:off
		String[] result = new String[]{
			"Foo",
			"Foo 1",
			"Foo 10",
			"Item 1",
			"Item 01",
			"Item 2",
			"Item 02",
			"Item 3",
			"Ultra-Foo"};
//@formatter:on
		assertSort(INPUT_1, Sort.CASE_SENSITIVE_A_Z, result);
	}

	@Test
	public void test1R() throws Exception {
//@formatter:off
		String[] result = new String[]{
			"Ultra-Foo",
			"Item 3",
			"Item 02",
			"Item 2",
			"Item 01",
			"Item 1",
			"Foo 10",
			"Foo 1",
			"Foo",
		};
//@formatter:on
		assertSort(INPUT_1, Sort.CASE_SENSITIVE_Z_A, result);
	}

	@Test
	public void test1L() throws Exception {
//@formatter:off
		String[] result = new String[]{
			"Foo",
			"Foo 1",
			"Item 2",
			"Item 1",
			"Foo 10",
			"Item 3",
			"Item 02",
			"Item 01",
			"Ultra-Foo",
		};
//@formatter:on
		assertSort(INPUT_1, Sort.LINE_LENGTH_SHORT_LONG, result);
	}

	@Test
	public void test1LR() throws Exception {
//@formatter:off
		String[] result = new String[]{
			"Ultra-Foo",
			"Item 02",
			"Item 01",
			"Item 2",
			"Item 1",
			"Foo 10",
			"Item 3",
			"Foo 1",
			"Foo",
		};
//@formatter:on
		assertSort(INPUT_1, Sort.LINE_LENGTH_LONG_SHORT, result);
	}

	@Test
	public void testSpaces() throws Exception {
//@formatter:off
		String[] input = new String[]{
			"Theme",
			"The a",
			"The x",
		};
		String[] result = new String[]{
			"The a",
			"The x",
			"Theme",
		};
		String[] result2 = new String[]{
			"Theme",
			"The x",
			"The a",
		};
		String[] inputTab = new String[]{
			"Theme",
			"The\ta",
			"The\tx",
		};
		String[] resultTab = new String[]{
			"The\ta",
			"The\tx",
			"Theme",
		};
		String[] inputHyphen = new String[]{
			"Theme",
			"The-a",
			"The-x",
		};
		String[] resultHyphen = new String[]{
			"The-a",
			"The-x",
			"Theme",
		};
//@formatter:on

		assertSort(inputHyphen, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), resultHyphen);
		assertSort(inputHyphen, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NATURAL), resultHyphen);
		assertSort(inputHyphen, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NORMAL), resultHyphen);
		assertSort(inputHyphen, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), resultHyphen);
		assertSort(inputHyphen, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NATURAL), resultHyphen);
		assertSort(inputHyphen, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NORMAL), resultHyphen);

		assertSort(inputTab, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), resultTab);
		assertSort(inputTab, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NATURAL), resultTab);
		assertSort(inputTab, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NORMAL), resultTab);
		assertSort(inputTab, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), resultTab);
		assertSort(inputTab, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NATURAL), resultTab);
		assertSort(inputTab, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NORMAL), resultTab);

		assertSort(input, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), result);
		assertSort(input, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NATURAL), result);
		assertSort(input, new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NORMAL), result);
		assertSort(input, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), result);
		assertSort(input, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NATURAL), result);
		assertSort(input, new SortSettings(Sort.CASE_INSENSITIVE_A_Z).comparator(SortSettings.BaseComparator.NORMAL), result);

		assertSort(input, new SortSettings(Sort.CASE_SENSITIVE_Z_A).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), result2);
		assertSort(input, new SortSettings(Sort.CASE_SENSITIVE_Z_A).comparator(SortSettings.BaseComparator.NATURAL), result2);
		assertSort(input, new SortSettings(Sort.CASE_SENSITIVE_Z_A).comparator(SortSettings.BaseComparator.NORMAL), result2);
		assertSort(input, new SortSettings(Sort.CASE_INSENSITIVE_Z_A).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR), result2);
		assertSort(input, new SortSettings(Sort.CASE_INSENSITIVE_Z_A).comparator(SortSettings.BaseComparator.NATURAL), result2);
		assertSort(input, new SortSettings(Sort.CASE_INSENSITIVE_Z_A).comparator(SortSettings.BaseComparator.NORMAL), result2);
	}

	@Test
	public void testCollator() throws Exception {
		Character[] cs = new Character[]{
				0x0009,      //	9	Horizontal tab	HT
//			0x000A,       //	10	Line feed	LF
				0x000B,      //	11	Vertical tab	VT
				0x000C,      //	12	Form feed	FF
				0x000D,       //	13	Carriage return	CR

				0x0020,    //	 	32	Space	0001
				0x0021,    //	!	33	Exclamation mark	0002
				0x0022,    //	"	34	Quotation mark	0003
				0x0023,    //	#	35	Number sign, Hashtag, Octothorpe, Sharp	0004
				0x0024,    //	$	36	Dollar sign	0005
				0x0025,    //	%	37	Percent sign	0006
				0x0026,    //	&	38	Ampersand	0007
				0x0027,    //	'	39	Apostrophe	0008
				0x0028,    //	(	40	Left parenthesis	0009
				0x0029,    //	)	41	Right parenthesis	0010
				0x002A,    //	*	42	Asterisk	0011
				0x002B,    //	+	43	Plus sign	0012
				0x002C,    //	,	44	Comma	0013
				0x002D,    //	-	45	Hyphen-minus	0014
				0x002E,    //	.	46	Full stop	0015
				0x002F,    //	/	47	Slash (Solidus)	0016

				0x003A,    // :	58	Colon	0027
				0x003B,    // ;	59	Semicolon	0028
				0x003C,    // <	60	Less-than sign	0029
				0x003D,    // =	61	Equal sign	0030
				0x003E,    // >	62	Greater-than sign	0031
				0x003F,    // ?	63	Question mark	0032
				0x0040,    // @	64	At sign	0033

				0x005B,    // [	91	Left Square Bracket	0060
				0x005C,    // \	92	Backslash	0061
				0x005D,    // ]	93	Right Square Bracket	0062
				0x005E,    // ^	94	Circumflex accent	0063
				0x005F,    // _	95	Low line	0064
				0x0060,    // `	96	Grave accent	0065

				0x007B,    //{	123	Left Curly Bracket	0092
				0x007C,    //|	124	Vertical bar	0093
				0x007D,    //}	125	Right Curly Bracket	0094
				0x007E    //~	126	Tilde	0095
		};
		for (Character c : cs) {
			String[] input = new String[]{
					"Theme",
					"The" + c + "a",
					"The" + c + "x",
			};
			String[] result = new String[]{
					"The" + c + "a",
					"The" + c + "x",
					"Theme",
			};
			//takes too long
//			Locale[] availableLocales = Locale.getAvailableLocales();
//			for (Locale availableLocale : availableLocales) {
//				String collatorLanguageTag = availableLocale.toLanguageTag();
			String collatorLanguageTag = "en_US";


			try {
				SortSettings sortSettings = new SortSettings(Sort.CASE_SENSITIVE_A_Z).comparator(SortSettings.BaseComparator.LOCALE_COLLATOR).collatorLanguageTag(collatorLanguageTag);
				assertSort(input, sortSettings, result);
			} catch (AssertionError e) {
				System.out.println(Integer.toHexString((int) c));
				throw e;
			}
//			}
		}
	}

	@Test
	public void test2() throws Exception {

//@formatter:off
		String[] input = new String[]{
			"Xiph Xlater 10000",
			"Xiph Xlater 5000",
			"Xiph Xlater 2000",
			"Xiph Xlater 500",
			"Xiph Xlater 300",
			"Xiph Xlater 58",
			"Xiph Xlater 50",
			"Xiph Xlater 40",
			"Xiph Xlater 5",
			"Callisto Morphamax 7000",
			"Callisto Morphamax 6000 SE2",
			"Callisto Morphamax 6000 SE",
			"Callisto Morphamax 5000",
			"Callisto Morphamax 700",
			"Callisto Morphamax 600",
			"Callisto Morphamax 500",
			"Callisto Morphamax",
			"Alpha 200",
			"Alpha 100",
			"Alpha 2A-8000",
			"Alpha 2A-900",
			"Alpha 2A",
			"Alpha 2",
			"Allegia 500 Clasteron",
			"Allegia 51 Clasteron",
			"Allegia 50 Clasteron",
			"Allegia 50B Clasteron",
			"Allegia 6R Clasteron",
			"1000X Radonius Maximus",
			"200X Radonius",
			"40X Radonius",
			"30X Radonius",
			"20X Radonius Prime",
			"20X Radonius",
			"10X Radonius",

		};

		String[] result = new String[]{
			"10X Radonius",
			"20X Radonius",
			"20X Radonius Prime",
			"30X Radonius",
			"40X Radonius",
			"200X Radonius",
			"1000X Radonius Maximus",
			"Allegia 6R Clasteron",
			"Allegia 50 Clasteron",
			"Allegia 50B Clasteron",
			"Allegia 51 Clasteron",
			"Allegia 500 Clasteron",
			"Alpha 2",
			"Alpha 2A",
			"Alpha 2A-900",
			"Alpha 2A-8000",
			"Alpha 100",
			"Alpha 200",
			"Callisto Morphamax",
			"Callisto Morphamax 500",
			"Callisto Morphamax 600",
			"Callisto Morphamax 700",
			"Callisto Morphamax 5000",
			"Callisto Morphamax 6000 SE",
			"Callisto Morphamax 6000 SE2",
			"Callisto Morphamax 7000",
			"Xiph Xlater 5",
			"Xiph Xlater 40",
			"Xiph Xlater 50",
			"Xiph Xlater 58",
			"Xiph Xlater 300",
			"Xiph Xlater 500",
			"Xiph Xlater 2000",
			"Xiph Xlater 5000",
			"Xiph Xlater 10000",
		};
//@formatter:on
		assertSort(scramble(input), Sort.CASE_SENSITIVE_A_Z, result);

	}

	@Test
	public void test3() throws Exception {
//@formatter:off
		String[] input = new String[]{
			"z102.doc",
			"z101.doc",
			"z100.doc",
			"z20.doc",
			"z19.doc",
			"z18.doc",
			"z17.doc",
			"z16.doc",
			"z15.doc",
			"z14.doc",
			"z13.doc",
			"z12.doc",
			"z11.doc",
			"z10.doc",
			"z9.doc",
			"z8.doc",
			"z7.doc",
			"z6.doc",
			"z5.doc",
			"z4.doc",
			"z3.doc",
			"z2.doc",
			"z1.doc",
		};

		String[] result = new String[]{
			"z1.doc",
			"z2.doc",
			"z3.doc",
			"z4.doc",
			"z5.doc",
			"z6.doc",
			"z7.doc",
			"z8.doc",
			"z9.doc",
			"z10.doc",
			"z11.doc",
			"z12.doc",
			"z13.doc",
			"z14.doc",
			"z15.doc",
			"z16.doc",
			"z17.doc",
			"z18.doc",
			"z19.doc",
			"z20.doc",
			"z100.doc",
			"z101.doc",
			"z102.doc",
		};

		;
//@formatter:on

		assertSort(scramble(input), Sort.CASE_SENSITIVE_A_Z, result);
	}

	@Test
	public void test4() throws Exception {
		assertSort(new String[]{"a", "A",}, Sort.CASE_SENSITIVE_A_Z, "A", "a");
		assertSort(new String[]{"a", "A",}, Sort.CASE_INSENSITIVE_A_Z, "a", "A");
		assertSort(new String[]{"b", "A", "a",}, Sort.CASE_SENSITIVE_Z_A, "b", "a", "A");
		assertSort(new String[]{"a", "A", "b"}, Sort.CASE_INSENSITIVE_Z_A, "b", "a", "A");
	}

	@Test
	public void testHexa() throws Exception {
		assertSort(new String[]{"9", "A",}, Sort.HEXA, "9", "A");
		assertSort(new String[]{"0x9", "A",}, Sort.HEXA, "0x9", "A");
		assertSort(new String[]{"9", "0xA",}, Sort.HEXA, "9", "0xA");
		assertSort(new String[]{"9", "a",}, Sort.HEXA, "9", "a");
		assertSort(new String[]{"a", "f",}, Sort.HEXA, "a", "f");
		try {
			assertSort(new String[]{"a", "g",}, Sort.HEXA, "f", "a");
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		assertSort(new String[]{"10", "a",}, Sort.HEXA, "a", "10");
	}

	@NotNull
	private static String[] scramble(String[] input) {
		List<String> inputList = Arrays.asList(input);
		Collections.shuffle(inputList);
		return inputList.toArray(input);
	}

	private void assertSort(String[] input, Sort sort, String... result) {
		assertSort(input, SortSettings.allFeaturesDisabled(sort), result);
	}

	private void assertSort(String[] input, SortSettings sortSettings, String... result) {
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(input));
		List<String> sorted = new SortLines(null, list, sortSettings).sortLines();
		Assert.assertArrayEquals(StringUtils.join(sorted.toArray(), '\n') + "\n", result, sorted.toArray());
	}

}
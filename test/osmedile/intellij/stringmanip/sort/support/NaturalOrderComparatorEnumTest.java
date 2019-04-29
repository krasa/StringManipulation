package osmedile.intellij.stringmanip.sort.support;

import org.junit.Assert;
import org.junit.Test;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NaturalOrderComparatorEnumTest {
	private static final NaturalOrderComparator C = new NaturalOrderComparator();

	@Test
	public void test1() throws Exception {
		String[] input = new String[]{"01", "1",};
		String[] result = new String[]{"01", "1",};
		assertSort(input, result);

		input = new String[]{"1", "01",};
		result = new String[]{"1", "01"};
		assertSort(input, result);
	}

	@Test
	public void test2() throws Exception {
		String[] input = new String[]{"Allegia 50 001", "Allegia 50 000",};
		String[] result = new String[]{"Allegia 50 000", "Allegia 50 001",};
		assertSort(input, result);
	}

	@Test
	public void test3() throws Exception {
		String[] input = new String[]{"Allegia 50001", "Allegia 50000",};
		String[] result = new String[]{"Allegia 50000", "Allegia 50001",};
		assertSort(input, result);
	}

	@Test
	public void test4() throws Exception {
		String[] input = new String[]{"50B x", "50 x",};
		String[] result = new String[]{"50 x", "50B x",};
		assertSort(input, result);
	}

	@Test
	public void test4a() throws Exception {
		String[] input = new String[]{"Allegia 50B Clasteron", "Allegia 50 Clasteron",};
		String[] result = new String[]{"Allegia 50 Clasteron", "Allegia 50B Clasteron",};
		assertSort(input, result);
	}

	@Test
	public void test5() throws Exception {
		String[] input = new String[]{"_1_", "_0_",};
		String[] result = new String[]{"_0_", "_1_"};
		assertSort(input, result);
	}

	@Test
	public void doNotIgnoreLeadingSpace() throws Exception {
		String[] input = new String[]{" a", "  c",};
		String[] result = new String[]{"  c", " a",};
		assertSort(input, result);
	}

	@Test
	public void doNotIgnoreLeadingSpace2() throws Exception {
		String[] input = new String[]{"a", " b", "c",};
		String[] result = new String[]{" b", "a", "c",};
		assertSort(input, result);
	}

	@Test
	public void doNotIgnoreLeadingTabAndSpace() throws Exception {
		String[] input = new String[]{"a", "\tb", " c",};
		String[] result = new String[]{"\tb", " c", "a"};
		assertSort(input, result);
	}

	@Test
	public void origTest() throws Exception {
		String[] strings = new String[]{"1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic01", "pic2", "pic02",
				"pic02a", "pic3", "pic4", "pic 4 else", "pic 5", "pic05", "pic 5", "pic 5 something", "pic 6",
				"pic   7", "pic100", "pic100a", "pic120", "pic121", "pic02000", "tom", "x2-g8", "x2-y7", "x2-y08",
				"x8-y8"};

		List orig = Arrays.asList(strings);

		System.out.println("Original: " + orig);

		List scrambled = Arrays.asList(strings);
		Collections.shuffle(scrambled);

		System.out.println("Scrambled: " + scrambled);

		Collections.sort(scrambled, new NaturalOrderComparator());

		System.out.println("Sorted: " + scrambled);
		System.out.println("Sorted: \n" + StringUtils.join(scrambled.toArray(), '\n'));
	}

	private void assertSort(String[] input, String[] result) {
		List<String> list = Arrays.asList(input);
		Collections.sort(list, C);
		Assert.assertArrayEquals(StringUtils.join(list.toArray(), '\n'), result, list.toArray());
	}
}
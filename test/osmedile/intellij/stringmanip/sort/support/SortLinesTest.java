package osmedile.intellij.stringmanip.sort.support;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static osmedile.intellij.stringmanip.sort.support.SortSettings.allFeaturesDisabled;

public class SortLinesTest {
	private List<String> input = Arrays.asList("ccc;;", "aa,", "b");
	private List<String> expected = Arrays.asList("aa,", "b", "ccc;;");
	private List<String> expected_preserveTrailing = Arrays.asList("aa;;", "b,", "ccc");

	private List<String> inputWithLeadingSpace = Arrays.asList("  c,", " a", "b;");
	private List<String> expectedWithLeadingSpace = Arrays.asList("  c,", " a", "b;");
	private List<String> expectedWithLeadingSpace_ignoreLeading = Arrays.asList(" a", "b;", "  c,");
	private List<String> expectedWithLeadingSpace_ignoreLeading_preserveLeading = Arrays.asList("  a", " b;", "c,");
	private List<String> expectedWithLeadingSpace_preserveLeading_preserveTrailing = Arrays.asList("  a,", " b", "c;");


	@Test
	public void sort_group() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).sortByGroups(true);
		String input = "" +
				"a:1\n" +
				"b:1\n" +
				"c:1\n" +
				"\n" +
				"b:1\n" +
				"c:1\n" +
				"a:1\n";

		String expected = "" +
				"a:1\n" +
				"b:1\n" +
				"c:1\n" +
				"\n" +
				"a:1\n" +
				"b:1\n" +
				"c:1\n";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_group2() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).sortByGroups(true);
		String input = "" +
				"a:1\n" +
				"b:1\n" +
				"c:1\n" +
				"\n" +
				"\n" +
				"\n" +
				"b:1\n" +
				"c:1\n" +
				"a:1\n";

		String expected = "" +
				"a:1\n" +
				"b:1\n" +
				"c:1\n" +
				"\n" +
				"\n" +
				"\n" +
				"a:1\n" +
				"b:1\n" +
				"c:1\n";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true);
		String input = "" +
				"a\n" +
				"  c\n" +
				"  b\n" +
				"c\n" +
				"  14\n" +
				"  3\n" +
				"  4\n" +
				"  12\n" +
				"  a\n" +
				"b\n" +
				"  11\n" +
				"  1\n" +
				"  2\n" +
				"  10\n" +
				"  c\n";


		String expected = "" +
				"a\n" +
				"  b\n" +
				"  c\n" +
				"b\n" +
				"  1\n" +
				"  2\n" +
				"  10\n" +
				"  11\n" +
				"  c\n" +
				"c\n" +
				"  3\n" +
				"  4\n" +
				"  12\n" +
				"  14\n" +
				"  a\n";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree2() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true);
		String input = "" +
				"a2\n" +
				" a2b2\n" +
				"   a2b2c2\n" +
				"   a2b2c1\n" +
				" a2b1\n" +
				"  a2b1c2\n" +
				"  a2b1c1\n" +
				"  \n" +
				"a1\n" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c1\n" +
				"  a1b1c3\n" +
				"  \n" +
				" a1b2\n" +
				"   a1b2c2\n" +
				"   a1b2c1\n" +
				"   a1b2c3\n" +
				"   \n" +
				" a1b3\n" +
				"   a1b3c2\n" +
				"   a1b3c1\n" +
				"   a1b3c3\n";


		String expected = "" +
				"a1\n" +
				" a1b1\n" +
				"  a1b1c1\n" +
				"  a1b1c2\n" +
				"  a1b1c3\n" +
				" a1b2\n" +
				"   a1b2c1\n" +
				"   a1b2c2\n" +
				"   a1b2c3\n" +
				" a1b3\n" +
				"   a1b3c1\n" +
				"   a1b3c2\n" +
				"   a1b3c3\n" +
				"a2\n" +
				" a2b1\n" +
				"  a2b1c1\n" +
				"  a2b1c2\n" +
				" a2b2\n" +
				"   a2b2c1\n" +
				"   a2b2c2\n";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree_group() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true).sortByGroups(true);
		String input = "" +
				"a2\n" +
				" a2b2\n" +
				"   a2b2c2\n" +
				"   a2b2c1\n" +
				" a2b1\n" +
				"  a2b1c2\n" +
				"  a2b1c1\n" +
				"  \n" +
				"a1\n" +
				" a1b2\n" +
				"   a1b2c2\n" +
				"   a1b2c1\n" +
				"   a1b2c3\n" +
				"   \n" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c3\n" +
				"  a1b1c1\n" +
				"  \n" +
				"  a1b1c5\n" +
				"  a1b1c4\n" +
				"  \n" +
				" a1b3\n" +
				"   a1b3c2\n" +
				"   a1b3c1\n" +
				"   a1b3c3\n";


		String expected = "" +
				"a2\n" +
				" a2b1\n" +
				"  a2b1c1\n" +
				"  a2b1c2\n" +
				" a2b2\n" +
				"   a2b2c1\n" +
				"   a2b2c2\n" +
				"  \n" +
				"a1\n" +
				" a1b2\n" +
				"   a1b2c1\n" +
				"   a1b2c2\n" +
				"   a1b2c3\n" +
				"   \n" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c1\n" +
				"  a1b1c3\n" +
				"  \n" +
				"  a1b1c4\n" +
				"  a1b1c5\n" +
				"  \n" +
				" a1b3\n" +
				"   a1b3c1\n" +
				"   a1b3c2\n" +
				"   a1b3c3\n";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree_group2() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true).sortByGroups(true);
		String input = "" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c3\n" +
				"  a1b1c1\n" +
				"";

		String expected = "" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c1\n" +
				"  a1b1c3\n" +
				"";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree_group3() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true).sortByGroups(true);
		String input = "" +
				"  \n" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c3\n" +
				"  a1b1c1\n" +
				"";

		String expected = "" +
				"  \n" +
				" a1b1\n" +
				"  a1b1c2\n" +
				"  \n" +
				"  a1b1c1\n" +
				"  a1b1c3\n" +
				"";
		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree3() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true);
		String input = "" +
				"a2\n" +
				" a2b1\n" +
				"\n" +
				"a1\n" +
				"a3\n";

		String expected = "" +
				"a1\n" +
				"a2\n" +
				" a2b1\n" +
				"a3\n" +
				"";


		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort_tree4() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).hierarchicalSort(true);
		String input = "" +
				"\n" +
				"a2\n" +
				" a2b1\n" +
				"a1\n" +
				"a3\n";

		String expected = "" +
				"a1\n" +
				"a2\n" +
				" a2b1\n" +
				"a3\n" +
				"";


		Assert.assertEquals(expected, new SortLines(input, sortSettings).sort());
	}

	@Test
	public void sort() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z);
		List<String> result = new SortLines(input, sortSettings).sortLines();
		Assert.assertEquals(expected, result);
	}

	@Test
	public void preserveTrailing() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).preserveTrailingSpecialCharacters(true);
		List<String> result = new SortLines(input, sortSettings).sortLines();
		Assert.assertEquals(expected_preserveTrailing, result);
	}

	@Test
	public void ignoreLeadingSpace() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).ignoreLeadingSpaces(true);
		List<String> result = new SortLines(inputWithLeadingSpace, sortSettings).sortLines();
		Assert.assertEquals(expectedWithLeadingSpace_ignoreLeading, result);
	}

	@Test
	public void ignoreLeadingSpace_preserveLeading() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).ignoreLeadingSpaces(true).preserveLeadingSpaces(true);
		List<String> result = new SortLines(inputWithLeadingSpace, sortSettings).sortLines();
		Assert.assertEquals(expectedWithLeadingSpace_ignoreLeading_preserveLeading, result);
	}

	@Test
	public void ignoreLeadingSpace_preserveLeading_preserveTrailing() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).ignoreLeadingSpaces(true).preserveLeadingSpaces(true).preserveTrailingSpecialCharacters(true);
		List<String> result = new SortLines(inputWithLeadingSpace, sortSettings).sortLines();
		Assert.assertEquals("input: " + inputWithLeadingSpace, expectedWithLeadingSpace_preserveLeading_preserveTrailing, result);
	}

	@Test
	public void not_ignoreLeadingSpace() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z);
		List<String> result = new SortLines(inputWithLeadingSpace, sortSettings).sortLines();
		Assert.assertEquals(expectedWithLeadingSpace, result);
	}

	@Test
	public void not_ignoreLeadingSpace_preserveLeading() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).preserveLeadingSpaces(true);
		List<String> result = new SortLines(inputWithLeadingSpace, sortSettings).sortLines();
		Assert.assertEquals(expectedWithLeadingSpace, result);
	}

}
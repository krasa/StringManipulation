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
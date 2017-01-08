package osmedile.intellij.stringmanip.sort.support;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static osmedile.intellij.stringmanip.sort.support.SortSettings.allFeaturesDisabled;

public class LinesTest {
	private List<String> input = Arrays.asList("ccc;;", "aa,", "b");
	private List<String> expected = Arrays.asList("aa,", "b", "ccc;;");
	private List<String> expected_preserveTrailing = Arrays.asList("aa;;", "b,", "ccc");


	@Test
	public void sort() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z);
		List<String> result = new Lines(input, sortSettings).sortLines();
		Assert.assertEquals(expected, result);
	}

	@Test
	public void preserveTrailing() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).preserveTrailingSpecialCharacters(true);
		List<String> result = new Lines(input, sortSettings).sortLines();
		Assert.assertEquals(expected_preserveTrailing, result);
	}

	@Test
	public void ignoreLeadingSpace() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).ignoreLeadingSpaces(true);
		List<String> result = new Lines(Arrays.asList("  c,", " a", "b;"), sortSettings).sortLines();
		Assert.assertEquals(Arrays.asList(" a", "b;", "  c,"), result);
	}

	@Test
	public void ignoreLeadingSpace_preserveLeading() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).ignoreLeadingSpaces(true).preserveLeadingSpaces(true);
		List<String> result = new Lines(Arrays.asList("  c,", " a", "b;"), sortSettings).sortLines();
		Assert.assertEquals(Arrays.asList("  a", " b;", "c,"), result);
	}

	@Test
	public void ignoreLeadingSpace_preserveLeading_preserveTrailing() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).ignoreLeadingSpaces(true).preserveLeadingSpaces(true).preserveTrailingSpecialCharacters(true);
		List<String> result = new Lines(Arrays.asList("  c,", " a", "b;"), sortSettings).sortLines();
		Assert.assertEquals("input: " + Arrays.asList("  c,", " a", "b;"), Arrays.asList("  a,", " b", "c;"), result);
	}

	@Test
	public void not_ignoreLeadingSpace() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z);
		List<String> result = new Lines(Arrays.asList("  c,", " a", "b;"), sortSettings).sortLines();
		Assert.assertEquals(Arrays.asList("  c,", " a", "b;"), result);
	}

	@Test
	public void not_ignoreLeadingSpace_preserveLeading() throws Exception {
		SortSettings sortSettings = allFeaturesDisabled(Sort.CASE_SENSITIVE_A_Z).preserveLeadingSpaces(true);
		List<String> result = new Lines(Arrays.asList("  c,", " a", "b;"), sortSettings).sortLines();
		Assert.assertEquals(Arrays.asList("  c,", " a", "b;"), result);
	}

}
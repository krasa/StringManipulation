package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Joiner;
import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.sort.support.tree.HierarchicalSortTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonSortTest {

	@Test
	public void test_array() throws Exception {
		String input = HierarchicalSortTest.readFile("array.json");

		SortSettings sortSettings = new SortSettings();
		JsonSort sort = new JsonSort(null, sortSettings);
		String result = sort.sort(input);

		String expected = HierarchicalSortTest.readFile("array_result.json");

		assertEquals(expected, result);
	}

	@Test
	public void test_array2() throws Exception {
		String input = HierarchicalSortTest.readFile("array2.json");

		SortSettings sortSettings = new SortSettings();
		JsonSort sort = new JsonSort(null, sortSettings);
		String result = sort.sort(input);

		String expected = HierarchicalSortTest.readFile("array2_result.json");

		assertEquals(expected, result);
	}

	@Test
	public void test_array2a() throws Exception {
		String input = HierarchicalSortTest.readFile("array2.json");

		SortSettings sortSettings = new SortSettings();
		JsonSort sort = new JsonSort(null, sortSettings);
		List<String> result = sort.sort(List.of(input.split("\n")));
		Assert.assertTrue(result.size() > 1);

		String expected = HierarchicalSortTest.readFile("array2_result.json");

		assertEquals(expected, Joiner.on("\n").join(result));
	}
}
package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Joiner;
import org.junit.Test;
import osmedile.intellij.stringmanip.sort.support.tree.HierarchicalSortTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonSortTest {

	@Test
	public void test_array() throws Exception {
		String input = HierarchicalSortTest.readFile("array.json");

		SortSettings sortSettings = new SortSettings();
		JsonSort sort = new JsonSort(null, List.of(input.split("\n")), sortSettings);
		List<String> result = sort.sort();

		String expected = HierarchicalSortTest.readFile("array_result.json");

		assertEquals(expected, Joiner.on("\n").join(result));
	}

	@Test
	public void test_array2() throws Exception {
		String input = HierarchicalSortTest.readFile("array2.json");

		SortSettings sortSettings = new SortSettings();
		JsonSort sort = new JsonSort(null, List.of(input.split("\n")), sortSettings);
		List<String> result = sort.sort();

		String expected = HierarchicalSortTest.readFile("array2_result.json");

		assertEquals(expected, Joiner.on("\n").join(result));
	}


}
package osmedile.intellij.stringmanip.sort.support.tree;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HierarchicalSortTest {


	@Test
	public void HierarchicalSortTest() throws Exception {
		String input = readFile("HierarchicalSort.yaml");

		SortSettings sortSettings = new SortSettings();
		sortSettings.setHierarchicalSort(true);
		sortSettings.setSortByGroups(true);
		HierarchicalSort hierarchicalSort = new HierarchicalSort(List.of(input.split("\n")), sortSettings);
		List<String> result = hierarchicalSort.sort();

		String expected = readFile("HierarchicalSort_result.yaml");

		assertEquals(expected, Joiner.on("\n").join(result));
	}


	private String readFile(String fileName) {
		try {
			String s = FileUtils.readFileToString(new File("test/osmedile/intellij/stringmanip/sort/data/" + fileName), StandardCharsets.UTF_8);
			String replace = s.replace("\r", "");
			return replace;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
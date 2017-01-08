package osmedile.intellij.stringmanip.sort.support;

import osmedile.intellij.stringmanip.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lines {
	private List<Line> lines = new ArrayList<Line>();
	private boolean endsWith;
	private final SortSettings sortSettings;

	public Lines(String text, SortSettings sortSettings) {
		this.sortSettings = sortSettings;
		this.endsWith = text.endsWith("\n");

		String[] split = text.split("\n");
		List<String> list = Arrays.asList(split);
		for (String s : list) {
			this.lines.add(new Line(s, sortSettings));
		}
	}

	public Lines(List<String> text, SortSettings sortSettings) {
		this.sortSettings = sortSettings;

		for (String s : text) {
			this.lines.add(new Line(s, sortSettings));
		}
	}

	public String sort() {
		List<String> sort = sortLines();
		String join = StringUtils.join(sort.toArray(), '\n');
		if (endsWith) {
			join = join + "\n";
		}
		return join;
	}

	public List<String> sortLines() {
		Sort sortType = sortSettings.getSortType();
		List<Line> lines = sortType.sortLines(new ArrayList<Line>(this.lines));

		List<String> result = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			Line originalLine = this.lines.get(i);
			Line newLine = lines.get(i);
			result.add(originalLine.transformTo(newLine));
		}
		return result;


	}
}

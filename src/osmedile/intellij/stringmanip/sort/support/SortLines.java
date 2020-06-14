package osmedile.intellij.stringmanip.sort.support;

import shaded.org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SortLines {
	private boolean endsWithNewLine;
	private List<String> originalLines;
	private final SortSettings sortSettings;


	public SortLines(String text, SortSettings sortSettings) {
		this.sortSettings = sortSettings;
		this.endsWithNewLine = text.endsWith("\n");

		String[] split = text.split("\n");
		originalLines = Arrays.asList(split);
	}

	public SortLines(List<String> text, SortSettings sortSettings) {
		originalLines = text;
		this.sortSettings = sortSettings;
	}

	public String sort() {
		List<String> sort = sortLines();
		String join = StringUtils.join(sort.toArray(), '\n');
		if (endsWithNewLine) {
			join = join + "\n";
		}
		return join;
	}

	public List<String> sortLines() {
		Map<Integer, String> emptyLines = new TreeMap<Integer, String>();
		List<SortLine> linesToSort = new ArrayList<SortLine>();

		for (int i1 = 0; i1 < originalLines.size(); i1++) {
			String s = originalLines.get(i1);
			if (StringUtils.isBlank(s)) {
				if (sortSettings.emptyLines() == SortSettings.BlankLines.PRESERVE) {
					emptyLines.put(i1, s);
				}
				continue;
			}
			linesToSort.add(new SortLine(s, sortSettings));
		}

		List<SortLine> sortedLines = sortSettings.getSortType().sortLines(linesToSort, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());

		List<String> result = new ArrayList<>();
		for (int i = 0; i < sortedLines.size(); i++) {
			SortLine originalLine = linesToSort.get(i);
			SortLine newLine = sortedLines.get(i);
			result.add(originalLine.transformTo(newLine));
		}

		for (Map.Entry<Integer, String> emptyLine : emptyLines.entrySet()) {
			result.add(emptyLine.getKey(), emptyLine.getValue());
		}
		return result;
	}
}

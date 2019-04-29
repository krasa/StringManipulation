package osmedile.intellij.stringmanip.sort.support;

import shaded.org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SortLines {
	private List<SortLine> lines = new ArrayList<SortLine>();
	private Map<Integer, String> emptyLines = new TreeMap<Integer, String>();
	private boolean endsWith;
	private final SortSettings sortSettings;


	public SortLines(String text, SortSettings sortSettings) {
		this.sortSettings = sortSettings;
		this.endsWith = text.endsWith("\n");

		String[] split = text.split("\n");
		List<String> list = Arrays.asList(split);
		initLines(sortSettings, list);
	}

	protected void initLines(SortSettings sortSettings, List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			if (shaded.org.apache.commons.lang3.StringUtils.isBlank(s)) {
				if (sortSettings.emptyLines() == SortSettings.BlankLines.PRESERVE) {
					emptyLines.put(i, s);
				}
				continue;
			}
			this.lines.add(new SortLine(s, sortSettings));
		}
	}

	public SortLines(List<String> text, SortSettings sortSettings) {
		this.sortSettings = sortSettings;

		initLines(sortSettings, text);
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
		List<SortLine> lines = sortType.sortLines(this.lines, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());

		List<String> result = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			SortLine originalLine = this.lines.get(i);
			SortLine newLine = lines.get(i);
			result.add(originalLine.transformTo(newLine));
		}


		for (Map.Entry<Integer, String> emptyLine : emptyLines.entrySet()) {
			result.add(emptyLine.getKey(), emptyLine.getValue());
		}
		return result;
	}
}

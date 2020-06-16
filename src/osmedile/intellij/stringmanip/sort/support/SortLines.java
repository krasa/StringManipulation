package osmedile.intellij.stringmanip.sort.support;

import org.jetbrains.annotations.NotNull;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

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
		if (sortSettings.isHierarchicalSort()) {
			return new HierarchicalSort(originalLines, sortSettings).sort();
		}

		List<Sortable> originalLines = toSortables(this.originalLines);

		if (sortSettings.isSortByGroups()) {
			List<Sortable> sortables = sortByGroup(originalLines, sortSettings);
			return toStrings(sortables);
		}

		List<Sortable> result = flatSort(originalLines, sortSettings, sortSettings.emptyLines() == SortSettings.BlankLines.PRESERVE);
		return toStrings(result);
	}

	private List<String> toStrings(List<Sortable> result) {
		List<String> strings = new ArrayList<>();
		for (Sortable sortable : result) {
			strings.add(sortable.getText());
		}
		return strings;
	}

	private List<Sortable> toSortables(List<String> originalLines) {
		List<Sortable> sortables = new ArrayList<>();
		for (String originalLine : originalLines) {
			sortables.add(new SortLine(originalLine, sortSettings));
		}
		return sortables;
	}

	@NotNull
	public static <T extends Sortable> List<T> flatSort(List<T> originalLines, SortSettings sortSettings, boolean preserveBlank) {
		Map<Integer, T> emptyLines = new TreeMap<>();
		List<T> linesToSort = new ArrayList<>();

		for (int i1 = 0; i1 < originalLines.size(); i1++) {
			T s = originalLines.get(i1);
			if (StringUtils.isBlank(s.getText())) {
				if (preserveBlank) {
					emptyLines.put(i1, s);
				}
				continue;
			}
			linesToSort.add(s);
		}

		List<T> sortedLines = sortSettings.getSortType().sortLines(linesToSort, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());

		List<T> result = new ArrayList<>();
		for (int i = 0; i < sortedLines.size(); i++) {
			T originalLine = linesToSort.get(i);
			T newLine = sortedLines.get(i);
			if (originalLine instanceof SortLine && newLine instanceof SortLine) {
				//preserve leading/trailing space
				result.add((T) ((SortLine) originalLine).transformTo((SortLine) newLine));
			} else {
				result.add(newLine);
			}
		}

		for (Map.Entry<Integer, T> emptyLine : emptyLines.entrySet()) {
			result.add(emptyLine.getKey(), emptyLine.getValue());
		}
		return result;
	}

	public static <T extends Sortable> List<T> sortByGroup(List<T> originalLines, SortSettings sortSettings) {
		String levelRegexp = sortSettings.getLevelRegex();
		Pattern compile = Pattern.compile(levelRegexp);
		boolean sortByLevel = true;
		List<T> result = new ArrayList<>();

		int from = 0;
		while (from < originalLines.size()) {
			Map<Integer, T> emptyLines = new TreeMap<>();
			List<T> linesToSort = new ArrayList<>();
			int prevLevel = -1;
			int level;

			for (int i1 = from; i1 < originalLines.size(); i1++) {

				T s = originalLines.get(i1);
				String text = s.getText();
				level = HierarchicalSort.level(text, compile);
				if (sortByLevel && prevLevel != -1 && prevLevel != level) {
					break;
				}
				if (StringUtils.isBlank(text)) {
					if (sortSettings.emptyLines() == SortSettings.BlankLines.PRESERVE || sortByLevel) {
						from++;
						emptyLines.put(i1, s);
					}
					if (sortByLevel && !linesToSort.isEmpty()) {
						break;
					}
					continue;
				}
				from++;
				linesToSort.add(s);


				prevLevel = level;
			}

			List<T> sortedLines = sortSettings.getSortType().sortLines(linesToSort, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());

			for (int i = 0; i < sortedLines.size(); i++) {
				T originalLine = linesToSort.get(i);
				T newLine = sortedLines.get(i);
				if (originalLine instanceof SortLine && newLine instanceof SortLine) {
					//preserve leading/trailing space
					result.add((T) ((SortLine) originalLine).transformTo((SortLine) newLine));
				} else {
					result.add(newLine);
				}
			}

			for (Map.Entry<Integer, T> emptyLine : emptyLines.entrySet()) {
				result.add(emptyLine.getKey(), emptyLine.getValue());
			}
		}
		return result;
	}

}

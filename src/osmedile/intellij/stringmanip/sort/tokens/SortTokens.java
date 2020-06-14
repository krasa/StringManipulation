package osmedile.intellij.stringmanip.sort.tokens;

import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.sort.support.SimpleSortable;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.utils.StringUtil;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SortTokens {
	private List<TokenLine> lines = new ArrayList<TokenLine>();
	private boolean endsWithNewLine;
	private SortTokensModel model;
	private SortSettings sortSettings;
	private HashSet<String> separators;


	public SortTokens(String text, SortTokensModel model) {
		this(splitLines(text),model );
		this.endsWithNewLine = text.endsWith("\n");
	}

	public SortTokens(List<String> lines, SortTokensModel model) {
		this.model = model;
		sortSettings = model.getSortSettings();
		separators = new HashSet<>(model.getSeparators());
		initLines(model, lines);
	}

	@NotNull
	protected static List<String> splitLines(String text) {
		String[] split = text.split("\n");
		List<String> list = Arrays.asList(split);
		return list;
	}
	protected void initLines(SortTokensModel sortSettings, List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			this.lines.add(new TokenLine(s, sortSettings));
		}
	}


	public String sortText() {
		List<String> sort = sortLines();
		return linesToString(sort);
	}

	@NotNull
	protected String linesToString(List<String> sort) {
		String join = StringUtils.join(sort.toArray(), '\n');
		if (endsWithNewLine) {
			join = join + "\n";
		}
		return join;
	}

	public List<String> sortLines() {
		List<String> result = new ArrayList<>();
		if (model.isProcessEachLineSeparately()) {
			for (TokenLine line : lines) {
				result.add(line.getSortedText());
			}
		} else {
			sortAllTogether(result);
		}

		return result;
	}

	protected void sortAllTogether(List<String> result) {
		List<SimpleSortable> tokens = new ArrayList<>();
		for (TokenLine line : lines) {
			String[] split = line.getSplit();
			for (int i = 0; i < split.length; i++) {
				if (isSeparator(split[i])) {
					continue;
				}
				String token = split[i];
				if (token.trim().length() == 0) {
					continue;
				}
				String textForComparison = token;
				if (sortSettings.isIgnoreLeadingSpaces()) {
					textForComparison = token.substring(StringUtil.indexOfAnyButWhitespace(token));
				}
				tokens.add(new SimpleSortable(token, textForComparison));
			}
		}
		List<SimpleSortable> sorted = sortSettings.getSortType().sortLines(tokens, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());
		Iterator<SimpleSortable> iterator = sorted.iterator();

		for (TokenLine line : lines) {
			result.add(line.replaceTokens(iterator));
		}
	}

	private boolean isSeparator(String s) {
		return separators.contains(s);
	}

}

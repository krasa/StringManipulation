package osmedile.intellij.stringmanip.sort.tokens;

import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.align.FixedStringTokenScanner;
import osmedile.intellij.stringmanip.sort.support.SimpleSortable;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.*;

public class TokenLine {
	private final String originalText;
	private final SortSettings sortSettings;
	private final String[] split;

	private Set<String> separators;
	private SortTokensModel model;

	public TokenLine(String originalText, SortTokensModel model) {
		this.originalText = originalText;
		this.sortSettings = model.getSortSettings();

		separators = new HashSet<>(model.getSeparators());
		this.model = model;
		String[] separators = model.getSeparators().toArray(new String[0]);
		if (separators.length == 1 && separators[0].equals(" ")) {
			split = StringUtil.splitToTokensBySpace(originalText).toArray(new String[0]);
		} else {
			split = FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens(originalText, -1, separators).toArray(new String[0]);
		}
		//should start with token, and end with token
//		if (split.length>1 && split.length % 2 == 0) {
//			throw new IllegalStateException("bug, report this, split.length="+split.length+"; "+split);
//		}
	}


	public String getSortedText() {
		List<String> strings = sortTokens();
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s);
		}
		return sb.toString();
	}

	private List<String> sortTokens() {
		List<SimpleSortable> lines = new ArrayList<>();
		for (int i = 0, splitLength = split.length; i < splitLength; i++) {
			if (isSeparator(split[i])) {
				continue;
			}
			String token = split[i];
			String textForComparison = token;
			if (sortSettings.isIgnoreLeadingSpaces()) {
				textForComparison = token.substring(StringUtil.indexOfAnyButWhitespace(token));
			}
			if (model.isIgnoreEmptyTokens() && token.trim().length() == 0) {
				continue;
			}
			lines.add(new SimpleSortable(token, textForComparison));
		}
		List<SimpleSortable> sorted = sortSettings.getSortType().sortLines(lines, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());
		Iterator<SimpleSortable> sortedIterator = sorted.iterator();

		List<String> strings = replaceTokens2(sortedIterator);

		if (sortedIterator.hasNext()) {
			strings.add(sortedIterator.next().getText());
		}
		return strings;
	}

	private boolean isSeparator(String s) {
		return separators.contains(s);
	}

	@NotNull
	private List<String> replaceTokens2(Iterator<SimpleSortable> sortedIterator) {
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			if (!isSeparator(split[i])) {
				if (model.isIgnoreEmptyTokens() && split[i].trim().length() == 0) {
					strings.add(split[i]);
					continue;
				}

				String resultToken = sortedIterator.next().getText();

//				if (sortSettings.isPreserveLeadingSpaces()) {
				int oldContentStartIndex = StringUtil.indexOfAnyButWhitespace(split[i]);
				int newContentStartIndex = StringUtil.indexOfAnyButWhitespace(resultToken);

				String oldContentLeadingSpaces = split[i].substring(0, oldContentStartIndex);
				String newActualContent = resultToken.substring(newContentStartIndex, resultToken.length());
				resultToken = oldContentLeadingSpaces + newActualContent;
//				}

				strings.add(resultToken);
			} else {
				strings.add(split[i]);
			}
		}
		return strings;
	}

	public String[] getSplit() {
		return split;
	}

	public String replaceTokens(Iterator<SimpleSortable> iterator) {
		List<String> strings = replaceTokens2(iterator);
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s);
		}
		return sb.toString();
	}

}

package osmedile.intellij.stringmanip.sort.support;

import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.isWhiteSpace;

public class SortLine implements Sortable {

	private final String text;
	private final SortSettings sortSettings;
	private final String textForComparison;

	public SortLine(String text, SortSettings sortSettings) {
		this.text = text;
		this.sortSettings = sortSettings;

		this.textForComparison = makeTextForComparison(text, sortSettings);
	}

	public SortLine(String selection) {
		text = selection;
		sortSettings = SortSettings.allFeaturesDisabled(null);

		this.textForComparison = makeTextForComparison(text, sortSettings);
	}

	protected String makeTextForComparison(String text, SortSettings sortSettings) {
		String textForComparison = text;
		if (sortSettings.isIgnoreLeadingSpaces()) {
			textForComparison = textForComparison.substring(StringUtil.indexOfAnyButWhitespace(textForComparison));
		}
		if (sortSettings.isIgnoreLeadingCharactersEnabled()) {
			String ignoreLeadingCharacters = sortSettings.getIgnoreLeadingCharacters();
			Matcher matcher = Pattern.compile(ignoreLeadingCharacters).matcher(textForComparison);
			boolean b = matcher.find();
			if (b) {
				int end = matcher.end();
				if (end > 0) {
					textForComparison = textForComparison.substring(end);
				}
			}
		}
		if (sortSettings.isIgnoreLeadingSpaces()) {
			textForComparison = textForComparison.substring(StringUtil.indexOfAnyButWhitespace(textForComparison));
		}
		if (sortSettings.isPreserveTrailingSpecialCharacters()) {
			int textWithoutTrailingCharsEndIndex = lastIndexOfAnyBut(textForComparison, sortSettings.getTrailingChars());
			textForComparison = textForComparison.substring(0, textWithoutTrailingCharsEndIndex);
		}
		return textForComparison;
	}

	@Override
	public String getTextForComparison() {
		return textForComparison;
	}

	public SortLine transformTo(SortLine to) {
		return new SortLine(transform(text, to.text), sortSettings);
	}

	protected String transform(String from, String to) {
		if (sortSettings.isPreserveLeadingSpaces()) {
			int oldContentStartIndex = StringUtil.indexOfAnyButWhitespace(from);
			int newContentStartIndex = StringUtil.indexOfAnyButWhitespace(to);

			String oldContentLeadingSpaces = from.substring(0, oldContentStartIndex);
			String newActualContent = to.substring(newContentStartIndex, to.length());

			to = oldContentLeadingSpaces + newActualContent;
		}

		if (sortSettings.isPreserveTrailingSpecialCharacters()) {
			int newContentEndIndex = lastIndexOfAnyBut(to, sortSettings.getTrailingChars());
			int oldContentEndIndex = lastIndexOfAnyBut(from, sortSettings.getTrailingChars());

			String newContentWithoutTrailingCharacters = to.substring(0, newContentEndIndex);
			String oldTrailingCharacters = from.substring(oldContentEndIndex);

			to = newContentWithoutTrailingCharacters + oldTrailingCharacters;
		}


		return to;
	}

	protected int lastIndexOfAnyBut(String str, String searchChars) {
		if (!isEmpty(str) && !isEmpty(searchChars)) {
			int lastSearchCharFoundIndex = str.length();
			for (int i = str.length() - 1; i >= 0; i--) {
				boolean isSearchChar = searchChars.indexOf(str.charAt(i)) >= 0;

				if (isSearchChar) {
					lastSearchCharFoundIndex = i;
				} else if (isWhiteSpace(str.charAt(i))) {
					continue;
				} else {
					return lastSearchCharFoundIndex;
				}
			}

			return str.length();
		} else {
			return str.length();
		}
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "SortLine{" +
				"text='" + text + '\'' +
				'}';
	}


}
							
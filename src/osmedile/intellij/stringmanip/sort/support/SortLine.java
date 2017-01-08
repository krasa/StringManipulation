package osmedile.intellij.stringmanip.sort.support;

import org.apache.commons.lang.StringUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;

import static com.intellij.openapi.util.text.StringUtil.*;

public class SortLine {

	private final String text;
	private final SortSettings sortSettings;

	public SortLine(String text, SortSettings sortSettings) {
		this.text = text;
		this.sortSettings = sortSettings;
	}

	public SortLine(String selection) {
		text = selection;
		sortSettings = SortSettings.allFeaturesDisabled(null);
	}

	public String getTextForComparison() {
		String textToComparison = text;
		if (sortSettings.isIgnoreLeadingSpaces()) {
			textToComparison = text.substring(StringUtil.indexOfAnyButWhitespace(text), text.length());
		}
		if (sortSettings.isPreserveTrailingSpecialCharacters()) {
			textToComparison = trimTrailing(textToComparison);
			textToComparison = StringUtils.stripEnd(textToComparison, sortSettings.getTrailingChars());
		}
		return textToComparison;
	}


	public String transformTo(SortLine line) {
		String result = line.text;
		String fromText = text;
		if (sortSettings.isPreserveLeadingSpaces()) {
			int oldContentStartIndex = StringUtil.indexOfAnyButWhitespace(fromText);
			int newContentStartIndex = StringUtil.indexOfAnyButWhitespace(result);
			String start = fromText.substring(0, oldContentStartIndex);
			String end = result.substring(newContentStartIndex, result.length());
			result = start + end;
		}

		if (sortSettings.isPreserveTrailingSpecialCharacters()) {
			int newContentEndIndex = lastIndexOfAnyBut(result, sortSettings.getTrailingChars());
			int oldContentEndIndex = lastIndexOfAnyBut(fromText, sortSettings.getTrailingChars());


			String newContentWithoutTrailingCharacters = result.substring(0, newContentEndIndex);
			String oldTrailingCharacters = fromText.substring(oldContentEndIndex);

			result = newContentWithoutTrailingCharacters + oldTrailingCharacters;
		}


		return result;
	}

	protected int lastIndexOfAnyBut(String str, String searchChars) {
		if (!isEmpty(str) && !isEmpty(searchChars)) {
			int lastSearchCharFoundIndex = str.length();
			for (int i = str.length() - 1; i < str.length(); --i) {
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


}

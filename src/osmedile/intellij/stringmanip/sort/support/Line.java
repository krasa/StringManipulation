package osmedile.intellij.stringmanip.sort.support;

import org.apache.commons.lang.StringUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;

import static com.intellij.openapi.util.text.StringUtil.trimTrailing;

public class Line {

	private final String text;
	private final SortSettings sortSettings;

	public Line(String text, SortSettings sortSettings) {
		this.text = text;
		this.sortSettings = sortSettings;
	}

	public Line(String selection) {
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


	public String transformTo(Line line) {
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
			//lets trim it, it's better for edge cases
			fromText = trimTrailing(fromText);
			result = trimTrailing(result);

			int newContentEndIndex = lastIndexOfAnyBut(result, sortSettings.getTrailingChars());
			int oldContentEndIndex = lastIndexOfAnyBut(fromText, sortSettings.getTrailingChars());


			String newContentWithoutTrailingCharacters = result.substring(0, newContentEndIndex);
			String oldTrailingCharacters = fromText.substring(oldContentEndIndex);

			result = newContentWithoutTrailingCharacters + oldTrailingCharacters;
		}


		return result;
	}

	protected int lastIndexOfAnyBut(String text, String trailingChars) {
		//todo perhaps optimize this shit 
		String reverse = StringUtils.reverse(text);
		int i = StringUtils.indexOfAnyBut(reverse, trailingChars);
		if (i == -1) {
			i = reverse.length();
		}
		return text.length() - i;
	}

}

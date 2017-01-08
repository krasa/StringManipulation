package osmedile.intellij.stringmanip.align;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("Duplicates")
public class ColumnAlignerLine {

	private final String[] split;
	protected boolean endsWithNextLine;
	protected StringBuilder sb = new StringBuilder();
	private int index = 0;
	private boolean hasSeparatorBeforeFirstToken = false;
	private String separator;

	public ColumnAlignerLine(String separator, String textPart, boolean endsWithNextLine) {
		this.endsWithNextLine = endsWithNextLine;
		this.separator = separator;
		if (separator.equals(" ")) {
			split = StringUtils.splitByWholeSeparator(textPart, separator);
		} else {
			split = StringUtils.splitByWholeSeparatorPreserveAllTokens(textPart, separator);
		}
		hasSeparatorBeforeFirstToken = split.length > 0 && split[0].length() == 0;
	}

	public void appendInitialSpace(int initialSeparatorPosition) {
		if (hasToken() && hasSeparatorBeforeFirstToken) {
			int initialSpaces = initialSeparatorPosition - 1; // -1 for empty space which is around separator
			for (int j = 0; j < initialSpaces; j++) {
				sb.append(" ");
			}
		}
	}

	public void appendText() {
		if (hasToken()) {
			sb.append(split[index].trim());
		}
	}

	public void appendSpace(int maxLength) {
		if (hasNextToken()) {
			int appendSpaces = Math.max(0, maxLength - sb.length());
			for (int j = 0; j < appendSpaces; j++) {
				sb.append(" ");
			}
		}
	}

	public void appendSeparator() {
		if (hasNextToken()) {
			if (!separator.equals(" ") && sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(separator);
			if (!separator.equals(" ") && hasNextNotEmptyToken()) {
				sb.append(" ");
			}
		}
	}

	public int resultLength() {
		return sb.length();
	}

	public boolean hasToken() {
		return index < split.length;
	}

	public boolean hasNextToken() {
		return hasToken() && index + 1 < split.length;
	}

	public boolean hasNextNotEmptyToken() {
		return hasToken() && index + 1 < split.length && split[index + 1].length() > 0;
	}

	public void next() {
		index++;
	}

	public int currentTokenLength() {
		int result = -1;
		if (hasToken()) {
			result = split[index].length();
		}
		return result;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}

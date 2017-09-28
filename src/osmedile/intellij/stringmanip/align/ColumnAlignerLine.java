package osmedile.intellij.stringmanip.align;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("Duplicates")
public class ColumnAlignerLine {

	private final String[] split;
	protected StringBuilder sb = new StringBuilder();
	private int index = 0;
	protected boolean endsWithNextLine;
	private boolean hasSeparatorBeforeFirstToken = false;
	private String separator;

	private boolean appendSpaceBeforeSeparator = false;
	private boolean appendSpaceAfterSeparator = false;
	private boolean trimValues = false;
	private boolean trimLines = false;


	public ColumnAlignerLine(ColumnAlignerModel model, String separator, String textPart, boolean endsWithNextLine) {
		this.endsWithNextLine = endsWithNextLine;
		this.separator = separator;
		if (separator.equals(" ")) {
			split = StringUtils.splitByWholeSeparator(textPart, separator);
		} else {
			split = StringUtils.splitByWholeSeparatorPreserveAllTokens(textPart, separator);
		}
		hasSeparatorBeforeFirstToken = split.length > 0 && split[0].length() == 0;

		this.appendSpaceBeforeSeparator = model.isSpaceBefore();
		this.appendSpaceAfterSeparator = model.isSpaceAfter();
		this.trimValues = model.isTrimValues();
		this.trimLines = model.isTrimLines();
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
			String str = split[index];
			if (trimValues) {
				str = str.trim();
			}
			sb.append(str);
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

	public void appendSpaceBeforeSeparator() {
		if (hasNextToken()) {
			if (appendSpaceBeforeSeparator && !separator.equals(" ") && sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
				sb.append(" ");
			}
		}
	}

	public void appendSpaceAfterSeparator() {
		if (hasNextToken()) {
			if (appendSpaceAfterSeparator && !separator.equals(" ") && hasNextNotEmptyToken() && !nextTokenIsStartingWithSpace()) {
				sb.append(" ");
			}
		}
	}

	public void appendSeparator() {
		if (hasNextToken()) {
			sb.append(separator);
		}
	}

	protected boolean nextTokenIsStartingWithSpace() {
		return !trimValues && split[index + 1].startsWith(" ");
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

	@NotNull
	String getString() {
		String e = sb.toString();
		if (trimLines) {
			e = e.trim();
		}
		if (endsWithNextLine) {
			return e + "\n";
		}
		return e;
	}
}

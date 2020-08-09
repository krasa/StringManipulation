package osmedile.intellij.stringmanip.align;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.intellij.openapi.util.text.StringUtil.trimTrailing;

@SuppressWarnings("Duplicates")
public class ColumnAlignerLine {

	private final String[] split;
	protected StringBuilder sb = new StringBuilder();
	private int index = 0;
	private final String originalText;
	protected final String leadingIndent;
	protected boolean endsWithNextLine;
	private boolean hasSeparatorBeforeFirstToken = false;

	private boolean appendSpaceBeforeSeparator = false;
	private boolean appendSpaceAfterSeparator = false;
	private boolean trimValues = false;
	private boolean keepLeadingIndent = false;
	private boolean trimLines = false;
	private Set<String> separators;


	public ColumnAlignerLine(ColumnAlignerModel model, String textPart, boolean endsWithNextLine, String... separator) {
		originalText = textPart;
		this.endsWithNextLine = endsWithNextLine;
		separators = new HashSet<String>(Arrays.asList(separator));
		this.appendSpaceBeforeSeparator = model.isSpaceBeforeSeparator();
		this.appendSpaceAfterSeparator = model.isSpaceAfterSeparator();
		this.trimValues = model.isTrimValues();
		this.trimLines = model.isTrimLines();
		this.keepLeadingIndent = model.isKeepLeadingIndent();

		leadingIndent = leading(textPart);
		if (trimLines) {
			textPart = textPart.trim();
		}
		split = FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens(textPart, model.getMaxSeparatorsPerLineAsInt(), separator).toArray(new String[0]);
		hasSeparatorBeforeFirstToken = split.length > 0 && split[0].length() == 0;
	}

	@NotNull
	public static String leading(@NotNull CharSequence string) {
		int index;
		for (index = 0; index < string.length() && Character.isWhitespace(string.charAt(index)); ++index) {
		}

		return string.subSequence(0, index).toString();
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
			sb.append(currentToken());
		}
	}

	protected String currentToken() {
		String str = split[index];
		if (trimValues) {
			str = str.trim();
		}
		return str;
	}


	public void appendSpace(int maxLength) {
		if (hasToken()) {
			int appendSpaces = Math.max(0, maxLength - sb.length());
			for (int j = 0; j < appendSpaces; j++) {
				sb.append(" ");
			}
		}
	}

	public void appendSpaceBeforeSeparator() {
		if (hasToken()) {
			if (appendSpaceBeforeSeparator && !split[index].equals(" ") && sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
				sb.append(" ");
			}
		}
	}

	public void appendSpaceAfterSeparator() {
		if (hasToken()) {
			if (appendSpaceAfterSeparator && !split[index - 1].equals(" ") && hasNotEmptyToken() && !tokenIsStartingWithSpace()) {
				sb.append(" ");
			}
		}
	}

	public void appendSeparator() {
		if (hasToken()) {
			String str = split[index];
			if (isSeparator(str)) {
				sb.append(str);
			} else {
//bad workaround for incorrect spliting when a space separator ' ' is next to non space separator  AlignToColumnsActionTest.test19
				index--;
			}
		}
	}

	public boolean isSeparator(String str) {
		return separators.contains(str);
	}

	protected boolean tokenIsStartingWithSpace() {
		return !trimValues && split[index].startsWith(" ");
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

	public boolean hasNotEmptyToken() {
		return hasToken() && index < split.length && split[index].length() > 0;
	}

	public void next() {
		index++;
	}

	public int currentTokenLength() {
		int result = -1;
		if (hasToken()) {
			result = currentToken().length();
		}
		return result;
	}

	@Override
	public String toString() {
		return sb.toString() + " [" + sb.length() + "]";
	}

	@NotNull
	String getString() {
		String e = sb.toString();
		if (trimLines) {
			if (keepLeadingIndent) {
				e = trimTrailing(e);
			} else {
				e = e.trim();
			}
		}
		if (StringUtils.isBlank(e)) {
			e = e.trim();
		}
		if (endsWithNextLine) {
			return e + "\n";
		}
		return e;
	}

	@NotNull
	String getOriginalString() {
		String e = originalText;
		if (endsWithNextLine) {
			return e + "\n";
		}
		return e;
	}

	public String getToken(int index) {
		int currentIndex = 0;
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (isSeparator(s)) {
				continue;
			}

			if (currentIndex == index) {
				return s;
			}
			currentIndex++;
		}
		return "";
	}

	public List<String> debugTokens() {
		int currentIndex = 1;
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (isSeparator(s)) {
				continue;
			}
			strings.add(currentIndex + "=" + s);
			currentIndex++;
		}
		return strings;
	}

	public void append(String text) {
		sb.append(text);
	}
}

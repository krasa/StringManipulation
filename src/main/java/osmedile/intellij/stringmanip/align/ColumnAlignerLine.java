package osmedile.intellij.stringmanip.align;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.sort.support.SortLine;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

import java.util.*;

@SuppressWarnings("Duplicates")
public class ColumnAlignerLine {

	private final String[] split;
	protected StringBuilder sb = new StringBuilder();
	private int index = 0;
	@NotNull
	private final ColumnAlignerModel model;
	private final String originalText;
	protected final String leadingIndent;
	protected boolean endsWithNextLine;
	private boolean hasSeparatorBeforeFirstToken = false;

	private Set<String> separators;


	public ColumnAlignerLine(ColumnAlignerModel model, String textPart, boolean endsWithNextLine, String... separator) {
		this.model = model;
		originalText = textPart;
		this.endsWithNextLine = endsWithNextLine;
		separators = new HashSet<String>(Arrays.asList(separator));

		leadingIndent = leading(textPart);
		if (this.model.isTrimLines()) {
			textPart = textPart.trim();
		}
		split = FixedStringTokenScanner.splitToFixedStringTokensAndOtherTokens(textPart, model.getMaxSeparatorsPerLineAsInt(), separator).toArray(new String[0]);

		if (model.isTrimValues()) {
			for (int i = 0, splitLength = split.length; i < splitLength; i++) {
				if (!isSeparator(split[i])) {
					split[i] = split[i].trim();
				}
			}
		}

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

	public void appendText(@Nullable CellAligner cellNumberAligner) {
		if (hasToken()) {
			String str = currentToken();

			if (cellNumberAligner != null) {
				str = cellNumberAligner.align(str);
			}

			sb.append(str);
		}
	}

	protected String currentToken() {
		return split[index];
	}

	public void setCurrentToken(String token) {
		split[index] = token;
	}

	public void appendSpace(int maxLength) {
		if (hasToken()) {
			int appendSpaces = Math.max(0, maxLength - getSbLength());
			for (int j = 0; j < appendSpaces; j++) {

				sb.append(" ");
			}
		}
	}

	protected int getSbLength() {
		if (model.isSbcCaseWorkaround()) {
			String s = sb.toString();
			return sbcReplace(s).length();
		} else {
			return sb.length();
		}
	}


	@NotNull
	private String sbcReplace(String s) {
		return s.replaceAll("[^ \\x00-\\xff]", "xx");
	}

	public void appendSpaceBeforeSeparator() {
//		if (hasToken()) {
		if (model.isSpaceBeforeSeparator() && (hasToken() && isSeparator(split[index]) && !split[index].equals(" ")) && sb.length() > 0) {
			sb.append(" ");
		}
//		}
	}

	public void appendSpaceAfterSeparator() {
		if (hasToken()) {
			if (model.isSpaceAfterSeparator() && !split[index - 1].equals(" ") && hasNotEmptyToken() && !tokenIsStartingWithSpace()) {
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
		return !model.isTrimValues() && split[index].startsWith(" ");
	}

	public int resultLength() {
		return getSbLength();
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
			String s = currentToken();
			if (model.isSbcCaseWorkaround()) {
				s = sbcReplace(s);
			}
			result = s.length();
		}
		return result;
	}

	@Override
	public String toString() {
		return sb.toString() + "[" + sb.length() + "," + getSbLength() + "]";
	}

	@NotNull
	String getString() {
		String e = sb.toString();
//todo conflicst with cellaligner, useless?
//		if (model.isTrimLines()) {
//			if (model.isKeepLeadingIndent()) {
//				e = trimTrailing(e);
//			} else {
//				e = e.trim();
//			}
//		}
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
		SortSettings sortSettings = model.getSortSettings();
		String columnSortOrder = model.getColumnSortOrder();
		List<Integer> columnIndexs = model.validColumnSortOrder();


		int currentIndex = 1;
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (isSeparator(s)) {
				continue;
			}

			strings.add(currentIndex + " = " + s);

			if (columnIndexs.contains(currentIndex)) {
				strings.add(currentIndex + "_SortBy = " + new SortLine(s, sortSettings).getTextForComparison());
			}

			currentIndex++;
		}
		return strings;
	}

	public void append(String text) {
		sb.append(text);
	}


}

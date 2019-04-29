package osmedile.intellij.stringmanip.align;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * customized https://stackoverflow.com/a/26908589/685796
 */
public final class FixedStringTokenScanner {

	/**
	 * Splits the given input into tokens. Each token is either one of the given constant string
	 * tokens or a string consisting of the other characters between the constant tokens.
	 *
	 * @param input             The string to split.
	 * @param fixedStringTokens A list of strings to be recognized as separate tokens.
	 * @return A list of strings, which when concatenated would result in the input string.
	 * Occurrences of the fixed string tokens in the input string are returned as separate
	 * list entries. These entries are reference-equal to the respective fixedStringTokens
	 * entry. Characters which did not match any of the fixed string tokens are concatenated
	 * and returned as list entries at the respective positions in the list. The list does
	 * not contain empty or <code>null</code> entries.
	 */
	public static List<String> splitToFixedStringTokensAndOtherTokens(final String input, final String... fixedStringTokens) {
		return new FixedStringTokenScannerRun(input, fixedStringTokens).splitToFixedStringAndOtherTokens();
	}

	private static class FixedStringTokenScannerRun {

		private final String input;
		private final String[] fixedStringTokens;

		private int scanIx = 0;
		StringBuilder otherContent = new StringBuilder();
		List<String> result = new ArrayList<String>();
		boolean lastTokenSpaceSeparator;

		public FixedStringTokenScannerRun(final String input, final String[] fixedStringTokens) {
			this.input = input;
			this.fixedStringTokens = fixedStringTokens;
		}

		List<String> splitToFixedStringAndOtherTokens() {
			while (scanIx < input.length()) {
				scanIx += matchFixedStringOrAppendToOther();
			}
			storeOtherToken("");
			return result;
		}

		/**
		 * @return the number of matched characters.
		 */
		private int matchFixedStringOrAppendToOther() {
			for (String separator : fixedStringTokens) {
				if (StringUtils.isEmpty(separator)) {
					continue;
				}
				if (input.regionMatches(scanIx, separator, 0, separator.length())) {
					storeOtherToken(separator);
					if (result.size() == 0 && separator.equals(" ")) {
						return separator.length();
					}
					if (!lastTokenSpaceSeparator) {
						result.add(separator); // add string instance so that identity comparison works
						if (separator.equals(" ")) {
							lastTokenSpaceSeparator = true;
						}
					}
					return separator.length();
				}
			}
			appendCharacterToOther();
			return 1;
		}

		private void appendCharacterToOther() {
			otherContent.append(input.substring(scanIx, scanIx + 1));
		}

		private void storeOtherToken(String separator) {
			if (otherContent.length() == 0 && separator.equals(" ")) {
				return;
			}
			result.add(otherContent.toString());
			lastTokenSpaceSeparator = false;
			otherContent.setLength(0);
//			}
		}
	}
}
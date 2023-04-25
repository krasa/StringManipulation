package osmedile.intellij.stringmanip.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Character.isWhitespace;
import static java.lang.Character.*;
import static org.apache.commons.lang3.StringUtils.*;


/**
 * @author Olivier Smedile
 * @version $Id: StringUtil.java 62 2008-04-20 11:11:54Z osmedile $
 */
public class StringUtil {
	public static PluginPersistentStateComponent persistentStateComponent;
	public static final char EMPTY_CHAR = 0;

	public static String removeAllSpace(String s) {
		return join(s.split("\\s"), "").trim();
	}

	public static String trimAllSpace(String s) {
		return join(s.split("\\s+"), " ");
	}

	public static String camelToText(String s) {
		StringBuilder buf = new StringBuilder();
		char lastChar = ' ';
		for (char c : s.toCharArray()) {
			char nc = c;
			if (isUpperCase(nc) && isLowerCase(lastChar)) {
				buf.append(" ");
				nc = Character.toLowerCase(c);
			} else if ((isSeparatorAfterDigit() && isDigit(lastChar) && isLetter(c))
					|| (isSeparatorBeforeDigit() && isDigit(c) && isLetter(lastChar))) {
				if (lastChar != ' ') {
					buf.append(" ");
				}
				nc = Character.toLowerCase(c);
			}

			if (lastChar != ' ' || c != ' ') {
				buf.append(nc);
			}
			lastChar = c;
		}
		return buf.toString();
	}

	/**
	 * inspired by org.apache.commons.text.WordUtils#capitalize
	 */
	public static String capitalizeFirstWord(String str, char[] delimiters) {
		if (isEmpty(str)) {
			return str;
		} else {
			boolean done = false;
			Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
			int strLen = str.length();
			int[] newCodePoints = new int[strLen];
			int outOffset = 0;
			boolean capitalizeNext = true;
			int index = 0;

			while (index < strLen) {
				int codePoint = str.codePointAt(index);
				if (delimiterSet.contains(codePoint)) {
					capitalizeNext = true;
					newCodePoints[outOffset++] = codePoint;
					index += Character.charCount(codePoint);
				} else if (!done && capitalizeNext && isLowerCase(codePoint)) {
					int titleCaseCodePoint = Character.toTitleCase(codePoint);
					newCodePoints[outOffset++] = titleCaseCodePoint;
					index += Character.charCount(titleCaseCodePoint);
					capitalizeNext = false;
					done = true;
				} else {
					newCodePoints[outOffset++] = codePoint;
					index += Character.charCount(codePoint);
				}
			}

			return new String(newCodePoints, 0, outOffset);
		}
	}

	public static String capitalizeFirstWord2(String str) {
		if (isEmpty(str)) {
			return str;
		} else {
			StringBuilder buf = new StringBuilder();
			boolean upperNext = true;
			char[] chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if (isLetter(c) && upperNext) {
					buf.append(toUpperCase(c));
					upperNext = false;
				} else {
					if (!isLetterOrDigit(c)) {
						upperNext = true;
					}
					buf.append(c);
				}

			}

			return buf.toString();
		}
	}

	/**
	 * org.apache.commons.text.WordUtils.generateDelimiterSet
	 */
	public static Set<Integer> generateDelimiterSet(char[] delimiters) {
		Set<Integer> delimiterHashSet = new HashSet();
		if (delimiters != null && delimiters.length != 0) {
			for (int index = 0; index < delimiters.length; ++index) {
				delimiterHashSet.add(Character.codePointAt(delimiters, index));
			}

			return delimiterHashSet;
		} else {
			if (delimiters == null) {
				delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
			}

			return delimiterHashSet;
		}
	}

	public static String toSoftCamelCase(String s) {
		String[] words = s.split("[\\s_]");


		for (int i = 0; i < words.length; i++) {
			words[i] = com.intellij.openapi.util.text.StringUtil.capitalize(words[i]);
		}

		return join(words);
	}


	public static String toCamelCase(String s) {
		String[] words = splitByCharacterTypeCamelCase(s);

		boolean firstWord = true;
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (firstWord && startsWithLetter(word)) {
				words[i] = word.toLowerCase();
				firstWord = false;
				if (i > 1 && isBlank(words[i - 1]) && isAllLetterOrDigit(words[i - 2])) {
					words[i - 1] = "";
				}
			} else if (specialWord(word)) { // multiple camelCases
				firstWord = true;
			} else {
				words[i] = com.intellij.openapi.util.text.StringUtil.capitalize(word.toLowerCase());
				if (i > 1 && isBlank(words[i - 1]) && isAllLetterOrDigit(words[i - 2])) {
					words[i - 1] = "";
				}
			}
		}
		String join = join(words);
		join = replaceSeparatorBetweenLetters(join, '_', EMPTY_CHAR);
		join = replaceSeparatorBetweenLetters(join, '-', EMPTY_CHAR);
		join = replaceSeparatorBetweenLetters(join, '.', EMPTY_CHAR);
		return join;
	}

	private static boolean isAllLetterOrDigit(String word) {
		for (char c : word.toCharArray()) {
			if (!isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private static boolean specialWord(String word) {
		if (isBlank(word)) {
			return false;
		}
		for (char c : word.toCharArray()) {
			if (isDigit(c) || isLetter(c) || isSeparator(c)) {
				return false;
			}
		}
		return true;
	}

	private static boolean startsWithLetter(String word) {
		return word.length() > 0 && isLetter(word.charAt(0));
	}

	private static boolean isNotQuote(String word) {
		return !"\"".equals(word) && !"\'".equals(word);
	}

	public static String wordsToConstantCase(String s) {
		StringBuilder buf = new StringBuilder();

		char lastChar = 'a';
		for (char c : s.toCharArray()) {
			if (isWhitespace(lastChar) && (!isWhitespace(c) && '_' != c) &&
					buf.length() > 0 && buf.charAt(buf.length() - 1) != '_') {
				buf.append("_");
			}
			if (!isWhitespace(c)) {
				buf.append(Character.toUpperCase(c));

			}
			lastChar = c;
		}
		if (isWhitespace(lastChar)) {
			buf.append("_");
		}


		return buf.toString();

	}

	public static String wordsAndHyphenAndCamelToConstantCase(String s) {
//		boolean containsLowerCase = containsLowerCase(s);

		StringBuilder buf = new StringBuilder();
		char previousChar = ' ';
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			boolean isUpperCaseAndPreviousIsUpperCase = isUpperCase(previousChar) && isUpperCase(c);
			boolean isUpperCaseAndPreviousIsLowerCase = isLowerCase(previousChar) && isUpperCase(c);
			// boolean isLowerCaseLetter = !isWhitespace(c) && '_' != c && !isUpperCase(c);
			// boolean isLowerCaseAndPreviousIsWhitespace = isWhitespace(lastChar) && isLowerCaseLetter;
//			boolean previousIsWhitespace = isWhitespace(previousChar);
//			boolean lastOneIsNotUnderscore = buf.length() > 0 && buf.charAt(buf.length() - 1) != '_';
//			boolean isNotUnderscore = c != '_';
			//  ORIGINAL      if (lastOneIsNotUnderscore && (isUpperCase(c) || isLowerCaseAndPreviousIsWhitespace)) {

			//camelCase handling - add extra _
			if (isLetter(c) && isLetter(previousChar) && (isUpperCaseAndPreviousIsLowerCase || (putSeparatorBetweenUppercases() && isUpperCaseAndPreviousIsUpperCase))) {
				buf.append("_");
				// extra _ after number
			} else if ((isSeparatorAfterDigit() && isDigit(previousChar) && isLetter(c))
					|| (isSeparatorBeforeDigit() && isDigit(c) && isLetter(previousChar))) {
				buf.append('_');
			}


			//replace separators by _
			if ((isSeparator(c) || isWhitespace(c)) && isLetterOrDigit(previousChar) && nextIsLetterOrDigit(s, i)) {
				buf.append('_');
			} else {
				buf.append(Character.toUpperCase(c));
			}

			previousChar = c;
		}
//		if (isWhitespace(previousChar)) {
//			buf.append("_");
//		}


		return buf.toString();
	}

	private static boolean putSeparatorBetweenUppercases() {
		if (persistentStateComponent == null) {
			persistentStateComponent = PluginPersistentStateComponent.getInstance();
		}
		return persistentStateComponent.getCaseSwitchingSettings().isPutSeparatorBetweenUpperCases();
	}

	private static boolean betweenLettersOrDigits(char[] chars, int i) {
		for (int j = i; j < chars.length; j++) {
			char aChar = chars[j];
			if (!isLetterOrDigit(aChar) && !isWhitespace(aChar)) {
				return false;
			}
			if (isLetterOrDigit(aChar)) {
				break;
			}
		}
		for (int j = i; j >= 0; j--) {
			char aChar = chars[j];
			if (!isLetterOrDigit(aChar) && !isWhitespace(aChar)) {
				return false;
			}
			if (isLetterOrDigit(aChar)) {
				break;
			}
		}
		return true;
	}

	private static boolean isSlash(char c) {
		return c == '\\' || c == '/';
	}

	private static boolean isNotBorderQuote(char actualChar, int i, char[] chars) {
		if (chars.length - 1 == i) {
			char firstChar = chars[0];
			if (isQuote(actualChar) && isQuote(firstChar)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isQuote(char actualChar) {
		return actualChar == '\'' || actualChar == '\"';
	}

	public static String toDotCase(String s) {
		StringBuilder buf = new StringBuilder();

		char lastChar = ' ';
		for (char c : s.toCharArray()) {
			boolean isUpperCaseAndPreviousIsLowerCase = isLowerCase(lastChar) && isUpperCase(c);
			boolean previousIsWhitespace = isWhitespace(lastChar);
			boolean lastOneIsNotUnderscore = buf.length() > 0 && buf.charAt(buf.length() - 1) != '.';
			if (lastOneIsNotUnderscore && (isUpperCaseAndPreviousIsLowerCase || previousIsWhitespace)) {
				buf.append(".");
			} else if ((isSeparatorAfterDigit() && isDigit(lastChar) && isLetter(c))
					|| (isSeparatorBeforeDigit() && isDigit(c) && isLetter(lastChar))) {
				buf.append(".");
			}

			if (c == '.') {
				buf.append('.');
			} else if (c == '-') {
				buf.append('.');
			} else if (c == '_') {
				buf.append('.');
			} else if (!isWhitespace(c)) {
				buf.append(Character.toLowerCase(c));
			}

			lastChar = c;
		}
		if (isWhitespace(lastChar)) {
			buf.append(".");
		}


		return buf.toString();
	}

	@NotNull
	public static String replaceSeparator(String s1, char s, char s2) {
		return s1.replace(s, s2);
	}

	@NotNull
	public static String replaceSeparatorBetweenLetters(String s, char from, char to) {
		StringBuilder buf = new StringBuilder();
		char lastChar = ' ';
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == from) {
				boolean lastDigit = isDigit(lastChar);
				boolean lastLetterOrDigit = isLetterOrDigit(lastChar);
				boolean nextDigit = nextIsDigit(s, i);
				boolean nextLetterOrDigit = nextIsLetterOrDigit(s, i);

				if (lastDigit && nextDigit) {
					buf.append(c);
				} else if (lastLetterOrDigit && nextLetterOrDigit) {
					if (to != EMPTY_CHAR) {
						buf.append(to);
					}
				} else {
					buf.append(c);
				}
			} else {
				buf.append(c);
			}
			lastChar = c;
		}

		return buf.toString();
	}

	private static boolean nextIsDigit(String s, int i) {
		if (i + 1 >= s.length()) {
			return false;
		} else {
			return Character.isDigit(s.charAt(i + 1));
		}
	}

	private static boolean nextIsLetterOrDigit(String s, int i) {
		if (i + 1 >= s.length()) {
			return false;
		} else {
			return Character.isLetterOrDigit(s.charAt(i + 1));
		}
	}

	private static boolean nextIsLetter(String s, int i) {
		if (i + 1 >= s.length()) {
			return false;
		} else {
			return Character.isLetter(s.charAt(i + 1));
		}
	}

	/**
	 * <p>Splits the given input sequence around matches of this pattern.<p/>
	 * <p/>
	 * <p> The array returned by this method contains each substring of the input sequence
	 * that is terminated by another subsequence that matches this pattern or is terminated by
	 * the end of the input sequence.
	 * The substrings in the array are in the order in which they occur in the input.
	 * If this pattern does not match any subsequence of the input then the resulting array
	 * has just one element, namely the input sequence in string form.<p/>
	 * <p/>
	 * <pre>
	 * splitPreserveAllTokens("boo:and:foo", ":") =  { "boo", ":", "and", ":", "foo"}
	 * splitPreserveAllTokens("boo:and:foo", "o") =  { "b", "o", "o", ":and:f", "o", "o"}
	 * </pre>
	 *
	 * @param input The character sequence to be split
	 * @return The array of strings computed by splitting the input around matches of this pattern
	 */
	public static String[] splitPreserveAllTokens(String input, String regex) {
		int index = 0;
		Pattern p = Pattern.compile(regex);
		ArrayList<String> result = new ArrayList<String>();
		Matcher m = p.matcher(input);

		// Add segments before each match found
		int lastBeforeIdx = 0;
		while (m.find()) {
			if (isNotEmpty(m.group())) {
				String match = input.subSequence(index, m.start()).toString();
				if (isNotEmpty(match)) {
					result.add(match);
				}
				result.add(input.subSequence(m.start(), m.end()).toString());
				index = m.end();
			}
		}

		// If no match was found, return this
		if (index == 0) {
			return new String[]{input};
		}


		final String remaining = input.subSequence(index, input.length()).toString();
		if (isNotEmpty(remaining)) {
			result.add(remaining);
		}

		// Construct result
		return result.toArray(new String[result.size()]);

	}


	public static String nonAsciiToUnicode(String s) {
		StringBuffer sb = new StringBuffer(s.length());
		for (Character c : s.toCharArray()) {
			if (!CharUtils.isAscii(c)) {
				sb.append(CharUtils.unicodeEscaped(c));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}


	public static String escapedUnicodeToString(String s) {
		String[] parts = StringUtil.splitPreserveAllTokens(s, "\\\\u[0-9a-fA-F]{4}");
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].startsWith("\\u")) {
				int v = Integer.parseInt(parts[i].substring(2), 16);
				parts[i] = "" + ((char) v);
			}
		}


		return join(parts);
	}

	public static String wordsToHyphenCase(String s) {
		StringBuilder buf = new StringBuilder();
		char lastChar = 'a';
		for (char c : s.toCharArray()) {
			if (isWhitespace(lastChar) && (!isWhitespace(c) && '-' != c) && buf.length() > 0
					&& buf.charAt(buf.length() - 1) != '-') {
				buf.append("-");
			}
			if ('_' == c) {
				buf.append('-');
			} else if ('.' == c) {
				buf.append('-');
			} else if (!isWhitespace(c)) {
				buf.append(toLowerCase(c));
			}
			lastChar = c;
		}
		if (isWhitespace(lastChar)) {
			buf.append("-");
		}
		return buf.toString();
	}

	public static boolean containsLowerCase(String s) {
		for (char c : s.toCharArray()) {
			if (isLowerCase(c)) {
				return true;
			}
		}
		return false;
	}

	public static int indexOfAnyButWhitespace(String cs) {
		if (isEmpty(cs)) {
			return cs.length();
		}
		final int csLen = cs.length();
		for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			if (isWhitespace(ch)) {
				continue;
			}
			return i;
		}
		return cs.length();
	}

	protected static boolean isSeparatorBeforeDigit() {
		if (persistentStateComponent == null) {
			persistentStateComponent = PluginPersistentStateComponent.getInstance();
		}
		return persistentStateComponent.getCaseSwitchingSettings().isSeparatorBeforeDigit();
	}

	protected static boolean isSeparatorAfterDigit() {
		if (persistentStateComponent == null) {
			persistentStateComponent = PluginPersistentStateComponent.getInstance();
		}
		return persistentStateComponent.getCaseSwitchingSettings().isSeparatorAfterDigit();
	}

	public static String substringUntilSpecialCharacter(String s) {
		int firstLetterOrDigitOrSeparator = -1;
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (!isLetterOrDigit(c) && !isWhitespace(c) && !isSeparator(c) && firstLetterOrDigitOrSeparator != -1) {
				return s.substring(firstLetterOrDigitOrSeparator, i);
			}
			if (isLetterOrDigit(c) || isWhitespace(c) || isSeparator(c)) {
				if (firstLetterOrDigitOrSeparator == -1) {
					firstLetterOrDigitOrSeparator = i;
				}
			}
		}
		return s;
	}

	public static boolean isSeparator(char c) {
		return c == '.' || c == '-' || c == '_';
	}

	public static boolean containsOnlyLettersAndDigits(String s) {
		for (char c : s.toCharArray()) {
			if (!isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;
	}


	public static boolean noUpperCase(String s) {
		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c)) {
				return false;
			}
		}
		return true;
	}

	public static String removeBorderQuotes(String s) {
		if (isQuoted(s)) {
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}

	public static boolean isQuoted(String selectedText) {
		return selectedText != null && selectedText.length() > 2
				&& (isBorderChar(selectedText, "\"") || isBorderChar(selectedText, "\'"));
	}

	public static boolean isBorderChar(String s, String borderChar) {
		return s.startsWith(borderChar) && s.endsWith(borderChar);
	}

	public static boolean noLowerCase(String s) {
		for (char c : s.toCharArray()) {
			if (Character.isLowerCase(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean containsUpperCase(String s) {
		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsUpperCaseAfterLowerCase(String s) {
		char previous = ' ';
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (isUpperCase(c) && isLetter(previous) && isLowerCase(previous)) {
				return true;
			}
			previous = c;
		}
		return false;
	}

	public static boolean isCapitalizedFirstButNotAll(String str) {
		if (str.length() == 0) {
			return false;
		}
		Set<Integer> delimiterSet = generateDelimiterSet(new char[]{' '});
		int strLen = str.length();
		int index = 0;

		int firstCapitalizedIndex = -1;
		boolean someUncapitalized = false;
		boolean afterSeparatorOrFirst = true;
		while (index < strLen) {
			int codePoint = str.codePointAt(index);
//			char c = str.charAt(index);
			if (delimiterSet.contains(codePoint)) {
				afterSeparatorOrFirst = true;
			} else {
				if (isLowerCase(codePoint) && afterSeparatorOrFirst) {
					if (firstCapitalizedIndex == -1) {
						return false;
					}
					someUncapitalized = true;
					afterSeparatorOrFirst = false;
				} else if (isUpperCase(codePoint) && afterSeparatorOrFirst) {
					if (firstCapitalizedIndex == -1) {
						firstCapitalizedIndex = index;
					}
					afterSeparatorOrFirst = false;
				}
			}
			index += charCount(codePoint);
		}
		return firstCapitalizedIndex != -1 && someUncapitalized;
	}

	public static boolean firstLetterUpperCase(String s) {
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (isLetter(c)) {
				if (isLowerCase(c)) {
					return false;
				} else if (isUpperCase(c)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean firstLetterLowerCase(String s) {
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (isLetter(c)) {
				if (isLowerCase(c)) {
					return true;
				} else if (isUpperCase(c)) {
					return false;
				}
			}
		}
		return false;
	}

	public static boolean noSeparators(String s, char... delimiters) {
		if (s.length() == 0) {
			return true;
		}
		Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
		boolean letterFound = false;
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (isLetterOrDigit(c)) {
				letterFound = true;
				continue;
			}
			if (letterFound && delimiterSet.contains((int) c)) {
				return false;
			}
		}
		return true;
	}


	public static boolean containsSeparatorBetweenLetters(String s, char separator) {
		char previous = '?';
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == separator && isLetterOrDigit(previous) && nextIsLetterOrDigit(s, i)) {
				return true;
			}
			previous = c;
		}
		return false;
	}

	@NotNull
	public static List<String> splitToTokensBySpace(String originalText) {
		char[] chars = originalText.toCharArray();
		List<String> result = new ArrayList<>();

		int whiteSpaceBeginning = -1;
		int tokenBeginning = -1;
		for (int i = 0; i < chars.length; i++) {
			char aChar = chars[i];
			if (aChar == ' ') {
				if (whiteSpaceBeginning == -1) {
					whiteSpaceBeginning = i;
				}
				if (tokenBeginning != -1) {
					result.add(new String(Arrays.copyOfRange(chars, tokenBeginning, i)));
					tokenBeginning = -1;
				}
			} else {
				if (whiteSpaceBeginning != -1) {
					result.add(new String(Arrays.copyOfRange(chars, whiteSpaceBeginning, i)));
					whiteSpaceBeginning = -1;
				}
				if (tokenBeginning == -1) {
					tokenBeginning = i;
				}
			}
		}

		if (tokenBeginning != -1) {
			result.add(new String(Arrays.copyOfRange(chars, tokenBeginning, chars.length)));
		}
		if (whiteSpaceBeginning != -1) {
			result.add(new String(Arrays.copyOfRange(chars, whiteSpaceBeginning, chars.length)));
		}
		return result;
	}

	public static String toSpringEnvVariable(String s) {
		return Arrays.stream(split(s, "."))
				.map(StringUtils::trim)
				.map(str -> StringUtils.replaceChars(str, "-", ""))
				.map(str -> StringUtils.replaceChars(str, "_", ""))
				.collect(Collectors.joining("_"))
				.toUpperCase();
	}

	public static boolean isCapitalizedFully(String s, char separator) {
		boolean result = false;
		char prev = 0;
		for (char c1 : s.toCharArray()) {
			boolean shouldBeUpper = (prev == 0 || prev == separator);
			if (!isLetter(c1)) {
				//skip
			} else if ((shouldBeUpper && isUpperCase(c1)) || (!shouldBeUpper && isLowerCase(c1))) {
				result = true;
			} else {
				return false;
			}
			prev = c1;
		}
		return result;
	}


	public static class Constants {
		public static final char[] DELIMITERS = new char[]{'\'', '\"', ' '};
	}
}

package osmedile.intellij.stringmanip.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.*;


/**
 * @author Olivier Smedile
 * @version $Id: StringUtil.java 62 2008-04-20 11:11:54Z osmedile $
 */
public class StringUtil {

    public static String removeAllSpace(String s) {
        return StringUtils.join(s.split("\\s"), "").trim();
    }

    public static String trimAllSpace(String s) {
        return StringUtils.join(s.split("\\s+"), " ");
    }

    public static String camelToText(String s) {
        StringBuilder buf = new StringBuilder();
        char lastChar = ' ';
        for (char c : s.toCharArray()) {
            if (isUpperCase(c)) {
                if (lastChar != ' ') {
                    buf.append(" ");
                }
                c = Character.toLowerCase(c);
            }
            if (!(lastChar == ' ' && c == ' ')) {
                buf.append(c);
            }
            lastChar = c;
        }
        return buf.toString();
    }

    public static String toSoftCamelCase(String s) {
        String[] words = s.split("[\\s_]");


        for (int i = 0; i < words.length; i++) {
            words[i] = com.intellij.openapi.util.text.StringUtil.capitalize(words[i]);
        }

        return StringUtils.join(words);
    }

    /**
     * Compound words를 Java(CamelCase)로 변경함
     * <p/>
     * 참고: http://en.wikipedia.org/wiki/CamelCase
     *
     * @param s
     * @return
     */
    public static String toCamelCase(String s) {
        String[] words = s.split("[\\s_]");

        //TODO search not blank words

//        if (words.length > 0) {
//            words[0] = words[0].toLowerCase();
//        }

        boolean firstWordNotFound = true;
        for (int i = 0; i < words.length; i++) {
            if (firstWordNotFound && StringUtils.isNotBlank(words[i])) {
                words[i] = words[i].toLowerCase();
                firstWordNotFound = false;
            } else {
                words[i] = com.intellij.openapi.util.text.StringUtil.capitalize(words[i].toLowerCase());
            }
        }

        return StringUtils.join(words);
    }

    /**
     * 1. UpperCase로 변경함
     * 2. 중복되는 space 제거
     *
     * @param s
     * @return
     */
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

    /**
     * @param s
     * @return
     */
    public static String wordsAndHyphenAndCamelToConstantCase(String s) {
        StringBuilder buf = new StringBuilder();

        char lastChar = 'a';
        for (char c : s.toCharArray()) {
            boolean isUpperCaseAndPreviousIsLowerCase = isLowerCase(lastChar) && isUpperCase(c);
            boolean isLowerCaseLetter = !isWhitespace(c) && '_' != c && !isUpperCase(c);
            boolean isLowerCaseAndPreviousIsWhitespace = isWhitespace(lastChar) && isLowerCaseLetter;
            boolean previousIsWhitespace = isWhitespace(lastChar);
            boolean lastOneIsNotUnderscore = buf.length() > 0 && buf.charAt(buf.length() - 1) != '_';
            //  ORIGINAL      if (lastOneIsNotUnderscore && (isUpperCase(c) || isLowerCaseAndPreviousIsWhitespace)) {  
            if (lastOneIsNotUnderscore && (isUpperCaseAndPreviousIsLowerCase || previousIsWhitespace)) {
                buf.append("_");
            }

            if (c == '.') {
                buf.append('_');
            } else if (c == '-') {
                buf.append('_');
            } else if (!isWhitespace(c)) {
                buf.append(Character.toUpperCase(c));
            }

            lastChar = c;
        }
        if (isWhitespace(lastChar)) {
            buf.append("_");
        }


        return buf.toString();
    }

    /**
     * 1 [.-_] 인 경우에는 .으로 replace됨
     * 2.space인 경우에는 .로 replace되나 여러 space가 있는 경우에는 추가로 삽입한됨
     *
     * @param s
     * @return
     */
    public static String toDotCase(String s) {
        StringBuilder buf = new StringBuilder();

        char lastChar = 'a';
        for (char c : s.toCharArray()) {
            boolean isUpperCaseAndPreviousIsLowerCase = isLowerCase(lastChar) && isUpperCase(c);
            boolean previousIsWhitespace = isWhitespace(lastChar);
            boolean lastOneIsNotUnderscore = buf.length() > 0 && buf.charAt(buf.length() - 1) != '.';
            if (lastOneIsNotUnderscore && (isUpperCaseAndPreviousIsLowerCase || previousIsWhitespace)) {
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
            if (StringUtils.isNotEmpty(m.group())) {
                String match = input.subSequence(index, m.start()).toString();
                if (StringUtils.isNotEmpty(match)) {
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
        if (StringUtils.isNotEmpty(remaining)) {
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


        return StringUtils.join(parts);
    }

    /**
     * 1. If alpha chars, then converts to lowerCase
     * 2. space -> '-' (hyphen)
     *
     * @param s
     * @return
     */
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

    /**
     * 1. space (multi) -> '_' (underscore)
     * 2. 중복 ___ -> 제거함
     * 3. [,-] -> 제거함
     * 4. [()] -> replace with '_'
     *
     * @param s
     * @return
     */
    public static String wordsToUnderscoreCase(String s) {
        StringBuilder buf = new StringBuilder();

        char lastChar = 'a';
        int i = 0;
        for (char c : s.toCharArray()) {
            if (isWhitespace(lastChar) && (!isWhitespace(c) && '_' != c) //중복된 '  '를 제거하겠다.
                    && buf.length() > 0
                    && buf.charAt(buf.length() - 1) != '_') {
                buf.append("_");
            } else if ('_' == lastChar && (!isWhitespace(c) && '_' != c) //중복된 '__'를 제거하겠다.
                    && buf.length() > 0
                    && buf.charAt(buf.length() - 1) != '_') {
                buf.append("_");
            }

            if ('(' == c || ')' == c) {
                buf.append("_");
            }
            if (!isWhitespace(c) && isAlphabetic(c)
                    && '-' != c //hyphen 제거함
                    && '_' != c) {
                buf.append(c);
            }

            //space인 경우 그냥 skip
            lastChar = c;
        }

        if (isWhitespace(lastChar)) {
            buf.append("_");
        }
        return buf.toString();
    }
}

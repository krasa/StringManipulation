package osmedile.intellij.stringmanip.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                buf.append(" ");
                c = Character.toLowerCase(c);
            }
            buf.append(c);
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

    public static String wordsToConstantCase(String s) {
        StringBuilder buf = new StringBuilder();

        char lastChar = 'a';
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(lastChar) && (!Character.isWhitespace(c) && '_' != c) &&
                    buf.length() > 0 && buf.charAt(buf.length() - 1) != '_') {
                buf.append("_");
            }
            if (!Character.isWhitespace(c)) {
                buf.append(Character.toUpperCase(c));

            }
            lastChar = c;
        }
        if (Character.isWhitespace(lastChar)) {
            buf.append("_");
        }


        return buf.toString();

    }

    public static String wordsAndCamelToConstantCase(String s) {
        StringBuilder buf = new StringBuilder();

        char lastChar = 'a';
        for (char c : s.toCharArray()) {
            if (buf.length() > 0 && buf.charAt(buf.length() - 1) != '_' && (Character.isUpperCase(c) || (
                    Character.isWhitespace(lastChar) &&
                            (!Character.isWhitespace(c) && '_' != c && !Character.isUpperCase(c))))) {
                buf.append("_");

            }

            if (!Character.isWhitespace(c)) {
                buf.append(Character.toUpperCase(c));
            }

            lastChar = c;
        }
        if (Character.isWhitespace(lastChar)) {
            buf.append("_");
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
     *
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

}

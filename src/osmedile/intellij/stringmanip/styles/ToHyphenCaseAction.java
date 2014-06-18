package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class ToHyphenCaseAction extends AbstractStringManipAction {

    public String transform(String s) {
        return wordsToHyphenCase(s);
    }

    public static String wordsToHyphenCase(String s) {
        StringBuilder buf = new StringBuilder();
        char lastChar = 'a';
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(lastChar) && (!Character.isWhitespace(c) && '-' != c) && buf.length() > 0
                    && buf.charAt(buf.length() - 1) != '-') {
                buf.append("-");
            }
            if ('_' == c) {
                buf.append('-');
            } else if (!Character.isWhitespace(c)) {
                buf.append(Character.toLowerCase(c));
            }
            lastChar = c;
        }
        if (Character.isWhitespace(lastChar)) {
            buf.append("-");
        }
        return buf.toString();
    }
}

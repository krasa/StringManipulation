package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

public class WordsAndHyphenAndCamelToConstantStyleCaseAction extends AbstractStringManipAction {

    public String transform(String s) {
        if (toConstantStyle(s)) {
            return StringUtil.wordsAndHyphenAndCamelToConstantCase(s);
        } else {
            return StringUtil.toCamelCase(s);
        }
    }

    private boolean toConstantStyle(String s) {
        boolean toConstantStyle = false;
        for (char c : s.toCharArray()) {
            if (c == '-' || Character.isLowerCase(c) || Character.isWhitespace(c)) {
                toConstantStyle = true;
                break;
            }
        }
        return toConstantStyle;
    }
}
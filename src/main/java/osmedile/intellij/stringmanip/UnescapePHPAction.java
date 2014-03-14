package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

public class UnescapePHPAction extends AbstractStringManipAction {

    public String transform(String s) {
        return StringEscapeUtil.unescapePHP(s);
    }
}
package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

public class EscapePHPAction extends AbstractStringManipAction {

    public String transform(String s) {
        return StringEscapeUtil.escapePHP(s);
    }
}
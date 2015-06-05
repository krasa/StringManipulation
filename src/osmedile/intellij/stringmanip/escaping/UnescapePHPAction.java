package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

public class UnescapePHPAction extends AbstractStringManipAction {

    public String transform(String s) {
        return StringEscapeUtil.unescapePHP(s);
    }
}
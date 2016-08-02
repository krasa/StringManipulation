package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

public class EscapePHPAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringEscapeUtil.escapePHP(s);
    }
}
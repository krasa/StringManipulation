package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

public class UnescapePHPAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringEscapeUtil.unescapePHP(s);
    }
}
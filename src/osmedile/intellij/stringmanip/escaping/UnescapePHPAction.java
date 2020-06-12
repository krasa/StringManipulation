package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

import java.util.Map;

public class UnescapePHPAction extends AbstractStringManipAction<Object> {

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
        return StringEscapeUtil.unescapePHP(s);
    }
}
package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class UnicodeEscapedToStringAction extends AbstractStringManipAction<Object> {

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return StringUtil.escapedUnicodeToString(s);
    }
}
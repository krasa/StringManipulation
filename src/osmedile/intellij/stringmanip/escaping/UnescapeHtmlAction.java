package osmedile.intellij.stringmanip.escaping;

import org.apache.commons.lang3.StringEscapeUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

/**
 * @author Olivier Smedile
 * @version $Id: UnescapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class UnescapeHtmlAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return StringEscapeUtils.unescapeHtml4(s);
    }
}
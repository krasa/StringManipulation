package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class EscapeJsonAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return shaded.org.apache.commons.text.StringEscapeUtils.escapeJson(s);
	}
}
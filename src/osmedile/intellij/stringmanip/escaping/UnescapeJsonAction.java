package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class UnescapeJsonAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return org.apache.commons.text.StringEscapeUtils.unescapeJson(s);
	}
}
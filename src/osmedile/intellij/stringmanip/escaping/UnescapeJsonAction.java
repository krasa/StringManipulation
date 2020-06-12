package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class UnescapeJsonAction extends AbstractStringManipAction<Object> {

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return shaded.org.apache.commons.text.StringEscapeUtils.unescapeJson(s);
	}
}
package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class EscapeJsonAction extends AbstractStringManipAction<Object> {

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return shaded.org.apache.commons.text.StringEscapeUtils.escapeJson(s);
	}
}
package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeJavaAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class EscapeJavaAction extends AbstractStringManipAction<Object> {

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return shaded.org.apache.commons.text.StringEscapeUtils.escapeJava(s);
    }
}

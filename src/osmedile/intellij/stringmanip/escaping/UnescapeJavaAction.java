package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

/**
 * @author Olivier Smedile
 * @version $Id: UnescapeJavaAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class UnescapeJavaAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return shaded.org.apache.commons.text.StringEscapeUtils.unescapeJava(s);
    }
}
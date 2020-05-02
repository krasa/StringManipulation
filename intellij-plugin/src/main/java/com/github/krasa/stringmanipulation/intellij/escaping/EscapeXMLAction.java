package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class EscapeXMLAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return StringEscapeUtils.escapeXml11(s);
    }
}
package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;

/**
 * @author Olivier Smedile
 * @version $Id: UnescapeJavaScriptAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class UnescapeJavaScriptAction extends AbstractStringManipAction {

	@Override                                                                         
	public String transformByLine(String s) {
		return org.apache.commons.text.StringEscapeUtils.unescapeEcmaScript(s);
    }
}                                                                            
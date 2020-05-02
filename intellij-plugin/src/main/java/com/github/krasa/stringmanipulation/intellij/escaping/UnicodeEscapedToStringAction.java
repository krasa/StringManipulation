package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.commons.util.StringUtil;
import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class UnicodeEscapedToStringAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return StringUtil.escapedUnicodeToString(s);
    }
}
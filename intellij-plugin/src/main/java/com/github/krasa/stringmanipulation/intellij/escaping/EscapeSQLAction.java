package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.utils.common.StringEscapeUtil;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class EscapeSQLAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringEscapeUtil.escapeSql(s);
    }
}
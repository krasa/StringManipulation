package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;

public class UnescapeJsonAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
		return org.apache.commons.text.StringEscapeUtils.unescapeJson(s);
	}
}
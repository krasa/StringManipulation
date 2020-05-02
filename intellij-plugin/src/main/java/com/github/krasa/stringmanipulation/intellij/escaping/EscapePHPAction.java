package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.commons.util.StringEscapeUtil;
import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;

public class EscapePHPAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringEscapeUtil.escapePHP(s);
    }
}
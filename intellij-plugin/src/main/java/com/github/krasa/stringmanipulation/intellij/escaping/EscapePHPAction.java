package com.github.krasa.stringmanipulation.intellij.escaping;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.utils.common.StringEscapeUtil;

public class EscapePHPAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringEscapeUtil.escapePHP(s);
    }
}
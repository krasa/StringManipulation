package com.github.krasa.stringmanipulation.intellij.unused;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.utils.common.StringUtil;

public class WordsToConstantStyleCaseAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringUtil.wordsToConstantCase(s);
    }
}
package com.github.krasa.stringmanipulation.intellij.unused;

import com.github.krasa.stringmanipulation.commons.util.StringUtil;
import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;

public class WordsToConstantStyleCaseAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringUtil.wordsToConstantCase(s);
    }
}
package com.github.krasa.stringmanipulation.intellij.filter;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;

public class TrimAction extends AbstractStringManipAction {

	@Override
    public String transformByLine(String s) {
        return s == null ? null : s.trim();
    }
}
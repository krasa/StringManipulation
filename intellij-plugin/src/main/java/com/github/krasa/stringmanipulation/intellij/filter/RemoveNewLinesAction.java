package com.github.krasa.stringmanipulation.intellij.filter;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

public class RemoveNewLinesAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, Object additionalParam) {
		return selectedText.replaceAll("\n", "");
	}

	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}

}
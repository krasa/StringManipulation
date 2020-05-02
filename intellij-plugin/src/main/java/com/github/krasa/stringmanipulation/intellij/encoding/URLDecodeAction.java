package com.github.krasa.stringmanipulation.intellij.encoding;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.intellij.utils.EncodingUtils;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

public class URLDecodeAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, Object additionalParam) {
		return EncodingUtils.decodeUrl(selectedText);
	}

	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}
}
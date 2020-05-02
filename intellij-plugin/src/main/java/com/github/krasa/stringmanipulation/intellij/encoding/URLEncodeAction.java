package com.github.krasa.stringmanipulation.intellij.encoding;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.intellij.utils.EncodingUtils;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class URLEncodeAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, Object additionalParam) {
		return EncodingUtils.encodeUrl(selectedText);
	}


	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}
}
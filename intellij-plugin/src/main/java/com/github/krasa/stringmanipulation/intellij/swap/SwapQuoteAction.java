package com.github.krasa.stringmanipulation.intellij.swap;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

public class SwapQuoteAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String s, Object additionalParam) {
		if (s.contains("\"")) {
			return s.replace("\"", "'");
		} else if (s.contains("'")) {
			return s.replace("'", "`");
		} else if (s.contains("`")) {
			return s.replace("`", "\"");
		} else {
			return "\"" + s + "\"";
		}
	}

	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}

}

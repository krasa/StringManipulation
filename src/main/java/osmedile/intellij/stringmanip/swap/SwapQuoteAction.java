package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class SwapQuoteAction extends AbstractStringManipAction<Object> {

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String s, Object additionalParam) {
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char aChar = chars[i];
			if (aChar == '\'') {
				chars[i] = '\"';
			} else if (aChar == '\"') {
				chars[i] = '\'';
			}
		}
		return String.valueOf(chars);
	}


	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}

}

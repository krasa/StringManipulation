package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class StraightenQuoteAction extends AbstractStringManipAction<Object> {


	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String s, Object additionalParam) {
		return s.replace("‘", "'").replace("”", "\"");
	}


	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}

}

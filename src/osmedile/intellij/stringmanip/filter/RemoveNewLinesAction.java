package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class RemoveNewLinesAction extends AbstractStringManipAction<Object> {

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		return selectedText.replaceAll("\n", "");
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}

}
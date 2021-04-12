package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class TrimAction extends AbstractStringManipAction<Object> {

	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, DataContext dataContext, SelectionModel selectionModel) {
		selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
		return true;
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return s == null ? null : s.trim();
	}
}
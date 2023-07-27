package osmedile.intellij.stringmanip.filter;

import java.util.Map;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class TrimLeadingAction extends AbstractStringManipAction<Object> {

	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, Caret caret, DataContext dataContext, SelectionModel selectionModel) {
		selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
		return true;
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return s == null ? null : s.stripLeading();
	}
}
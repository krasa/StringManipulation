package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: RemoveAllSpacesAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class RemoveAllSpacesAction extends AbstractStringManipAction<Object> {
	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, Caret caret, DataContext dataContext, SelectionModel selectionModel) {
		selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
		return true;
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return StringUtil.removeAllSpace(s);
	}
}
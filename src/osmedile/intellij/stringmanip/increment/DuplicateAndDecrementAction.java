package osmedile.intellij.stringmanip.increment;

import com.intellij.openapi.editor.*;

/**
 * @author Olivier Smedile
 * @author Vojtech Krasa
 */
public class DuplicateAndDecrementAction extends DecrementAction {
	@Override
	protected void applyChanges(Editor editor, CaretModel caretModel, int line, int column,
			SelectionModel selectionModel, boolean hasSelection, String newText, int caretOffset) {
		editor.getDocument().insertString(selectionModel.getSelectionStart(), newText);

		if (hasSelection) {
			int selectionStart = selectionModel.getSelectionStart();
			int length = newText.length();
			caretModel.moveToOffset(selectionStart - length);
			selectionModel.setSelection(selectionStart - length, selectionStart);
			editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
		} else {
			selectionModel.removeSelection();
			caretModel.moveToLogicalPosition(new LogicalPosition(line, column));
		}
	}
}

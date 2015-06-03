package osmedile.intellij.stringmanip;

import com.intellij.openapi.editor.*;

/**
 * @author Olivier Smedile
 * @author Vojtech Krasa
 */
public class DuplicateAndIncrementAction extends IncrementAction {

	@Override
	protected void applyChanges(Editor editor, CaretModel caretModel, int line, int column,
			SelectionModel selectionModel, boolean hasSelection, String selectedText, String newText, int caretOffset) {
		editor.getDocument().insertString(selectionModel.getSelectionEnd(), newText);

		if (hasSelection) {
			int selectionStart = selectionModel.getSelectionStart();
			int selectionEnd = selectionModel.getSelectionEnd();
			int length = newText.length();
			caretModel.moveToOffset(caretOffset + length);
			selectionModel.setSelection(selectionStart + length, selectionEnd + length);
			editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
		} else {
			selectionModel.removeSelection();
			caretModel.moveToLogicalPosition(new LogicalPosition(line + 1, column));
		}
	}

}
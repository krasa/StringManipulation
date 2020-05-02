package com.github.krasa.stringmanipulation.intellij.increment;

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
			int selectionEnd = selectionModel.getSelectionEnd();
			int length = newText.length();
			caretModel.moveToOffset(caretOffset);
			selectionModel.setSelection(selectionStart - length, selectionEnd - length);
			editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
		} else {
			selectionModel.removeSelection();
			caretModel.moveToLogicalPosition(new LogicalPosition(line, column));
		}
	}
}

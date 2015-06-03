package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import osmedile.intellij.stringmanip.utils.StringUtils;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

/**
 * @author Olivier Smedile
 * @author Vojtech Krasa
 */
public class IncrementAction extends EditorAction {

	public IncrementAction() {
		super(null);
		this.setupHandler(new EditorWriteActionHandler(true) {

			public void executeWriteAction(Editor editor, DataContext dataContext) {

				// Column mode not supported
				if (editor.isColumnMode()) {
					return;
				}
				final CaretModel caretModel = editor.getCaretModel();

				final int line = caretModel.getLogicalPosition().line;
				final int column = caretModel.getLogicalPosition().column;
				int caretOffset = caretModel.getOffset();

				final SelectionModel selectionModel = editor.getSelectionModel();
				boolean hasSelection = selectionModel.hasSelection();
				if (hasSelection == false) {
					selectionModel.selectLineAtCaret();
				}
				final String selectedText = selectionModel.getSelectedText();

				if (selectedText != null) {
					String[] textParts = StringUtil.splitPreserveAllTokens(selectedText,
							DuplicatUtils.SIMPLE_NUMBER_REGEX);
					for (int i = 0; i < textParts.length; i++) {
						textParts[i] = DuplicatUtils.simpleInc(textParts[i]);
					}

					final String newText = StringUtils.join(textParts);
					applyChanges(editor, caretModel, line, column, selectionModel, hasSelection, selectedText, newText,
							caretOffset);
				}
			}

		});
	}

	protected void applyChanges(Editor editor, CaretModel caretModel, int line, int column,
			SelectionModel selectionModel, boolean hasSelection, String selectedText, String newText, int caretOffset) {
		editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(),
				newText);
	}

}
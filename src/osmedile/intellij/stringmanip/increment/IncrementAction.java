package osmedile.intellij.stringmanip.increment;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.MyApplicationComponent;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import shaded.org.apache.commons.lang3.StringUtils;

/**
 * @author Olivier Smedile
 * @author Vojtech Krasa
 */
public class IncrementAction extends MyEditorAction {
	public IncrementAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(true) {

				@Override
				public void executeWriteAction(Editor editor, DataContext dataContext) {
					MyApplicationComponent.setAction(getActionClass());

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
						final String newText = processSelection(selectedText);
						applyChanges(editor, caretModel, line, column, selectionModel, hasSelection, selectedText, newText,
							caretOffset);
					}
				}

			});

		}
	}

	protected String processSelection(String selectedText) {
		String[] textParts = StringUtil.splitPreserveAllTokens(selectedText,
			DuplicatUtils.SIMPLE_NUMBER_REGEX);
		for (int i = 0; i < textParts.length; i++) {
			textParts[i] = DuplicatUtils.simpleInc(textParts[i]);
		}

		return StringUtils.join(textParts);
	}

	public IncrementAction() {
		this(true);
	}


	protected void applyChanges(Editor editor, CaretModel caretModel, int line, int column,
								SelectionModel selectionModel, boolean hasSelection, String selectedText, String newText, int caretOffset) {
		editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(),
			newText);
	}

}
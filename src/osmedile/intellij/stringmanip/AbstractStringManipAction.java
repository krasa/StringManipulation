package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

import osmedile.intellij.stringmanip.utils.StringUtils;

/**
 * @author Olivier Smedile
 * @version $Id: AbstractStringManipAction.java 62 2008-04-20 11:11:54Z osmedile $
 */
public abstract class AbstractStringManipAction extends EditorAction {

	protected AbstractStringManipAction() {
		this(true);
	}

	protected AbstractStringManipAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(true) {

				@Override
				public void executeWriteAction(Editor editor, DataContext dataContext) {
					final SelectionModel selectionModel = editor.getSelectionModel();
					String selectedText = selectionModel.getSelectedText();

					if (selectedText == null) {
						selectSomethingUnderCaret(editor, dataContext, selectionModel);
						selectedText = selectionModel.getSelectedText();

						if (selectedText == null) {
							return;
						}
					}

					String s = transformSelection(editor, dataContext, selectedText);
					s = s.replace("\r\n", "\n");
					s = s.replace("\r", "\n");
					editor.getDocument().replaceString(selectionModel.getSelectionStart(),
							selectionModel.getSelectionEnd(), s);
				}
			});
		}

	}

	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText) {
		String[] textParts = selectedText.split("\n");

		for (int i = 0; i < textParts.length; i++) {
			textParts[i] = transformByLine(textParts[i]);
		}

		String join = StringUtils.join(textParts, '\n');

		if (selectedText.endsWith("\n")) {
			return join + "\n";
		}
		return join;
	}

	protected boolean selectSomethingUnderCaret(Editor editor, DataContext dataContext, SelectionModel selectionModel) {
		selectionModel.selectLineAtCaret();
		String selectedText = selectionModel.getSelectedText();
		if (selectedText != null && selectedText.endsWith("\n")) {
			selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
		}
		return true;
	}

	public abstract String transformByLine(String s);
}

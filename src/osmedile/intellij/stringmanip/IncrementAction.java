package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import osmedile.intellij.stringmanip.utils.StringUtils;

/**
 * @author Olivier Smedile
 * @version $Id: IncrementAction.java 40 2008-03-26 21:08:33Z osmedile $
 */
public class IncrementAction extends EditorAction {

    public IncrementAction() {
        super(new EditorWriteActionHandler() {
            public void executeWriteAction(Editor editor, DataContext dataContext) {

                //Column mode not supported
                if (editor.isColumnMode()) {
                    return;
                }
                final CaretModel caretModel = editor.getCaretModel();

                final int line = caretModel.getLogicalPosition().line;
                final int column = caretModel.getLogicalPosition().column;
				long offset = caretModel.getOffset();

				final SelectionModel selectionModel = editor.getSelectionModel();
				boolean hasSelection = selectionModel.hasSelection();
				if (hasSelection == false) {
                    selectionModel.selectLineAtCaret();
                }
                final String selectedText = selectionModel.getSelectedText();

                if (selectedText != null) {
                    String[] textParts = StringUtil
                            .splitPreserveAllTokens(selectedText, DuplicatUtils.SIMPLE_NUMBER_REGEX);
                    for (int i = 0; i < textParts.length; i++) {
                        textParts[i] = DuplicatUtils.simpleInc(textParts[i]);
                    }

                    final String s = StringUtils.join(textParts);
                    editor.getDocument().insertString(selectionModel.getSelectionEnd(), s);

					if (hasSelection) {
						long selectionStart = selectionModel.getSelectionStart();
						long selectionEnd = selectionModel.getSelectionEnd();
						long length = s.length();
						caretModel.moveToOffset((int) (offset+length));
						selectionModel.setSelection((int) (selectionStart + length), (int) (selectionEnd + length));
						editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
					} else {
						selectionModel.removeSelection();
						caretModel.moveToLogicalPosition(new LogicalPosition(line + 1, column));
					} 
                }
            }
        });
    }
}
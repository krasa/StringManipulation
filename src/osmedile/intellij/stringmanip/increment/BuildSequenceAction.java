package osmedile.intellij.stringmanip.increment;

import java.util.HashSet;
import java.util.Set;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import osmedile.intellij.stringmanip.utils.StringUtils;

public class BuildSequenceAction extends EditorAction {

	public BuildSequenceAction() {
		super(null);
		this.setupHandler(new EditorWriteActionHandler(false) {

			public void executeWriteAction(final Editor editor, DataContext dataContext) {
				final HashSet<String> values = new HashSet<String>();
				editor.getCaretModel().runForEachCaret(new CaretAction() {
					@Override
					public void perform(Caret caret) {
						if (caret.isValid()) {
							processCaret(editor, caret, values);
						}
					}
				});
			}

			private void processCaret(Editor editor, Caret caret, Set<String> values) {
				boolean hasSelection = caret.hasSelection();
				if (!hasSelection) {
					caret.selectLineAtCaret();
				}

				int selectionStart = caret.getSelectionStart();
				int selectionEnd = caret.getSelectionEnd();
				TextRange textRange = new TextRange(selectionStart, selectionEnd);
				String selectedText = editor.getDocument().getText(textRange);

				String[] textParts = StringUtil.splitPreserveAllTokens(selectedText, DuplicatUtils.SIMPLE_NUMBER_REGEX);
				for (int i = 0; i < textParts.length; i++) {
					textParts[i] = processText(values, textParts[i]);
				}

				final String newText = StringUtils.join(textParts);
				editor.getDocument().replaceString(textRange.getStartOffset(), textRange.getEndOffset(), newText);
			}

			private String processText(Set<String> values, String textPart) {
				String s = textPart;
				if (DuplicatUtils.getNumber(textPart) != null) {
					s = DuplicatUtils.simpleInc(textPart);
					while (values.contains(s)) {
						s = DuplicatUtils.simpleInc(s);
					}
					values.add(s);
				}
				return s;
			}

		});
	}

}
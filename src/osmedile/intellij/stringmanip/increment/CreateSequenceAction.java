package osmedile.intellij.stringmanip.increment;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.MyApplicationComponent;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import osmedile.intellij.stringmanip.utils.StringUtils;

import java.util.concurrent.atomic.AtomicReference;

public class CreateSequenceAction extends MyEditorAction {

	public CreateSequenceAction() {
		this(true);
	}

	public CreateSequenceAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {

				@Override
				public void executeWriteAction(final Editor editor, DataContext dataContext) {
					MyApplicationComponent.setAction(getActionClass());
					
					final AtomicReference<String> lastValue = new AtomicReference<String>();
					editor.getCaretModel().runForEachCaret(caret -> {
						if (caret.isValid()) {
							processCaret(editor, caret, lastValue);
						}
					});
				}

				private void processCaret(Editor editor, Caret caret, AtomicReference<String> lastValue) {
					if (!caret.hasSelection()) {
						caret.selectLineAtCaret();
					}

					final String newText = processSelection(caret.getSelectedText(), lastValue);

					editor.getDocument().replaceString(caret.getSelectionStart(), caret.getSelectionEnd(), newText);
				}

			});

		}
	}

	protected String processSelection(String selectedText, AtomicReference<String> lastValue) {
		String[] textParts = StringUtil.splitPreserveAllTokens(selectedText, DuplicatUtils.SIMPLE_NUMBER_REGEX);
		for (int i = 0; i < textParts.length; i++) {
			textParts[i] = processTextPart(lastValue, textParts[i]);
		}

		return StringUtils.join(textParts);
	}

	private String processTextPart(AtomicReference<String> lastValue, String textPart) {
		String s = textPart;
		if (DuplicatUtils.getNumber(s) != null) {
			String last = lastValue.get();

			if (last != null) {
				s = DuplicatUtils.simpleInc(last);
			}

			lastValue.set(s);
		}
		return s;
	}

}
package osmedile.intellij.stringmanip.increment;

import java.util.concurrent.atomic.AtomicReference;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import osmedile.intellij.stringmanip.utils.StringUtils;

public class CreateSequenceAction extends EditorAction {

	public CreateSequenceAction() {
		this(true);
	}

	public CreateSequenceAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {

				@Override
				public void executeWriteAction(final Editor editor, DataContext dataContext) {
					final AtomicReference<String> lastValue = new AtomicReference<String>();
					editor.getCaretModel().runForEachCaret(new CaretAction() {
						@Override
						public void perform(Caret caret) {
							if (caret.isValid()) {
								processCaret(editor, caret, lastValue);
							}
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
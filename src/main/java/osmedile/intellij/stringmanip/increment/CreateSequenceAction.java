package osmedile.intellij.stringmanip.increment;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyApplicationService;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;

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
					MyApplicationService.setAction(getActionClass());

					final AtomicReference<String> lastValue = new AtomicReference<String>();
					editor.getCaretModel().runForEachCaret(new CaretAction() {
						@Override
						public void perform(@NotNull Caret caret) {
							if (caret.isValid()) {
								processCaret(editor, caret, lastValue);
							}
						}
					});
				}

				private void processCaret(Editor editor, Caret caret, AtomicReference<String> lastValue) {
					final String newText = processSelection(caret, lastValue);

					editor.getDocument().replaceString(caret.getSelectionStart(), caret.getSelectionEnd(), newText);
					editor.getSelectionModel().setSelection(caret.getSelectionStart(), caret.getSelectionStart() + newText.length());
				}

			});

		}
	}

	private String processSelection(Caret caret, AtomicReference<String> lastValue) {
		String selectedText = caret.getSelectedText();
		if (selectedText == null) {
			selectedText = "0";
		}
		return processSelection(selectedText, lastValue);
	}

	protected String processSelection(String selectedText, AtomicReference<String> lastValue) {
		String[] textParts = StringUtil.splitPreserveAllTokens(selectedText, UniversalNumber.UNIVERSAL_NUMBER_REGEX);
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
				s = UniversalNumber.increment(last);
			}

			lastValue.set(s);
		}
		return s;
	}

}
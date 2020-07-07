package osmedile.intellij.stringmanip.increment;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.MyApplicationService;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class IncrementDuplicateNumbersAction extends MyEditorAction {

	public IncrementDuplicateNumbersAction() {
		this(true);
	}

	public IncrementDuplicateNumbersAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {

				@Override
				public void executeWriteAction(final Editor editor, DataContext dataContext) {
					MyApplicationService.setAction(getActionClass());

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
					if (!caret.hasSelection()) {
						caret.selectLineAtCaret();
					}

					final String newText = processSelection(caret.getSelectedText(), values);

					editor.getDocument().replaceString(caret.getSelectionStart(), caret.getSelectionEnd(), newText);
				}

			});

		}
	}

	protected String processSelection(String selectedText, Set<String> values) {
		String[] textParts = StringUtil.splitPreserveAllTokens(selectedText, UniversalNumber.UNIVERSAL_NUMBER_REGEX);
		for (int i = 0; i < textParts.length; i++) {
			textParts[i] = processTextPart(values, textParts[i]);
		}

		return StringUtils.join(textParts);
	}

	private String processTextPart(Set<String> values, String textPart) {
		String s = textPart;
		if (DuplicatUtils.getNumber(textPart) != null) {
			if (values.contains(textPart)) {
				s = UniversalNumber.increment(textPart);
				while (values.contains(s)) {
					s = UniversalNumber.increment(s);
				}
			}
			values.add(s);
		}
		return s;
	}

}
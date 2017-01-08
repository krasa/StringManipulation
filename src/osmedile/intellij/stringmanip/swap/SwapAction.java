package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwapAction extends EditorAction {
	public static final Key<String> KEY = Key.create("StringManipulation.SwapAction.UserData");

	String lastSeparator = ",";

	protected SwapAction() {
		super(null);
		this.setupHandler(new EditorWriteActionHandler(false) {
			@Override
			public void doExecute(Editor editor, @Nullable Caret caret, DataContext dataContext) {
				SwapActionExecutor swapActionExecutor = new SwapActionExecutor(SwapAction.this, editor, dataContext);
				if (swapActionExecutor.isSwappingTokens()) {
					String separator = swapActionExecutor.chooseSeparator();
					if (separator == null) {
						return;
					} else {
						try {
							editor.putUserData(KEY, separator);
							super.doExecute(editor, caret, dataContext);
						} finally {
							editor.putUserData(KEY, null);
						}
					}
				} else {
					super.doExecute(editor, caret, dataContext);
				}
			}

			@Override
			public void executeWriteAction(Editor editor, DataContext dataContext) {
				new SwapActionExecutor(SwapAction.this, editor, dataContext).execute();
			}
		});
	}

	private class SwapActionExecutor extends SwapActionExecutorSupport {

		public SwapActionExecutor(SwapAction action, Editor editor, DataContext dataContext) {
			super(action, editor, dataContext);
		}

		public boolean isSwappingTokens() {
			if (singleCaret() && (noSelection() || max2CharSelection())) {
				return false;
			} else if (singleCaret() && singleSelection()) {
				return true;
			}
			return false;
		}

		public void execute() {
			IdeUtils.sort(caretsAndSelections);
			if (singleCaret() && (noSelection() || max2CharSelection())) {
				swapCharacters();
			} else if (singleCaret() && singleSelection()) {
				swapTokens();
			} else {// multiple carets || multiple selections
				selectLines_for_caretsWithoutSelection();
				rotateSelections();
			}
		}

		public void swapCharacters() {
			for (CaretState caretsAndSelection : caretsAndSelections) {
				int selectionStart = startOffset(caretsAndSelection);
				int selectionEnd = endOffset(caretsAndSelection);

				final Document document = this.document;
				final int textLength = document.getTextLength();
				String selectedText;
				if (selectionStart == selectionEnd) {
					selectionStart = selectionStart - 1;
					selectionEnd = selectionEnd + 1;
					if (selectionStart < 0 || selectionEnd > textLength) {
						return;
					}
					selectedText = document.getText(TextRange.create(selectionStart, selectionEnd));
				} else {
					selectedText = document.getText(TextRange.create(selectionStart, selectionEnd));
				}
				document.replaceString(selectionStart, selectionEnd, swapCharacters(selectedText));
			}

		}

		private void swapTokens() {
			String separator = editor.getUserData(KEY);
			if (separator == null) {
				return;
			}
			for (CaretState caretsAndSelection : caretsAndSelections) {
				int start = toOffset(caretsAndSelection.getSelectionStart());
				int end = toOffset(caretsAndSelection.getSelectionEnd());
				String selectedText = document.getText(TextRange.create(start, end));
				String result = swapTokens(separator, selectedText);
				document.replaceString(start, end, result);
			}

		}

		private void rotateSelections() {
			List<String> selections = new ArrayList<String>();
			for (CaretState caretsAndSelection : caretsAndSelections) {
				String text = editor.getDocument().getText(getTextRange(caretsAndSelection));
				selections.add(text);
			}

			Collections.rotate(selections, 1);

			for (int i = selections.size() - 1; i >= 0; i--) {
				String text = selections.get(i);
				CaretState caretsAndSelection = caretsAndSelections.get(i);
				TextRange textRange = getTextRange(caretsAndSelection);
				editor.getDocument().replaceString(textRange.getStartOffset(), textRange.getEndOffset(), text);
			}
		}
	}

}

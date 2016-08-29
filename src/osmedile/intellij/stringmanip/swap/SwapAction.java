package osmedile.intellij.stringmanip.swap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

public class SwapAction extends EditorAction {

	String lastSeparator = ",";

	protected SwapAction() {
		super(null);
		this.setupHandler(new EditorWriteActionHandler(false) {

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

		public void execute() {
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
			String separator = chooseSeparator();
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

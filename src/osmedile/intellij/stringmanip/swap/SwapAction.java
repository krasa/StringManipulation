package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwapAction extends EditorAction {

	String lastSeparator = ",";

	protected SwapAction() {
		super(null);
		this.setupHandler(new MyEditorWriteActionHandler<String>(false) {

			@NotNull
			public Pair<Boolean, String> beforeWriteAction(Editor editor, DataContext dataContext) {
				SwapActionExecutor swapActionExecutor = new SwapActionExecutor(SwapAction.this, editor, dataContext, lastSeparator);
				if (swapActionExecutor.isSwappingTokens()) {
					String separator = swapActionExecutor.chooseSeparator();
					if (separator != null) {
						return continueExecution(separator);
					}
				}
				return stopExecution();
			}

			@Override
			public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, String separator) {
				new SwapActionExecutor(SwapAction.this, editor, dataContext, separator).execute();
			}

		});
	}

	private class SwapActionExecutor extends SwapActionExecutorSupport {

		public SwapActionExecutor(SwapAction action, Editor editor, DataContext dataContext, String separator) {
			super(action, editor, dataContext, separator);
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
			ObjectUtils.assertNotNull(separator);
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

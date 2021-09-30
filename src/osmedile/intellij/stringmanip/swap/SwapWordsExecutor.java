package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.List;

public class SwapWordsExecutor {

	protected Editor editor;
	protected DataContext dataContext;
	protected List<CaretState> caretsAndSelections;
	protected Document document;

	public SwapWordsExecutor(Editor editor, DataContext dataContext) {
		this.editor = editor;
		this.dataContext = dataContext;
		document = editor.getDocument();
		caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
	}

	public SwapWordsExecutor() {
	}


	public int toOffset(LogicalPosition selectionEnd) {
		return editor.logicalPositionToOffset(selectionEnd);
	}

	protected boolean hasSelection(CaretState caretsAndSelection) {
		return (caretsAndSelection.getSelectionEnd() != null && caretsAndSelection.getSelectionStart() != null)
				&& !caretsAndSelection.getSelectionEnd().equals(caretsAndSelection.getSelectionStart());
	}

	protected void selectLines_for_caretsWithoutSelection() {
		editor.getCaretModel().runForEachCaret(new CaretAction() {
			@Override
			public void perform(Caret caret) {
				if (!caret.hasSelection()) {
					caret.selectLineAtCaret();
					String selectedText = caret.getSelectedText();
					if (selectedText != null && selectedText.endsWith("\n")) {
						caret.setSelection(caret.getSelectionStart(), caret.getSelectionEnd() - 1);
					}
				}
			}
		}, true);

		caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
	}

	public void execute() {
		selectLines_for_caretsWithoutSelection();
		swapWords();
	}

	private void swapWords() {
		for (CaretState caretsAndSelection : caretsAndSelections) {
			int start = toOffset(caretsAndSelection.getSelectionStart());
			int end = toOffset(caretsAndSelection.getSelectionEnd());
			if (end - start > 2) {
				String selectedText = document.getText(TextRange.create(start, end));
				String result = swapWords(selectedText);
				document.replaceString(start, end, result);
			}
		}

	}

	@NotNull
	public String swapWords(String selectedText) {
		return new Swapper(selectedText, false).swap();
	}
}

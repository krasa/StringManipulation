package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwapActionExecutor {


	protected Editor editor;
	protected DataContext dataContext;
	protected List<CaretState> caretsAndSelections;
	protected String separator;
	protected Document document;

	public SwapActionExecutor() {
	}

	public SwapActionExecutor(Editor editor, DataContext dataContext, @Nullable String separator) {
		this.editor = editor;
		this.dataContext = dataContext;
		document = editor.getDocument();
		caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		this.separator = separator;
	}

	@NotNull
	public String swapTokens(String separator, String selectedText) {
		String[] split;
		if (separator.equals(" ")) {
			split = org.apache.commons.lang.StringUtils.splitByWholeSeparator(selectedText.trim(), separator);
		} else {
			split = org.apache.commons.lang.StringUtils.splitByWholeSeparatorPreserveAllTokens(selectedText, separator);
		}

		String[] result = new String[split.length];
		result[0] = split[split.length - 1];
		System.arraycopy(split, 0, result, 1, split.length - 1);

		if (separator.equals(" ")) {
			return org.apache.commons.lang.StringUtils.join(result, separator);
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < result.length; i++) {
				String old = split[i];
				appendSpaceBefore(sb, old);
				sb.append(result[i].trim());
				appendSpaceAfter(sb, old);
				if (i != result.length - 1) {
					sb.append(separator);
				}

			}
			return sb.toString();
		}
	}

	private void appendSpaceAfter(StringBuilder sb, String old) {
		char[] chars = old.toCharArray();
		for (int j = chars.length - 1; j >= 0; j--) {
			char aChar = chars[j];
			if (aChar == ' ') {
				sb.append(' ');
			} else {
				break;
			}
		}
	}

	public void appendSpaceBefore(StringBuilder sb, String old) {
		char[] chars = old.toCharArray();
		for (int j = 0; j < chars.length; j++) {
			char aChar = chars[j];
			if (aChar == ' ') {
				sb.append(' ');
			} else {
				break;
			}
		}
	}

	public int endOffset(CaretState caretsAndSelection) {
		int i;
		LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
		if (selectionEnd != null) {
			i = toOffset(selectionEnd);
		} else {
			i = toOffset(caretsAndSelection.getCaretPosition());
		}
		return i;
	}

	public int toOffset(LogicalPosition selectionEnd) {
		return editor.logicalPositionToOffset(selectionEnd);
	}

	public int startOffset(CaretState caretsAndSelection) {
		int i;
		LogicalPosition selectionEnd = caretsAndSelection.getSelectionStart();
		if (selectionEnd != null) {
			i = toOffset(selectionEnd);
		} else {
			i = toOffset(caretsAndSelection.getCaretPosition());
		}
		return i;
	}

	public String swapCharacters(String s) {
		if (s.contains("\n")) {
			return s;
		}
		if (s.length() != 2) {
			return s;
		}
		return s.charAt(1) + "" + s.charAt(0);
	}

	protected boolean max2CharSelection() {
		int moreThan2CharSelections = 0;
		for (CaretState caretsAndSelection : caretsAndSelections) {
			if (hasSelection(caretsAndSelection)) {
				int start = toOffset(caretsAndSelection.getSelectionStart());
				int end = toOffset(caretsAndSelection.getSelectionEnd());
				if (Math.abs(start - end) > 2) {
					moreThan2CharSelections++;
				}
			}
		}
		return moreThan2CharSelections == 0;
	}

	protected boolean hasSelection(CaretState caretsAndSelection) {
		return (caretsAndSelection.getSelectionEnd() != null && caretsAndSelection.getSelectionStart() != null)
				&& !caretsAndSelection.getSelectionEnd().equals(caretsAndSelection.getSelectionStart());
	}

	protected boolean singleSelection() {
		int selections = 0;
		for (CaretState caretsAndSelection : caretsAndSelections) {
			if (caretsAndSelection.getSelectionEnd() != null
					&& !caretsAndSelection.getSelectionEnd().equals(caretsAndSelection.getSelectionStart())) {
				selections++;
			}
		}
		return selections == 1;
	}

	protected boolean multipleCarets() {
		return caretsAndSelections.size() > 1;
	}

	@NotNull
	public TextRange getTextRange(CaretState caretsAndSelection) {
		LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
		LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
		int selectionStartOffset = editor.logicalPositionToOffset(selectionStart);
		int selectionEndOffset = editor.logicalPositionToOffset(selectionEnd);
		return new TextRange(selectionStartOffset, selectionEndOffset);
	}

	protected String chooseSeparator() {
		StringBuilder sb = new StringBuilder();

		for (CaretState caretsAndSelection : caretsAndSelections) {
			int start = toOffset(caretsAndSelection.getSelectionStart());
			int end = toOffset(caretsAndSelection.getSelectionEnd());
			String selectedText = document.getText(TextRange.create(start, end));
			sb.append(selectedText);
		}

		String s = sb.toString();
		if (!s.contains(separator)) {
			if (s.contains(";")) {
				separator = ";";
			} else if (s.contains("||")) {
				separator = "||";
			} else if (s.contains("|")) {
				separator = "|";
			} else if (s.contains("/")) {
				separator = "/";
			} else if (s.contains("&&")) {
				separator = "&&";
			} else if (s.contains(".")) {
				separator = ".";
			} else if (s.contains(" ")) {
				separator = " ";
			}
		}

		String newSeparator = Messages.showInputDialog("Separator", "Split by separator and swap",
				Messages.getQuestionIcon(), separator, null);
		if (newSeparator != null) {
			if (newSeparator.equals("")) {
				newSeparator = " ";
			}
		} else {
			return null;
		}
		separator = newSeparator;
		return separator;
	}

	protected boolean multipleSelections() {
		int selections = 0;
		for (CaretState caretsAndSelection : caretsAndSelections) {
			if (hasSelection(caretsAndSelection)) {
				selections++;
			}
		}
		return selections >= 2;
	}

	protected boolean singleCaret() {
		return caretsAndSelections.size() == 1;
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

	protected boolean noSelection() {
		int selections = 0;
		for (CaretState caretsAndSelection : caretsAndSelections) {
			if (hasSelection(caretsAndSelection)) {
				selections++;
			}
		}
		return selections == 0;
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

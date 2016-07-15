package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SortLinesBySubSelectionAction extends EditorAction {

	private static final NaturalOrderComparator NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator();

	public SortLinesBySubSelectionAction() {
		this(true);
	}

	public SortLinesBySubSelectionAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {


				@SuppressWarnings("deprecation")
				public void executeWriteAction(Editor editor, DataContext dataContext) {
					List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
					sort(caretsAndSelections);
					filterCarets(editor, caretsAndSelections);

					if (caretsAndSelections.size() > 1) {
						processMultiCaret(editor, caretsAndSelections);
					}
				}

				public void sort(List<CaretState> caretsAndSelections) {
					Collections.sort(caretsAndSelections, new Comparator<CaretState>() {
						@Override
						public int compare(CaretState o1, CaretState o2) {
							return o1.getCaretPosition().compareTo(o2.getCaretPosition());
						}
					});
				}

				public void filterCarets(Editor editor, List<CaretState> caretsAndSelections) {
					int previousLineNumber = -1;
					Iterator<CaretState> iterator = caretsAndSelections.iterator();
					while (iterator.hasNext()) {
						CaretState caretsAndSelection = iterator.next();
						LogicalPosition caretPosition = caretsAndSelection.getCaretPosition();
						int lineNumber = editor.getDocument().getLineNumber(editor.logicalPositionToOffset(caretPosition));
						if (lineNumber == previousLineNumber) {
							Caret caret = editor.getCaretModel().getCaretAt(caretPosition.toVisualPosition());
							editor.getCaretModel().removeCaret(caret);
							iterator.remove();
						}
						previousLineNumber = lineNumber;
					}
				}


				private void processMultiCaret(Editor editor, List<CaretState> caretsAndSelections) {
					List<SortLine> lines = new ArrayList<SortLine>();
					for (CaretState caretsAndSelection : caretsAndSelections) {
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						int selectionStartOffset = editor.logicalPositionToOffset(selectionStart);
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						int selectionEndOffset = editor.logicalPositionToOffset(selectionEnd);
						LogicalPosition caretPosition = caretsAndSelection.getCaretPosition();
						if (selectionStartOffset == selectionEndOffset) {
							selectionEndOffset = editor.getDocument().getText().indexOf("\n", selectionStartOffset);
							Caret caret = editor.getCaretModel().getCaretAt(caretPosition.toVisualPosition());
							caret.setSelection(selectionStartOffset, selectionEndOffset);
							selectionEnd = editor.visualToLogicalPosition(caret.getSelectionEndPosition());
							caretPosition = caret.getLogicalPosition();
						}

						String selection = editor.getDocument().getText(new TextRange(selectionStartOffset, selectionEndOffset));

						int lineNumber = editor.getDocument().getLineNumber(selectionStartOffset);
						int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);
						int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
						String line = editor.getDocument().getText(new TextRange(lineStartOffset, lineEndOffset));


						lines.add(new SortLine(line, selection, lineStartOffset, lineEndOffset, selectionStart, selectionEnd, caretPosition));
					}

					List<SortLine> sortedLines = new ArrayList<SortLine>(lines);
					Collections.sort(sortedLines);

					write(editor, lines, sortedLines);
				}

				private void write(Editor editor, List<SortLine> lines, List<SortLine> sortedLines) {
					for (int i = lines.size() - 1; i >= 0; i--) {
						SortLine oldLine = lines.get(i);
						SortLine newLine = sortedLines.get(i);
						int lineStartOffset = oldLine.lineStartOffset;
						editor.getDocument().replaceString(lineStartOffset, oldLine.lineEndOffset, newLine.line);
						Caret caret = editor.getCaretModel().getCaretAt(oldLine.caretPosition.toVisualPosition());
						int startColumn = newLine.selectionStart.column;
						int endColumn = newLine.selectionEnd.column;
						caret.setSelection(lineStartOffset + startColumn, lineStartOffset + endColumn);
						caret.moveToOffset(lineStartOffset + startColumn);
					}
				}
			});
		}
	}

	private class SortLine implements Comparable {
		private final String line;
		private final String selection;
		private final int lineStartOffset;
		private final int lineEndOffset;
		private final LogicalPosition selectionStart;
		private final LogicalPosition selectionEnd;
		private final LogicalPosition caretPosition;

		public SortLine(String line, String selection, int lineStartOffset, int lineEndOffset, LogicalPosition selectionStart, LogicalPosition selectionEnd, LogicalPosition caretPosition) {
			this.line = line;
			this.selection = selection;
			this.lineStartOffset = lineStartOffset;
			this.lineEndOffset = lineEndOffset;
			this.selectionStart = selectionStart;
			this.selectionEnd = selectionEnd;
			this.caretPosition = caretPosition;
		}

		@Override
		public int compareTo(@NotNull Object o) {
			return NATURAL_ORDER_COMPARATOR.compare(selection, ((SortLine) o).selection);
		}
	}
}
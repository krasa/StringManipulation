package osmedile.intellij.stringmanip.sort;

import java.util.*;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.TextRange;

public class SortLinesBySubSelectionAction extends EditorAction {

	private static final NaturalOrderComparator NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator();
	private SortAction.Sort sortType;

	public SortLinesBySubSelectionAction() {
		this(true);
	}

	public SortLinesBySubSelectionAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {


				@Override
				@SuppressWarnings("deprecation")
				public void executeWriteAction(Editor editor, DataContext dataContext) {
					final SortTypeDialog dialog = new SortTypeDialog(sortType);
					DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
						{
							init();
							setTitle("Choose Charset");
						}

						@Nullable
						@Override
						public JComponent getPreferredFocusedComponent() {
							return dialog.insensitive;
						}

						@Nullable
						@Override
						protected String getDimensionServiceKey() {
							return "StringManipulation.SortTypeDialog";
						}

						@Nullable
						@Override
						protected JComponent createCenterPanel() {
							return dialog.contentPane;
						}

						@Override
						protected void doOKAction() {
							super.doOKAction();
						}
					};

					boolean b = dialogWrapper.showAndGet();
					if (!b) {
						return;
					}
					sortType = dialog.getResult();

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
						//no selection -> expand to end of line
						if (selectionStartOffset == selectionEndOffset) {
							String text = editor.getDocument().getText();
							selectionEndOffset = text.indexOf("\n", selectionStartOffset);
							if (selectionEndOffset == -1) {
								selectionEndOffset = text.length();
							}
							Caret caret = editor.getCaretModel().getCaretAt(caretPosition.toVisualPosition());
							caret.setSelection(selectionStartOffset, selectionEndOffset);
							caretPosition = caret.getLogicalPosition();
						}

						String selection = editor.getDocument().getText(new TextRange(selectionStartOffset, selectionEndOffset));

						int lineNumber = editor.getDocument().getLineNumber(selectionStartOffset);
						int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);
						int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
						String line = editor.getDocument().getText(new TextRange(lineStartOffset, lineEndOffset));


						lines.add(new SortLine(line, selection, lineStartOffset, lineEndOffset, selectionStartOffset - lineStartOffset, selectionEndOffset - lineStartOffset, caretPosition));
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
						Caret caret = editor.getCaretModel().getCaretAt(oldLine.caretPosition.toVisualPosition());
						editor.getDocument().replaceString(lineStartOffset, oldLine.lineEndOffset, newLine.line);
						int startColumn = newLine.selectionStartLineOffset;
						int endColumn = newLine.selectionEndLineOffset;
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
		private final int selectionStartLineOffset;
		private final int selectionEndLineOffset;
		private final LogicalPosition caretPosition;

		public SortLine(String line, String selection, int lineStartOffset, int lineEndOffset, int selectionStartLineOffset, int selectionEndLineOffset, LogicalPosition caretPosition) {
			this.line = line;
			this.selection = selection;
			this.lineStartOffset = lineStartOffset;
			this.lineEndOffset = lineEndOffset;
			this.selectionStartLineOffset = selectionStartLineOffset;
			this.selectionEndLineOffset = selectionEndLineOffset;
			this.caretPosition = caretPosition;
		}

		@Override
		public int compareTo(@NotNull Object o) {
			return sortType.getComparator().compare(selection, ((SortLine) o).selection);
		}
	}
}
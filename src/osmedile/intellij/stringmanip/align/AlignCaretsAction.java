package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.repeat;

public class AlignCaretsAction extends MyEditorAction {
	public AlignCaretsAction() {
		this(true);
	}

	protected AlignCaretsAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new AlignCaretsHandler(getActionClass(), false));
		}
	}

	public class AlignCaretsHandler extends MultiCaretHandlerHandler<Object> {
		boolean alignEnd;

		public AlignCaretsHandler(Class actionClass, boolean b) {
			super(actionClass);
			alignEnd = b;
		}

		@Override
		protected void executeWriteAction(Editor editor, @Nullable Caret mainCaret, DataContext dataContext, @Nullable Object additionalParameter) {
			Document document = editor.getDocument();
			CaretModel caretModel = editor.getCaretModel();

			SelectionSplitter.split(editor, "\n");
			int caretIndex = 0;
			while (caretIndex < 10_000) {
				List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
				IdeUtils.sort(caretsAndSelections);
				caretsAndSelections = filter2(caretIndex, caretsAndSelections);
				Collections.reverse(caretsAndSelections);

				if (caretsAndSelections.size() <= 1) {
					return;
				}

				int maxLeftColumn = caretsAndSelections.stream().mapToInt((c) -> getPositionStart(c).column).max().getAsInt();
				int maxLength = caretsAndSelections.stream().mapToInt((c) -> getPositionEnd(c).column - getPositionStart(c).column).max().getAsInt();
				int maxRightColumn = caretsAndSelections.stream().mapToInt((c) -> getPositionEnd(c).column).max().getAsInt();
				maxRightColumn = Math.max(maxLength + maxLeftColumn, maxRightColumn);

				for (CaretState caret : caretsAndSelections) {
					Caret caretAt = caretModel.getCaretAt(editor.logicalToVisualPosition(caret.getCaretPosition()));
					int pad = maxLeftColumn - getPositionStart(caret).column;
					int padRight = maxRightColumn - (getPositionEnd(caret).column + pad);
					int offset = editor.logicalPositionToOffset(getPositionStart(caret));
					int offsetEnd = editor.logicalPositionToOffset(getPositionEnd(caret));

					if (alignEnd) {
						document.insertString(offsetEnd, repeat(' ', padRight));
					}
					document.insertString(offset, repeat(' ', pad));

					if (alignEnd) {
						caretAt.setSelection(offset + pad, offsetEnd + padRight + pad);
						caretAt.moveToOffset(offsetEnd + padRight + pad);
					} else {
						caretAt.moveToOffset(offset + pad);
					}
				}
				caretIndex++;
			}
		}

		@Override
		protected String processSingleSelection(Editor editor, String text, Object model) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected List<String> processMultiSelections(Editor editor, List<String> lines, Object additionalParameter) {
			throw new UnsupportedOperationException();
		}

		private List<CaretState> filter2(int caretIndex, List<CaretState> caretsAndSelections) {
			int previousLine = -1;
			int index = 0;
			List<CaretState> caretStates = new ArrayList<>();
			for (CaretState caret : caretsAndSelections) {
				int line = getPositionStart(caret).line;
				if (getPositionStart(caret).line == previousLine) {
					index++;
				} else {
					index = 0;
				}
				if (index == caretIndex) {
					caretStates.add(caret);
				}
				previousLine = line;
			}
			return caretStates;
		}

		private List<CaretState> filter(List<CaretState> caretsAndSelections, Editor editor, CaretModel caretModel) {
			int previousLine = -1;
			List<CaretState> caretStates = new ArrayList<>();
			for (CaretState caret : caretsAndSelections) {
				int line = getPositionStart(caret).line;
				if (getPositionStart(caret).line == previousLine) {
					Caret caretAt = caretModel.getCaretAt(editor.logicalToVisualPosition(caret.getCaretPosition()));
					if (caretAt != null) {
						caretModel.removeCaret(caretAt);
					}
				} else {
					caretStates.add(caret);
				}
				previousLine = line;
			}
			return caretStates;
		}

		private LogicalPosition getPositionStart(CaretState c) {
			LogicalPosition selectionStart = c.getSelectionStart();
			LogicalPosition caretPosition = c.getCaretPosition();
			if (selectionStart != null) {
				return selectionStart;
			} else {
				return caretPosition;
			}
		}

		private LogicalPosition getPositionEnd(CaretState c) {
			LogicalPosition selectionEnd = c.getSelectionEnd();
			LogicalPosition caretPosition = c.getCaretPosition();
			if (selectionEnd != null) {
				return selectionEnd;
			} else {
				return caretPosition;
			}
		}


	}
}

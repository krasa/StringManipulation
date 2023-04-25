package osmedile.intellij.stringmanip.border;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.SimpleTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler2;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.utils.ActionUtils;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.inamik.text.tables.Cell.Functions.*;
import static com.intellij.openapi.util.text.StringUtil.repeatSymbol;

public class CreateBorderAction extends MyEditorAction {

	public CreateBorderAction() {
		super(null);
		setupHandler(new MyEditorWriteActionHandler2<BorderSettings>(getActionClass()) {

			@NotNull
			@Override
			protected Pair<Boolean, BorderSettings> beforeWriteAction(Editor editor, DataContext dataContext) {
				editor.getCaretModel().runForEachCaret(caret -> ActionUtils.selectSomethingUnderCaret(editor));

				BorderSettings settings = configure(editor);
				if (settings == null) return stopExecution();

				return continueExecution(PluginPersistentStateComponent.getInstance().getBorderSettings());
			}


			@Override
			public boolean executeLater(@NotNull Editor editor, DataContext dataContext) {
				return true;
			}

			@Override
			protected void executeWriteAction(Editor editor, BorderSettings settings) {
				transform(editor, settings);
			}
		});
	}

	private void transform(Editor editor, BorderSettings settings) {
		CaretModel caretModel = editor.getCaretModel();
		List<CaretState> caretsAndSelections = caretModel.getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
		List<String> lines = new ArrayList<String>();
		Document document = editor.getDocument();

		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			String text = document.getText(
					new TextRange(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd)));
			String[] split = text.split("\n");
			lines.addAll(List.of(split));
		}

		String join = StringUtils.join(lines, '\n');
		final String newText = transform(settings, join, EditorUtil.getTabSize(editor));

		if (caretsAndSelections.size() == 1 && lines.size() > 1) {
			SelectionModel selectionModel = editor.getSelectionModel();
			document.replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), newText);
			return;
		}
		caretModel.removeSecondaryCarets();
		editor.getSelectionModel().removeSelection(true);

		String[] result = newText.split("\n");
		for (int i = result.length - 1; i >= 0; i--) {
			boolean first = i == result.length - 1;
			String s1 = result[i];

			if (i >= caretsAndSelections.size()) {
				CaretState caretState = caretsAndSelections.get(caretsAndSelections.size() - 1);
				int lineNumber = caretState.getSelectionStart().line;
				int lineStartOffset = document.getLineStartOffset(lineNumber);
				int lineEndOffset = document.getLineEndOffset(lineNumber);
				int selStartOffset = editor.logicalPositionToOffset(caretState.getSelectionStart());

				String before = selStartOffset > lineStartOffset ? document.getText(TextRange.create(lineStartOffset, selStartOffset)) : "";
				int selEndOffset = editor.logicalPositionToOffset(caretState.getSelectionEnd());
				String after = lineEndOffset > selEndOffset ? document.getText(TextRange.create(selEndOffset, lineEndOffset)) : "";

				String addedText = before + s1 + after + "\n";
				Caret caret = first ? caretModel.getPrimaryCaret() : caretModel.addCaret(editor.offsetToLogicalPosition(lineEndOffset + 1), true);
				document.insertString(lineEndOffset + 1, addedText);
				if (caret != null) {
					caret.moveToOffset(lineEndOffset + 1 + addedText.length() - 1 - after.length());
					caret.setSelection(before.length() + lineEndOffset + 1, lineEndOffset + 1 + addedText.length() - 1 - after.length());
				}
			} else {
				CaretState caretState = caretsAndSelections.get(i);
				int startOffset = editor.logicalPositionToOffset(caretState.getSelectionStart());
				int endOffset = editor.logicalPositionToOffset(caretState.getSelectionEnd());
				String text = document.getText(TextRange.create(startOffset, endOffset));
				Caret caret = first ? caretModel.getPrimaryCaret() : caretModel.addCaret(caretState.getCaretPosition(), true);
				if (text.endsWith("\n")) {
					endOffset--;
				}
				document.replaceString(startOffset, endOffset, s1);
				if (caret != null) {
					caret.moveToOffset(startOffset + s1.length());
					caret.setSelection(startOffset, startOffset + s1.length());
				}
			}

		}
	}


	protected BorderSettings configure(Editor editor) {
		BorderSettings settings = getSettings();

		final BorderDialog dialog = new BorderDialog(this, settings, editor);

		if (!dialog.showAndGet(editor.getProject(), StringManipulationBundle.message("border"), "StringManipulation.BorderDialog")) {
			return null;
		}

		settings = dialog.getSettings();
		storeSettings(settings);
		return settings;

	}

	protected BorderSettings getSettings() {
		return PluginPersistentStateComponent.getInstance().getBorderSettings();
	}

	protected void storeSettings(BorderSettings newSettings) {
		PluginPersistentStateComponent.getInstance().setBorderSettings(newSettings);
	}

	protected String transform(BorderSettings settings, String selectedText, int tabSize) {
		String string = repeatSymbol(' ', tabSize);
		selectedText = selectedText.replace("\t", string);
		int topBottomPadding = 0;

		for (int i = 0; i < settings.getBorderWidthAsInt(); i++) {
			Collection<String> cell = Cell.of();
			for (String s : selectedText.split("\n")) {
				cell = Cell.append(cell, s);
			}
			int cellWidth = getCellWidth(cell);
			int cellHeight = getCellHeight(cell);
			int padding = i == 0 ? settings.getPaddingAsInt() : 0;

			SimpleTable simpleTable = SimpleTable.of()
					.nextRow()
					.nextCell(cell);
			// Expand to current height + 5, adding new lines to TOP
			//

			if (settings.isTopAndBottomPadding() || settings.isFullPadding()) {
				topBottomPadding = padding;
				simpleTable.applyToCell(TOP_PAD.withHeight(cellHeight + padding))
						// Expand to new-current-height + 5, adding new lines to the BOTTOM
						//
						.applyToCell(BOTTOM_PAD.withHeight(cellHeight + padding + padding));
			}
			// Expand to current width + 5, adding new lines to the LEFT
			//
			// Expand to new-current-width + 5, adding new lines to the RIGHT
			//

			if (settings.isSidePadding() || settings.isFullPadding()) {
				simpleTable.applyToCell(RIGHT_PAD.withWidth(cellWidth + padding))
						.applyToCell(LEFT_PAD.withWidth(cellWidth + padding + padding));

			}

			GridTable g = simpleTable.toGrid();

			if (settings.isFullBorder()) {
				if (settings.isBorderSingle()) {
					g = Border.SINGLE_LINE.apply(g);
				} else if (settings.isBorderDouble()) {
					g = Border.DOUBLE_LINE.apply(g);
				} else if (settings.isBorderCustom()) {
					g = Border.of(Border.Chars.of(settings.getBorderChar(i))).apply(g);
				}
			} else if (settings.isTopAndBottomBorder() || settings.isBottomBorder()) {
				if (settings.isBorderSingle()) {
					g = HorizontalBorder.of(HorizontalBorder.Chars.of('─')).apply(g);
				} else if (settings.isBorderDouble()) {
					g = HorizontalBorder.of(HorizontalBorder.Chars.of('═')).apply(g);
				} else if (settings.isBorderCustom()) {
					char intersect = settings.getBorderChar(i);
					g = HorizontalBorder.of(HorizontalBorder.Chars.of(intersect)).apply(g);
				}
			}

			selectedText = Util.asString(g);
			selectedText = selectedText.replace("\r\n", "\n");
			if (!settings.isFullBorder()) {
				break;
			}
		}


		if (settings.isTopAndBottomBorder()) {
			String[] split = selectedText.split("\n");
			ArrayList<String> lines = new ArrayList<>();

			for (int i = 0; i < settings.getBorderWidthAsInt() - 1; i++) {
				lines.add(normalizePadding(split[0]));
			}

			for (int i = 0; i < split.length; i++) {
				lines.add(normalizePadding(split[i]));
			}

			for (int i = 0; i < settings.getBorderWidthAsInt() - 1; i++) {
				lines.add(normalizePadding(split[split.length - 1]));
			}

			selectedText = StringUtils.join(lines, '\n');
		} else if (settings.isBottomBorder()) {
			String[] split = selectedText.split("\n");
			ArrayList<String> lines = new ArrayList<>();

			for (int i = 1 + topBottomPadding; i < split.length; i++) {
				lines.add(normalizePadding(split[i]));
			}

			for (int i = 0; i < settings.getBorderWidthAsInt() - 1; i++) {
				lines.add(normalizePadding(split[split.length - 1]));
			}

			selectedText = StringUtils.join(lines, '\n');
		}

		return selectedText;
	}

	private String normalizePadding(String s) {
		return s.substring(1, s.length() - 1);
	}


	static int getCellWidth(Collection<String> cell) {
		int width = 0;
		for (String line : cell) {
			width = Math.max(width, line.length());
		}
		return width;
	}

	static int getCellHeight(Collection<String> cell) {
		return cell.size();
	}
}

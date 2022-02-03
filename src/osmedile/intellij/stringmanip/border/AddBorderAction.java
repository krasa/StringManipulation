package osmedile.intellij.stringmanip.border;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.SimpleTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import java.util.Collection;
import java.util.List;

import static com.inamik.text.tables.Cell.Functions.*;
import static com.intellij.openapi.util.text.StringUtil.repeatSymbol;

public class AddBorderAction extends MyEditorAction {

	public AddBorderAction() {
		super(null);
		setupHandler(new MyEditorWriteActionHandler<BorderSettings>(getActionClass()) {
			@NotNull
			@Override
			protected Pair<Boolean, BorderSettings> beforeWriteAction(Editor editor, DataContext dataContext) {
				SelectionModel selectionModel = editor.getSelectionModel();
				if (!selectionModel.hasSelection()) {
					selectionModel.setSelection(0, editor.getDocument().getTextLength());
				} else {
					String selectedText = selectionModel.getSelectedText(true);
					if (selectedText != null && !selectedText.contains("\n")) {
						selectionModel.setSelection(0, editor.getDocument().getTextLength());
					}
				}
				BorderSettings settings = configure(editor);
				if (settings == null) return stopExecution();

				return continueExecution(settings);
			}


			@Override
			protected void executeWriteAction(Editor editor, DataContext dataContext, BorderSettings settings) {
				List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
				for (CaretState caretsAndSelection : caretsAndSelections) {
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();

					int startOffset = editor.logicalPositionToOffset(selectionStart);
					int endOffset = editor.logicalPositionToOffset(selectionEnd);
					final String selectedText = editor.getDocument().getText(new TextRange(startOffset, endOffset));

					final String s = transform(settings, selectedText, EditorUtil.getTabSize(editor));
					editor.getDocument().replaceString(startOffset, endOffset, s);
					postProcess(editor, settings);
				}

			}
		});
	}

	protected void postProcess(Editor editor, BorderSettings grepSettings) {

	}

	protected BorderSettings configure(Editor editor) {
		BorderSettings settings = getSettings();

		final BorderDialog dialog = new BorderDialog(this, settings, editor);

		if (!dialog.showAndGet(editor.getProject(), StringManipulationBundle.message("grep"), "StringManipulation.BorderDialog")) {
			return null;
		}
		BorderSettings newSettings = dialog.getSettings();
		storeSettings(newSettings);
		return newSettings;

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
					.nextCell(cell)
					// Expand to current height + 5, adding new lines to TOP
					//
					.applyToCell(TOP_PAD.withHeight(cellHeight + padding))
					// Expand to new-current-height + 5, adding new lines to the BOTTOM
					//
					.applyToCell(BOTTOM_PAD.withHeight(cellHeight + padding + padding))
					// Expand to current width + 5, adding new lines to the LEFT
					//
					// Expand to new-current-width + 5, adding new lines to the RIGHT
					//
					.applyToCell(RIGHT_PAD.withWidth(cellWidth + padding))
					.applyToCell(LEFT_PAD.withWidth(cellWidth + padding + padding));
			GridTable g = simpleTable.toGrid();


			if (settings.isBorderSingle()) {
				g = Border.SINGLE_LINE.apply(g);
			} else if (settings.isBorderDouble()) {
				g = Border.DOUBLE_LINE.apply(g);
			} else if (settings.isBorderCustom()) {
				g = Border.of(Border.Chars.of(settings.getBorderChar(i))).apply(g);
			}

			selectedText = Util.asString(g);
			selectedText = selectedText.replace("\r\n", "\n");
		}
		return selectedText;
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

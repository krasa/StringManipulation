package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.sort.support.SortLine;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.support.SortTypeDialog;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SortLinesBySubSelectionAction extends MyEditorAction {
	public static final String STORE_KEY = "StringManipulation.SortLinesBySubSelectionAction.SortSettings";


	public SortLinesBySubSelectionAction() {
		this(true);
	}

	public SortLinesBySubSelectionAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new MyEditorWriteActionHandler<SortSettings>(getActionClass()) {
				@NotNull
				@Override
				protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
					SortSettings settings = null;
					if (editor.getCaretModel().getCaretCount() > 1) {
						settings = getSortSettings(editor);
					} else {
						Messages.showInfoMessage(editor.getProject(), StringManipulationBundle.message("dialog.message.you.must.have.multiple.selections.carets.on.multiple.lines"), StringManipulationBundle.message("dialog.title.sort.by.subselection"));
					}

					if (settings == null) return stopExecution();
					return continueExecution(settings);
				}


				@Override
				protected void executeWriteAction(Editor editor, DataContext dataContext, SortSettings sortSettings) {
					List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
					IdeUtils.sort(caretsAndSelections);
					filterCarets(editor, caretsAndSelections);

					if (caretsAndSelections.size() > 1) {
						processMultiCaret(editor, sortSettings, caretsAndSelections);
					}
				}

			});
		}
	}

	@SuppressWarnings("Duplicates")
	@Nullable
	protected SortSettings getSortSettings(final Editor editor) {
		final SortTypeDialog dialog = new SortLinesBySubSelectionActionDialog(editor);
		if (!dialog.showAndGet(editor.getProject(), StringManipulationBundle.message("sort.lines.by.subselection"), "StringManipulation.SortLinesBySubSelection")) {
			return null;
		}
		SortSettings sortSettings = dialog.getSettings().preserveLeadingSpaces(false).preserveTrailingSpecialCharacters(false);
		PluginPersistentStateComponent.getInstance().storeSortSettings(STORE_KEY, sortSettings);
		return sortSettings;
	}

	public void filterCarets(Editor editor, List<CaretState> caretsAndSelections) {
		int previousLineNumber = -1;
		Iterator<CaretState> iterator = caretsAndSelections.iterator();
		while (iterator.hasNext()) {
			CaretState caretsAndSelection = iterator.next();
			LogicalPosition caretPosition = caretsAndSelection.getCaretPosition();
			int lineNumber = editor.getDocument().getLineNumber(
					editor.logicalPositionToOffset(caretPosition));
			if (lineNumber == previousLineNumber) {
				Caret caret = getCaretAt(editor, caretsAndSelection.getCaretPosition());
				editor.getCaretModel().removeCaret(caret);
				iterator.remove();
			}
			previousLineNumber = lineNumber;
		}
	}

	protected Caret getCaretAt(Editor editor, LogicalPosition position) {
		List<Caret> allCarets = editor.getCaretModel().getAllCarets();
		for (Caret caret : allCarets) {
			if (caret.getLogicalPosition().equals(position)) {
				return caret;
			}
		}
		throw new IllegalStateException("caret not found for " + position + "allCarets:" + allCarets);
	}

	private void processMultiCaret(Editor editor, @NotNull SortSettings sortSettings, List<CaretState> caretsAndSelections) {
		List<SubSelectionSortLine> lines = getLines(editor, sortSettings, caretsAndSelections);

		List<SubSelectionSortLine> sortedLines = sortSettings.getSortType().sortLines(lines, sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());

		write(editor, lines, sortedLines);
	}

	@NotNull
	private List<SubSelectionSortLine> getLines(Editor editor, @NotNull SortSettings sortSettings, List<CaretState> caretsAndSelections) {
		List<SubSelectionSortLine> lines = new ArrayList<SubSelectionSortLine>();
		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			int selectionStartOffset = editor.logicalPositionToOffset(selectionStart);
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			int selectionEndOffset = editor.logicalPositionToOffset(selectionEnd);
			LogicalPosition caretPosition = caretsAndSelection.getCaretPosition();
			// no selection -> expand to end of line
			if (selectionStartOffset == selectionEndOffset) {
				String text = editor.getDocument().getText();
				selectionEndOffset = text.indexOf("\n", selectionStartOffset);
				if (selectionEndOffset == -1) {
					selectionEndOffset = text.length();
				}
				Caret caret = getCaretAt(editor, caretsAndSelection.getCaretPosition());
				caret.setSelection(selectionStartOffset, selectionEndOffset);
			}

			String selection = editor.getDocument().getText(
					new TextRange(selectionStartOffset, selectionEndOffset));

			int lineNumber = editor.getDocument().getLineNumber(selectionStartOffset);
			int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);
			int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
			String line = editor.getDocument().getText(new TextRange(lineStartOffset, lineEndOffset));

			lines.add(new SubSelectionSortLine(sortSettings, line, selection, lineStartOffset, lineEndOffset,
					selectionStartOffset - lineStartOffset, selectionEndOffset - lineStartOffset
			));
		}
		return lines;
	}

	private void write(Editor editor, List<SubSelectionSortLine> lines, List<SubSelectionSortLine> sortedLines) {
		List<Caret> allCarets = editor.getCaretModel().getAllCarets();
		for (int i = lines.size() - 1; i >= 0; i--) {
			SubSelectionSortLine oldLine = lines.get(i);
			SubSelectionSortLine newLine = sortedLines.get(i);
			int lineStartOffset = oldLine.lineStartOffset;
			editor.getDocument().replaceString(lineStartOffset, oldLine.lineEndOffset, newLine.line);
			int startColumn = newLine.selectionStartLineOffset;
			int endColumn = newLine.selectionEndLineOffset;

			Caret caret = allCarets.get(i);
			caret.setSelection(lineStartOffset + startColumn, lineStartOffset + endColumn);
			caret.moveToOffset(lineStartOffset + startColumn);
		}
	}

	private class SubSelectionSortLine extends SortLine {
		private final String line;
		private final int lineStartOffset;
		private final int lineEndOffset;
		private final int selectionStartLineOffset;
		private final int selectionEndLineOffset;

		public SubSelectionSortLine(SortSettings sortSettings, String line, String selection, int lineStartOffset, int lineEndOffset,
									int selectionStartLineOffset, int selectionEndLineOffset) {
			super(selection, sortSettings);
			this.line = line;
			this.lineStartOffset = lineStartOffset;
			this.lineEndOffset = lineEndOffset;
			this.selectionStartLineOffset = selectionStartLineOffset;
			this.selectionEndLineOffset = selectionEndLineOffset;
		}
	}

	private class SortLinesBySubSelectionActionDialog extends SortTypeDialog<List<SubSelectionSortLine>> {

		public SortLinesBySubSelectionActionDialog(Editor editor) {
			super(PluginPersistentStateComponent.getInstance().getSortSettings(SortLinesBySubSelectionAction.STORE_KEY), false, editor);
		}

		@Override
		protected List<SubSelectionSortLine> preparePreviewInput(Editor editor) {
			List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
			IdeUtils.sort(caretsAndSelections);
			List<SubSelectionSortLine> lines = getLines(editor, getSettings(), caretsAndSelections);
			return lines;
		}

		@Override
		protected List<String> sortPreview(Editor editor1, SortSettings settings, List<SubSelectionSortLine> input, @Nullable Project project) {
			List<SubSelectionSortLine> sortedLines = settings.getSortType().sortLines(input, settings.getBaseComparator(), settings.getCollatorLanguageTag());

			List<String> result = new ArrayList<>();
			for (SubSelectionSortLine sortedLine : sortedLines) {
				result.add(sortedLine.line);
			}
			return result;
		}
	}
}
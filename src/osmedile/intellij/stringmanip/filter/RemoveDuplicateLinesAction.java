package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler2;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.utils.ActionUtils;
import osmedile.intellij.stringmanip.utils.Cloner;
import osmedile.intellij.stringmanip.utils.FilterUtils;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.*;

public class RemoveDuplicateLinesAction extends MyEditorAction {

	public RemoveDuplicateLinesAction() {
		super(null);
		setupHandler(new MultiCaretHandlerHandler2<RemoveDuplicatesSettings>(getActionClass()) {
			@Override
			public boolean executeLater(@NotNull Editor editor, DataContext dataContext) {
				return true;
			}

			@NotNull
			@Override
			protected Pair<Boolean, RemoveDuplicatesSettings> beforeWriteAction(Editor editor, DataContext dataContext) {
				SelectionModel selectionModel = editor.getSelectionModel();
				if (!selectionModel.hasSelection()) {
					selectionModel.setSelection(0, editor.getDocument().getTextLength());
				}
				if (ActionUtils.countSelections(editor) > 1) {
					RemoveDuplicatesSettings settings = getSettings(editor);
					if (settings == null) return stopExecution();
					return continueExecution(settings);
				}

				return continueExecution(new RemoveDuplicatesSettings());
			}

			@Override
			protected String processSingleSelection(Editor editor, String text, RemoveDuplicatesSettings settings) {
				String[] split = text.split("\n");
				String[] uniqueLines = FilterUtils.filterDuplicates(split);
				return join(uniqueLines);
			}

			protected void multiSelection(Editor editor, List<CaretState> caretsAndSelections, RemoveDuplicatesSettings settings) {
				if (settings.getType() == RemoveDuplicatesSettings.Type.REMOVE_SELECTION) {
					super.multiSelection(editor, caretsAndSelections, settings);
				} else {
					IdeUtils.sort(caretsAndSelections);
					filterCarets(editor, caretsAndSelections);
					List<SubSelectionLine> lines = getLines(editor, caretsAndSelections);

					filterLines(lines);
					write(editor, lines, settings);
				}

			}

			private void filterLines(List<SubSelectionLine> lines) {
				Set<String> set = new HashSet<String>();
				for (SubSelectionLine line : lines) {
					boolean unique = set.add(line.selection);
					if (!unique) {
						line.remove = true;
					}
				}
			}

			@Override
			protected List<String> processMultiSelections(Editor editor, List<String> lines, RemoveDuplicatesSettings settings) {
				String[] split = lines.toArray(new String[0]);
				String[] uniqueLines = FilterUtils.filterDuplicates(split);
				return Arrays.asList(uniqueLines);
			}

			private void write(Editor editor, List<SubSelectionLine> lines, RemoveDuplicatesSettings settings) {
				List<Caret> allCarets = editor.getCaretModel().getAllCarets();
				for (int i = lines.size() - 1; i >= 0; i--) {
					SubSelectionLine line = lines.get(i);

					if (line.remove) {
						int lineEndOffset = line.lineEndOffset;
						if (settings.getType() == RemoveDuplicatesSettings.Type.REMOVE_LINE) {
							lineEndOffset++;
						}
						editor.getDocument().replaceString(line.lineStartOffset, lineEndOffset, "");
						editor.getCaretModel().removeCaret(allCarets.get(i));
					}
				}
			}

			@NotNull
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

			private List<SubSelectionLine> getLines(Editor editor, List<CaretState> caretsAndSelections) {
				List<SubSelectionLine> lines = new ArrayList<SubSelectionLine>();
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

					lines.add(new SubSelectionLine(line, selection, lineStartOffset, lineEndOffset,
							selectionStartOffset - lineStartOffset, selectionEndOffset - lineStartOffset
					));
				}
				return lines;
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

			private String join(String[] array) {
				StringBuilder buf = new StringBuilder();
				for (int i = 0; i < array.length; i++) {
					if (array[i] != null && i > 0) {
						buf.append("\n");
					}
					if (array[i] != null) {
						buf.append(array[i]);
					}
				}
				return buf.toString();
			}


		});
	}

	@SuppressWarnings("Duplicates")
	@Nullable
	protected RemoveDuplicatesSettings getSettings(final Editor editor) {
		final RemoveDuplicatesDialog dialog = new RemoveDuplicatesDialog(getSettings(), editor);
		if (!dialog.showAndGet(editor.getProject(), "Remove Duplicates", "StringManipulation.RemoveDuplicatesDialog")) {
			return null;
		}
		RemoveDuplicatesSettings newSettings = dialog.getSettings();
		storeSettings(newSettings);
		return newSettings;
	}

	protected void storeSettings(RemoveDuplicatesSettings newSettings) {
		PluginPersistentStateComponent.getInstance().setRemoveDuplicatesSettings(newSettings);
	}

	protected RemoveDuplicatesSettings getSettings() {
		RemoveDuplicatesSettings sortSettings = PluginPersistentStateComponent.getInstance().getRemoveDuplicatesSettings();
		return Cloner.deepClone(sortSettings);
	}
}
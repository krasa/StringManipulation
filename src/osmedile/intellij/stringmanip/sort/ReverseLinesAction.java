package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;
import osmedile.intellij.stringmanip.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReverseLinesAction extends EditorAction {

	protected ReverseLinesAction() {
		this(true);
	}

	protected ReverseLinesAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {

				public void executeWriteAction(Editor editor, DataContext dataContext) {
					List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();

					if (caretsAndSelections.size() > 1) {
						processMultiCaret(editor, caretsAndSelections);
					} else if (caretsAndSelections.size() == 1) {
						processSingleSelection(editor, caretsAndSelections);
					}
				}

				private void processSingleSelection(Editor editor, List<CaretState> caretsAndSelections) {
					CaretState caretsAndSelection = caretsAndSelections.get(0);
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
					String text = editor.getDocument().getText(
							new TextRange(editor.logicalPositionToOffset(selectionStart),
									editor.logicalPositionToOffset(selectionEnd)));

					String charSequence = reverse(text);

					editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd), charSequence);
				}

				private void processMultiCaret(Editor editor, List<CaretState> caretsAndSelections) {
					List<String> lines = new ArrayList<String>();
					for (CaretState caretsAndSelection : caretsAndSelections) {
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						String text = editor.getDocument().getText(
								new TextRange(editor.logicalPositionToOffset(selectionStart),
										editor.logicalPositionToOffset(selectionEnd)));
						lines.add(text);
					}

					lines = reverse(lines);

					for (int i = lines.size() - 1; i >= 0; i--) {
						String line = lines.get(i);
						CaretState caretsAndSelection = caretsAndSelections.get(i);
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
								editor.logicalPositionToOffset(selectionEnd), line);
					}
				}
			});
		}
	}

	private List<String> reverse(List<String> lines) {
		Collections.reverse(lines);
		return lines;
	}

	private String reverse(String text) {
		String[] split = text.split("\n");

		List<String> list = Arrays.asList(split);
		reverse(list);

		String join = StringUtils.join(split, '\n');
		if (text.endsWith("\n")) {
			join = join + "\n";
		}
		return join;
	}
}

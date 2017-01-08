package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlignToColumnsAction extends EditorAction {
	public static final Key<String> KEY = Key.create("StringManipulation.AlignToColumnsAction.UserData");

	private String lastSeparator = ",";

	protected AlignToColumnsAction() {
		this(true);
	}

	protected AlignToColumnsAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {
				@Override
				public void doExecute(Editor editor, @Nullable Caret caret, DataContext dataContext) {
					String separator = chooseSeparator();
					if (separator == null)
						return;
					try {
						editor.putUserData(KEY, separator);
						super.doExecute(editor, caret, dataContext);
					} finally {
						editor.putUserData(KEY, null);
					}
				}

				@Override
				public void executeWriteAction(Editor editor, DataContext dataContext) {
					List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
					String separator = editor.getUserData(KEY);
					if (caretsAndSelections.size() > 1) {
						processMultiCaret(editor, separator, caretsAndSelections);
					} else if (caretsAndSelections.size() == 1) {
						processSingleSelection(editor, separator, caretsAndSelections);
					}
				}

				private String chooseSeparator() {
					String separator = Messages.showInputDialog("Separator", "Separator", Messages.getQuestionIcon(),
							lastSeparator, null);
					if (separator != null) {
						if (separator.equals("")) {
							separator = " ";
						}
						lastSeparator = separator;
					} else {
						return null;
					}
					return separator;
				}

				private void processSingleSelection(Editor editor, String separator, List<CaretState> caretsAndSelections) {
					CaretState caretsAndSelection = caretsAndSelections.get(0);
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
					String text = editor.getDocument().getText(
							new TextRange(editor.logicalPositionToOffset(selectionStart),
									editor.logicalPositionToOffset(selectionEnd)));

					String charSequence = new ColumnAligner().reformat(separator, text);
					editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd), charSequence);
				}

				private void processMultiCaret(Editor editor, String separator, List<CaretState> caretsAndSelections) {
					List<ColumnAlignerLine> lines = new ArrayList<ColumnAlignerLine>();
					for (CaretState caretsAndSelection : caretsAndSelections) {
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						String text = editor.getDocument().getText(
								new TextRange(editor.logicalPositionToOffset(selectionStart),
										editor.logicalPositionToOffset(selectionEnd)));
						lines.add(new ColumnAlignerLine(separator, text, text.endsWith("\n")));
					}

					new ColumnAligner().process(lines);
					for (int i = lines.size() - 1; i >= 0; i--) {
						ColumnAlignerLine line = lines.get(i);
						CaretState caretsAndSelection = caretsAndSelections.get(i);
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
								editor.logicalPositionToOffset(selectionEnd), line.sb.toString().trim());
					}
				}
			});
		}

	}


}

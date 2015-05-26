package osmedile.intellij.stringmanip;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;

public class RemoveDuplicateLinesAction extends EditorAction {

	private static final Key<Set<String>> KEY = Key.create("StringManipulation.RemoveDuplicateLines");

	public RemoveDuplicateLinesAction() {
		super(new EditorWriteActionHandler(true) {
			public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {

				// Column mode not supported
				if (editor.isColumnMode()) {
					return;
				}
				final SelectionModel selectionModel = editor.getSelectionModel();
				boolean hasSelection = selectionModel.hasSelection();
				if (!hasSelection) {
					return;
				}
				final String selectedText = selectionModel.getSelectedText();
				if (selectedText != null) {
					int selectionStart = selectionModel.getSelectionStart();
					int selectionEnd = selectionModel.getSelectionEnd();

					String[] split = selectedText.split("\n");
					String[] uniqueLines = filter(editor, split);
					naiveCleanup(editor, caret);

					String result = join(uniqueLines, selectedText.length());

					editor.getDocument().replaceString(selectionStart, selectionEnd, result);
					editor.getCaretModel().moveToOffset(selectionStart + result.length());
					selectionModel.setSelection(selectionStart, selectionStart + result.length());
				}
			}

			private String join(String[] array, int length) {
				StringBuilder buf = new StringBuilder(length);
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

			private String[] filter(UserDataHolder dataContext, String[] split) {
				if (split.length > 0) {
					Set<String> set = KEY.get(dataContext);
					if (set == null) {
						set = new HashSet<String>();
						dataContext.putUserData(KEY, set);
					}
					for (int i = 0; i < split.length; i++) {
						boolean unique = set.add(split[i]);
						if (!unique) {
							split[i] = null;
						}
					}
					return split;
				}
				return split;
			}

			private void naiveCleanup(Editor editor, Caret caret) {
				List<Caret> allCarets = editor.getCaretModel().getAllCarets();
				if (allCarets.get(0) == caret) {
					editor.putUserData(KEY, null);
				}
			}
		});
	}
}
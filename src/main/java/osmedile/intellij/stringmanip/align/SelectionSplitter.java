package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * from https://github.com/danielkurecka/intellij-extra-actions
 */
public class SelectionSplitter {

	/**
	 * Splits selection into multiple carets by given separator.
	 */
	public static void split(Editor editor, String separator) {
		ArrayList<CaretState> newCarets = new ArrayList<>();
		int sepLength = separator.length();
		String sepEscaped = Pattern.quote(separator);

		for (Caret caret : editor.getCaretModel().getAllCarets()) {
			String selectedText = caret.getSelectedText() != null ? caret.getSelectedText() : "";
			String[] parts = selectedText.split(sepEscaped, -1);
			int offset = caret.getSelectionStart();

			for (String part : parts) {
				int start = offset;
				int end = offset + part.length();
				offset = end + sepLength;
				newCarets.add(createCaretState(editor, end, start, end));
			}
		}

		editor.getCaretModel().setCaretsAndSelections(newCarets);
	}

	private static CaretState createCaretState(Editor editor, int offset, int selStar, int selEnd) {
		LogicalPosition position = editor.offsetToLogicalPosition(offset);
		LogicalPosition start = editor.offsetToLogicalPosition(selStar);
		LogicalPosition end = editor.offsetToLogicalPosition(selEnd);
		return new CaretState(position, start, end);
	}

}

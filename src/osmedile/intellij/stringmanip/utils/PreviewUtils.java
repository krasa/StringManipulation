package osmedile.intellij.stringmanip.utils;

import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewUtils {

	@NotNull
	public static List<String> getPreviewLines(Editor editor) {
		String text = getTextForPreview(editor);
		String[] split = text.split("\n");
		return new ArrayList<String>(Arrays.asList(split));
	}

	@NotNull
	public static String getTextForPreview(Editor editor) {
		List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
		StringBuilder sb = new StringBuilder();
		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			String text = editor.getDocument().getText(
				new TextRange(editor.logicalPositionToOffset(selectionStart),
					editor.logicalPositionToOffset(selectionEnd)));

			sb.append(text);
			if (sb.length() > 10000) {
				break;
			}
		}
		String s = sb.toString();
		s = s.substring(0, Math.min(10000, s.length()));
		return s;
	}
}

package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ShuffleCharactersAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, Map actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		return shuffleString(selectedText);
	}

	@Override
	public String transformByLine(Map actionContext, String s) {
		return null;
	}

	public static String shuffleString(String string) {
		List<String> letters = Arrays.asList(string.split(""));
		Collections.shuffle(letters);
		StringBuilder shuffled = new StringBuilder();
		for (String letter : letters) {
			shuffled.append(letter);
		}
		return shuffled.toString();
	}
}

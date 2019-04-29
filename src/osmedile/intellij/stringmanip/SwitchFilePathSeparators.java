package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

public class SwitchFilePathSeparators extends AbstractStringManipAction {

	@Override
	public String transformSelection(Editor editor, DataContext dataContext, String s, Object additionalParam) {
		String s1;
		if (s.contains("/")) {
			s1 = s.replace("/", "\\");
		} else {
			s1 = s.replace("\\", "/");
		}
		return s1;
	}

	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}
}

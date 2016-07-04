package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang.NotImplementedException;

public class SwitchFilePathSeparators extends AbstractStringManipAction {

	public String transformSelection(Editor editor, DataContext dataContext, String s) {
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
		throw new NotImplementedException();
	}
}

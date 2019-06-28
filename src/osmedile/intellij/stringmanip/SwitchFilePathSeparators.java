package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

public class SwitchFilePathSeparators extends AbstractStringManipAction {

    protected SwitchFilePathSeparators() {
    }

    protected SwitchFilePathSeparators(boolean setupHandler) {
        super(setupHandler);
    }

	@Override
	public String transformByLine(String s) {
        String s1;
        if (s.contains("/")) {
            s1 = s.replace("/", "\\");
        } else {
            s1 = s.replace("\\", "/");
        }
        return s1;
	}
}

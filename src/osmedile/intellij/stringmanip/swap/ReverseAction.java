package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang3.StringUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class ReverseAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String s, Object additionalParam) {
		return StringUtils.reverse(s);
	}

	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}

}

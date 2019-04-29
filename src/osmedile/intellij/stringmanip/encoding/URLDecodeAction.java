package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.EncodingUtils;

public class URLDecodeAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, Object additionalParam) {
		return EncodingUtils.decodeUrl(selectedText);
	}

	@Override
	public String transformByLine(String s) {
		throw new UnsupportedOperationException();
	}
}
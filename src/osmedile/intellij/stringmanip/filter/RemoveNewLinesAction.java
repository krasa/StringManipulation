package osmedile.intellij.stringmanip.filter;

import org.apache.commons.lang.NotImplementedException;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class RemoveNewLinesAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText) {
		return selectedText.replaceAll("\n", "");
	}

	@Override
	public String transformByLine(String s) {
		throw new NotImplementedException();
	}

}
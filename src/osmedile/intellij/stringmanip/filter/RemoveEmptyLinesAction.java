package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.apache.commons.lang3.StringUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: RemoveEmptyLinesAction.java 60 2008-04-18 06:51:03Z osmedile $
 */
public class RemoveEmptyLinesAction extends AbstractStringManipAction<Object> {

	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, DataContext dataContext, SelectionModel selectionModel) {
		selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
		return true;
	}

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		String[] textParts = selectedText.split("\n");
		Collection<String> result = new ArrayList<String>();

		for (String textPart : textParts) {
			if (StringUtils.isNotBlank(textPart)) {
				result.add(textPart);
			}
		}

		String[] res = result.toArray(new String[result.size()]);

		final String s = StringUtils.join(res, '\n');
		return s;
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}

}

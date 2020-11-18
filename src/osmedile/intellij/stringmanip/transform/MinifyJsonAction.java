package osmedile.intellij.stringmanip.transform;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.json.JSONObject;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class MinifyJsonAction extends AbstractStringManipAction<Object> {
	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		return new JSONObject(selectedText).toString();
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new RuntimeException();
	}
}
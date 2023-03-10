package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class RemoveExtraEmptyLinesAction extends RemoveEmptyLinesAction {


	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		String[] textParts = selectedText.split("\n");
		Collection<String> result = new ArrayList<String>();

		boolean removeNext = false;
		for (String textPart : textParts) {
			if (StringUtils.isBlank(textPart)) {
				if (removeNext) {
					continue;
				} else {
					result.add(textPart);
					removeNext = true;
				}
			} else {
				result.add(textPart);
				removeNext = false;
			}
		}

		String[] res = result.toArray(new String[result.size()]);

		final String s = StringUtils.join(res, '\n');
		return s;
	}


}

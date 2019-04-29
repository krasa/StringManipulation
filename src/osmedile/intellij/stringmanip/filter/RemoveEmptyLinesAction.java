package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.MyApplicationComponent;
import osmedile.intellij.stringmanip.MyEditorAction;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Olivier Smedile
 * @version $Id: RemoveEmptyLinesAction.java 60 2008-04-18 06:51:03Z osmedile $
 */
public class RemoveEmptyLinesAction extends MyEditorAction {

	public RemoveEmptyLinesAction() {
		super(null);
		setupHandler(new EditorWriteActionHandler(true) {

			@Override
			public void executeWriteAction(Editor editor, DataContext dataContext) {
				MyApplicationComponent.setAction(getActionClass());

				// Column mode not supported
				if (editor.isColumnMode()) {
					return;
				}

				final SelectionModel selectionModel = editor.getSelectionModel();
				if (selectionModel.hasSelection()) {

					final String selectedText = selectionModel.getSelectedText();

					String[] textParts = selectedText.split("\n");
					Collection<String> result = new ArrayList<String>();

					for (String textPart : textParts) {
						if (StringUtils.isNotBlank(textPart)) {
							result.add(textPart);
						}
					}

					String[] res = result.toArray(new String[result.size()]);

					final String s = StringUtils.join(res, '\n');
					editor.getDocument().replaceString(selectionModel.getSelectionStart(),
						selectionModel.getSelectionEnd(), s);
				}

			}
		});
	}
}
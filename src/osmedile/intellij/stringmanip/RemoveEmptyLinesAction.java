package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Olivier Smedile
 * @version $Id: RemoveEmptyLinesAction.java 60 2008-04-18 06:51:03Z osmedile $
 */
public class RemoveEmptyLinesAction extends EditorAction {

    public RemoveEmptyLinesAction() {
        super(new EditorWriteActionHandler() {
            public void executeWriteAction(Editor editor, DataContext dataContext) {

                //Column mode not supported
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
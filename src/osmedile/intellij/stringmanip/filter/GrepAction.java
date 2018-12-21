package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;
import osmedile.intellij.stringmanip.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Olivier Smedile
 * @version $Id: GrepAction.java 60 2008-04-18 06:51:03Z osmedile $
 */
public class GrepAction extends MyEditorAction {

	public GrepAction() {
		this(String::contains);
	}

	interface GrepFilter {
		boolean execute(String text, String grepos);
	}

	public GrepAction(final GrepFilter shouldAdd) {
		super(null);
		setupHandler(new MyEditorWriteActionHandler<String>(getActionClass()) {
			@NotNull
			@Override
			protected Pair<Boolean, String> beforeWriteAction(Editor editor, DataContext dataContext) {
				final SelectionModel selectionModel = editor.getSelectionModel();
				if (selectionModel.hasSelection()) {
					String grepos = Messages.showInputDialog(editor.getProject(), "过滤文本", "过滤", null);
					if (!StringUtil.isEmptyOrSpaces(grepos)) {
						return continueExecution(grepos);
					}
				} else {
					Messages.showInfoMessage(editor.getProject(), "请先选定要过滤的文本", "过滤");
				}
				return stopExecution();
			}

			@Override
			protected void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, String grepos) {
				// Column mode not supported
				if (editor.isColumnMode()) {
					return;
				}

				final SelectionModel selectionModel = editor.getSelectionModel();
				if (selectionModel.hasSelection()) {

					if (StringUtil.isEmptyOrSpaces(grepos)) {
						return;
					}
					final String selectedText = selectionModel.getSelectedText();

					String[] textParts = selectedText.split("\n");
					Collection<String> result = new ArrayList<String>();

					for (String textPart : textParts) {
						if (shouldAdd.execute(textPart, grepos)) {
							result.add(textPart);
						}
					}

					String[] res = result.toArray(new String[result.size()]);

					final String s = StringUtils.join(res, '\n');
					editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), s);
				}

			}
		});
	}
}
package osmedile.intellij.stringmanip.actions;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actions.TextComponentEditorAction;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;

public class SelectWordInDifferentHumpsModeAction extends TextComponentEditorAction implements DumbAware {

	protected SelectWordInDifferentHumpsModeAction() {
		super(new MyEditorWriteActionHandler<String>(SelectWordInDifferentHumpsModeAction.class) {
			@Override
			protected void executeWriteAction(Editor editor, DataContext dataContext, @Nullable String additionalParameter) {
				EditorSettings settings = editor.getSettings();
				boolean camelWords = settings.isCamelWords();
				try {
					settings.setCamelWords(!camelWords);
					EditorAction editorSelectWord = (EditorAction) ActionManager.getInstance().getAction("EditorSelectWord");
					editorSelectWord.getHandler().execute(editor, null, dataContext);
				} finally {
					settings.setCamelWords(camelWords);
				}
			}
		});
		setInjectedContext(true);
	}

}

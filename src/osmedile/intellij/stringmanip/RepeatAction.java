package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RepeatAction extends EditorAction {

	public RepeatAction() {
		super(new MyEditorWriteActionHandler(null) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {

				AnAction anAction = MyApplicationComponent.getInstance().getAnAction();
				if (anAction != null) {
					anAction.actionPerformed(new AnActionEvent(null, dataContext, ActionPlaces.UNKNOWN, new Presentation(), ActionManager.getInstance(), 0));
				}
				return stopExecution();
			}

			@Override
			protected void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, @Nullable Object additionalParameter) {

			}
		});
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		AnAction anAction = MyApplicationComponent.getInstance().getAnAction();
		if (anAction != null) {
			e.getPresentation().setEnabled(true);
			e.getPresentation().setText("重复 - " + anAction.getTemplatePresentation().getText());
		} else {
			e.getPresentation().setText("重复最后的操作");
			e.getPresentation().setEnabled(false);
		}
	}
}

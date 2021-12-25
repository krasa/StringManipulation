package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RepeatActionSetShortcut extends AnAction implements DumbAware {
	public RepeatActionSetShortcut() {
	}

	public RepeatActionSetShortcut(@Nullable @NlsActions.ActionText String text) {
		super(text);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
		AnAction anAction = MyApplicationService.getInstance().getAnAction();
		if (anAction != null) {
			String id = ActionManager.getInstance().getId(anAction);
			EditKeymapsDialog dialog = new EditKeymapsDialog(null, id);
			ApplicationManager.getApplication().invokeLater(dialog::show);
		}
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		AnAction anAction = MyApplicationService.getInstance().getAnAction();
		e.getPresentation().setVisible(anAction != null);
	}
}

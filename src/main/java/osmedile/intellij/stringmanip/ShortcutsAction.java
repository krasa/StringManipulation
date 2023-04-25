package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShortcutsAction extends AnAction implements DumbAware {
	public ShortcutsAction() {
	}

	public ShortcutsAction(@Nullable @NlsActions.ActionText String text) {
		super(text);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
		Pair<AnAction, Object> pair = MyApplicationService.getInstance().getLastAction();
		AnAction anAction = pair.getFirst();
		String id = "StringManipulation.Group.Main";
		if (anAction != null) {
			id = ActionManager.getInstance().getId(anAction);
		}
		EditKeymapsDialog dialog = new EditKeymapsDialog(null, id);
		ApplicationManager.getApplication().invokeLater(dialog::show);
	}

}

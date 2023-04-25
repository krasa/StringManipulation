package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SaveAsAction extends AnAction implements DumbAware {
	public SaveAsAction() {
	}

	public SaveAsAction(@Nullable @NlsActions.ActionText String text) {
		super(text);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
		//TODO
	}

}

package osmedile.intellij.stringmanip.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.RepeatAction;
import osmedile.intellij.stringmanip.RepeatActionSetShortcut;
import osmedile.intellij.stringmanip.StringManipulationBundle;

public class RepeatActionGroup extends ActionGroup implements DumbAware, UpdateInBackground {

	private final RepeatAction repeatAction;
	private final RepeatActionSetShortcut addShortcut;

	public RepeatActionGroup() {
		repeatAction = new RepeatAction();
		addShortcut = new RepeatActionSetShortcut(StringManipulationBundle.message("action.set.shortcut.text"));
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		repeatAction.actionPerformed(e);
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		repeatAction.update(e);
	}

	@Override
	public boolean canBePerformed(@NotNull DataContext context) {
		return true;
	}

	@Override
	public boolean isPopup() {
		return true;
	}

	@NotNull
	@Override
	public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
		return new AnAction[]{
				addShortcut
		};
	}

}

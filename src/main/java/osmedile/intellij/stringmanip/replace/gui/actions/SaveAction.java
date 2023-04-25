package osmedile.intellij.stringmanip.replace.gui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.replace.gui.CompositeForm;

public class SaveAction extends DumbAwareAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		CompositeForm form = CompositeForm.PANEL.getData(e.getDataContext());
		if (form != null) {
			form.addToHistory();
		}
	}
}

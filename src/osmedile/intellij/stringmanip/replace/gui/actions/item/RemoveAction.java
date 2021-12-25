package osmedile.intellij.stringmanip.replace.gui.actions.item;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.replace.gui.CompositeForm;
import osmedile.intellij.stringmanip.replace.gui.ItemForm;

public class RemoveAction extends DumbAwareAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		CompositeForm compositeForm = CompositeForm.PANEL.getData(e.getDataContext());
		ItemForm data = ItemForm.PANEL.getData(e.getDataContext());
		if (compositeForm != null && data != null) {
			compositeForm.remove(data);
		}
	}
}

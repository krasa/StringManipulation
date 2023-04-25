package osmedile.intellij.stringmanip.replace.gui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.replace.gui.CompositeForm;
import osmedile.intellij.stringmanip.replace.gui.HistoryForm;
import osmedile.intellij.stringmanip.replace.gui.ReplaceCompositeModel;

import javax.swing.*;

public class HistoryAction extends DumbAwareAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		Project myProject = e.getProject();
		if (myProject == null) {
			return;
		}

		CompositeForm form = CompositeForm.PANEL.getData(e.getDataContext());
		if (form == null) {
			return;
		}

		final HistoryForm historyForm = new HistoryForm();
		DialogWrapper dialogWrapper = new DialogWrapper(myProject, false) {
			{
				init();
				setTitle(StringManipulationBundle.message("history"));
			}

			@Nullable
			@Override
			protected String getDimensionServiceKey() {
				return "StringManipulation.ReplacementHistoryForm";
			}

			@Nullable
			@Override
			protected JComponent createCenterPanel() {
				return historyForm.root;
			}

			@Override
			protected void doOKAction() {
				super.doOKAction();
			}
		};

		boolean b = dialogWrapper.showAndGet();
		if (b) {
			ReplaceCompositeModel model = historyForm.getModel();
			if (model != null) {
				form.addToHistory();
				form.initModel(model);
			}
		}
	}

}

package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;

public class OpenSettingsAction extends DumbAwareAction {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(OpenSettingsAction.class);

	@Override
	public void actionPerformed(AnActionEvent e) {
		ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), "StringManipulation.MyConfigurable");
	}
}

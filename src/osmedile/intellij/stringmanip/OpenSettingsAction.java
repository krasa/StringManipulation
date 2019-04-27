package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;

public class OpenSettingsAction extends DumbAwareAction {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(OpenSettingsAction.class);

	@Override
	public void actionPerformed(AnActionEvent e) {
		MyConfigurable instance = new MyConfigurable();
		ShowSettingsUtil.getInstance().editConfigurable(e.getProject(), "StringManipulation.MyConfigurable", instance, true);
	}
}

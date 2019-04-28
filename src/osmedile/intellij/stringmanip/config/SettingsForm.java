package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;

public class SettingsForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(SettingsForm.class);

	private JPanel root;
	private JPanel settings;
	private JPanel customActions;
	private CaseSwitchingSettingsForm caseSwitchingSettingsForm;
	private CustomActionSettingsForm customActionSettingsForm;

	public SettingsForm() {
	}

	public JPanel getRoot() {
		return root;
	}

	public void dispose() {
		if (customActionSettingsForm != null) {
			customActionSettingsForm.dispose();
		}
	}

	public boolean isModified(PluginPersistentStateComponent state) {
		if (customActionSettingsForm.isModified()) return true;
		if (caseSwitchingSettingsForm.isModified(state.getCaseSwitchingSettings())) return true;
		return false;
	}

	public void getData(PluginPersistentStateComponent state) {
		customActionSettingsForm.getData();
		caseSwitchingSettingsForm.getData(state.getCaseSwitchingSettings());
	}

	public void setData(PluginPersistentStateComponent state) {
		customActionSettingsForm.setData();
		caseSwitchingSettingsForm.setData(state.getCaseSwitchingSettings());

	}

	private void createUIComponents() {
		caseSwitchingSettingsForm = new CaseSwitchingSettingsForm();
		settings = caseSwitchingSettingsForm.getRoot();
		customActionSettingsForm = new CustomActionSettingsForm();
		customActions = customActionSettingsForm.getRoot();
	}
}


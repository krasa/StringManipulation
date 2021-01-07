package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.diagnostic.Logger;
import osmedile.intellij.stringmanip.Donate;

import javax.swing.*;

public class SettingsForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(SettingsForm.class);

	private JPanel root;
	private JPanel settings;
	private JPanel customActions;
	private JCheckBox doNotAddSelection;
	private JPanel donatePanel;
	private CaseSwitchingSettingsForm caseSwitchingSettingsForm;
	private CustomActionSettingsForm customActionSettingsForm;

	public SettingsForm() {
		donatePanel.add(Donate.newDonateButton(donatePanel));
	}

	public JPanel getRoot() {
		return root;
	}

	public void dispose() {
		if (customActionSettingsForm != null) {
			customActionSettingsForm.dispose();
		}
	}

	public boolean isModified(PluginPersistentStateComponent data) {
		if (doNotAddSelection.isSelected() != data.isDoNotAddSelection()) return true;
		if (customActionSettingsForm.isModified()) return true;
		if (caseSwitchingSettingsForm.isModified(data.getCaseSwitchingSettings())) return true;
		return false;
	}

	public void getData(PluginPersistentStateComponent data) {
		data.setDoNotAddSelection(doNotAddSelection.isSelected());
		customActionSettingsForm.getData();
		caseSwitchingSettingsForm.getData(data.getCaseSwitchingSettings());
	}

	public void setData(PluginPersistentStateComponent data) {
		doNotAddSelection.setSelected(data.isDoNotAddSelection());
		customActionSettingsForm.setData();
		caseSwitchingSettingsForm.setData(data.getCaseSwitchingSettings());

	}

	private void createUIComponents() {
		caseSwitchingSettingsForm = new CaseSwitchingSettingsForm();
		settings = caseSwitchingSettingsForm.getRoot();
		customActionSettingsForm = new CustomActionSettingsForm();
		customActions = customActionSettingsForm.getRoot();
	}

}


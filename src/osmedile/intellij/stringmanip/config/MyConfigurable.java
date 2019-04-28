package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyApplicationComponent;

import javax.swing.*;

public class MyConfigurable implements SearchableConfigurable {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(MyConfigurable.class);
	private SettingsForm gui;
	private PluginPersistentStateComponent instance;

	public MyConfigurable() {
		instance = PluginPersistentStateComponent.getInstance();
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		if (gui == null) {
			gui = new SettingsForm();
		}
		return gui.getRoot();
	}

	@Override
	public void disposeUIResources() {
		if (gui != null) {
			gui.dispose();
		}
		gui = null;
	}

	@Nls
	@Override
	public String getDisplayName() {
		return "String Manipulation";
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		return null;
	}

	@NotNull
	@Override
	public String getId() {
		return "StringManipulation";
	}

	@Nullable
	@Override
	public Runnable enableSearch(String s) {
		return null;
	}

	@Override
	public boolean isModified() {
		return gui != null && gui.isModified(instance);
	}

	@Override
	public void apply() throws ConfigurationException {
		MyApplicationComponent.getInstance().unRegisterActions(instance.getCustomActionModels());

		gui.getData(instance);

		MyApplicationComponent.getInstance().registerActions();
	}

	@Override
	public void reset() {
		gui.setData(instance);
	}
}

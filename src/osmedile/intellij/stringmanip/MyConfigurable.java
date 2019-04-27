package osmedile.intellij.stringmanip;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		return gui.isModified(instance.getCaseSwitchingSettings());
	}

	@Override
	public void apply() throws ConfigurationException {
		gui.getData(instance.getCaseSwitchingSettings());
	}

	@Override
	public void reset() {
		gui.setData(instance.getCaseSwitchingSettings());
	}
}

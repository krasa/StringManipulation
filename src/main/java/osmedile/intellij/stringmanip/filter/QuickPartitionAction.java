package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

public class QuickPartitionAction extends QuickGrepAction {
	@Override
	protected GrepSettings getSettings(Editor editor, String initialValue) {
		if (!GrepAction.INITIAL_VALUE.equals(initialValue)) {
			GrepSettings grepSettings = new GrepSettings();
			grepSettings.setGroupMatching(true);
			grepSettings.setPattern(initialValue);
			grepSettings.setCaseSensitive(true);
			grepSettings.quick = true;
			storeGrepSettings(grepSettings);
			return grepSettings;
		} else {
			return super.getSettings(editor, initialValue);
		}
	}


	@Override
	protected GrepSettings getSettings(String initialValue) {
		return PluginPersistentStateComponent.getInstance().guessSettings(initialValue, false, true);
	}

}
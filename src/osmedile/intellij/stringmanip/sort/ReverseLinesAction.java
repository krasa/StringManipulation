package osmedile.intellij.stringmanip.sort;

import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.utils.Cloner;

public class ReverseLinesAction extends SortAction {
	public static final String STORE_KEY = "StringManipulation.ReverseLinesAction.SortSettings";

	public ReverseLinesAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(String storeKey) {
		SortSettings sortSettings = super.getSortSettings(storeKey);
		sortSettings.sortType(Sort.REVERSE);
		return sortSettings;
	}
	@Override
	protected void storeSortSettings(SortSettings newSettings) {
		SortSettings storing = Cloner.deepClone(newSettings);
		storing.setSortType(PluginPersistentStateComponent.getInstance()
				.getSortSettings().getSortType());
		PluginPersistentStateComponent.getInstance().setSortSettings(storing);
	}
}


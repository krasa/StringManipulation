package osmedile.intellij.stringmanip.sort;

import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.utils.Cloner;

public class ShuffleLinesAction extends SortAction {
	public static final String STORE_KEY = "StringManipulation.ShuffleLinesAction.SortSettings";

	public ShuffleLinesAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(String storeKey) {
		SortSettings sortSettings = super.getSortSettings(storeKey);
		sortSettings.sortType(Sort.SHUFFLE);
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

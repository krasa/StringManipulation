package osmedile.intellij.stringmanip.sort;

import osmedile.intellij.stringmanip.sort.support.SortSettings;

public class JsonSortAction extends SortAction {
	public static final String STORE_KEY = "StringManipulation.JsonSortAction.SortSettings";

	public JsonSortAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(String storeKey) {
		SortSettings sortSettings = super.getSortSettings(storeKey);
		sortSettings.setJsonSort(true);
		return sortSettings;
	}
}


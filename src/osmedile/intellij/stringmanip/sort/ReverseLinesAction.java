package osmedile.intellij.stringmanip.sort;

import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

public class ReverseLinesAction extends SortAction {
	public static final String STORE_KEY = "StringManipulation.ReverseLinesAction.SortSettings";

	public ReverseLinesAction() {
		super(STORE_KEY);
	}

	protected SortSettings getSortSettings(String storeKey) {
		SortSettings sortSettings = super.getSortSettings(storeKey);
		sortSettings.sortType(Sort.REVERSE);
		return sortSettings;
	}
}


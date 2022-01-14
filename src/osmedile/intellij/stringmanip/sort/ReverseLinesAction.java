package osmedile.intellij.stringmanip.sort;

import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

public class ReverseLinesAction extends SortAction {
	public static final String STORE_KEY = "ReverseLinesAction";

	public ReverseLinesAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(String storeKey) {
		SortSettings sortSettings = super.getSortSettings(storeKey);
		sortSettings.sortType(Sort.REVERSE);
		return sortSettings;
	}
}


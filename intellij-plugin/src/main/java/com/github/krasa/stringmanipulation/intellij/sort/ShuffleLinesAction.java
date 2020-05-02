package com.github.krasa.stringmanipulation.intellij.sort;

import com.github.krasa.stringmanipulation.utils.sort.Sort;
import com.github.krasa.stringmanipulation.utils.sort.SortSettings;

public class ShuffleLinesAction extends SortAction {
	public static final String STORE_KEY = "StringManipulation.ShuffleLinesAction.SortSettings";

	public ShuffleLinesAction() {
		super(STORE_KEY);
	}

	protected SortSettings getSortSettings(String storeKey) {
		SortSettings sortSettings = super.getSortSettings(storeKey);
		sortSettings.sortType(Sort.SHUFFLE);
		return sortSettings;
	}
}

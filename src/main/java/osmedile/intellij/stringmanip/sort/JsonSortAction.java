package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

public class JsonSortAction extends SortAction {
	public static final String STORE_KEY = "JsonSortAction";

	public JsonSortAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(Editor editor) {
		SortSettings sortSettings = new SortSettings();
		sortSettings.setSortType(Sort.CASE_SENSITIVE_A_Z);
		sortSettings.setJsonSort(true);
		return sortSettings;
	}
}


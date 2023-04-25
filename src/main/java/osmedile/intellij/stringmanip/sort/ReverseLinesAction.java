package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

public class ReverseLinesAction extends SortAction {
	public static final String STORE_KEY = "ReverseLinesAction";

	public ReverseLinesAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(Editor editor) {
		SortSettings sortSettings = new SortSettings();
		sortSettings.sortType(Sort.REVERSE);
		return sortSettings;
	}
}


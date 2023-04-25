package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.sort.support.Sort;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

public class ShuffleLinesAction extends SortAction {
	public static final String STORE_KEY = "ShuffleLinesAction";

	public ShuffleLinesAction() {
		super(STORE_KEY);
	}

	@Override
	protected SortSettings getSortSettings(Editor editor) {
		SortSettings sortSettings = new SortSettings();
		sortSettings.sortType(Sort.SHUFFLE);
		return sortSettings;
	}

}

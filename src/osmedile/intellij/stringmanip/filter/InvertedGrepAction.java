package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.util.Pair;

public class InvertedGrepAction extends GrepAction {

	public InvertedGrepAction() {
		super(new GrepFilter() {
			public boolean execute(String text, Pair<String, Boolean> grepos) {
				return !GREP_FILTER.execute(text, grepos);
			}
		});
	}
}
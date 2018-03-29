package osmedile.intellij.stringmanip.filter;

public class InvertedGrepAction extends GrepAction {

	public InvertedGrepAction() {
		super(new GrepFilter() {
			public boolean execute(String textPart, String grepos) {
				return !textPart.contains(grepos);
			}
		});
	}
}
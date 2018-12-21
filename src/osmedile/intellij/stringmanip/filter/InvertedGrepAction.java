package osmedile.intellij.stringmanip.filter;

public class InvertedGrepAction extends GrepAction {

	public InvertedGrepAction() {
		super((textPart, grepos) -> !textPart.contains(grepos));
	}
}
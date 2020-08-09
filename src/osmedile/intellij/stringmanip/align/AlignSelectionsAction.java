package osmedile.intellij.stringmanip.align;

public class AlignSelectionsAction extends AlignCaretsAction {
	public AlignSelectionsAction() {
		this(true);
	}

	protected AlignSelectionsAction(boolean setupHandler) {
		super(false);
		if (setupHandler) {
			this.setupHandler(new AlignCaretsHandler(getActionClass(), true));
		}
	}

}

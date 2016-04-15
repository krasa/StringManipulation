package osmedile.intellij.stringmanip.sort;

public class SortByLengthDescAction extends SortAction {
	private static final Sort SORT = Sort.LINE_LENGTH_LONG_SHORT;

	public SortByLengthDescAction(boolean setupHandler) {
		super(setupHandler, SORT);
	}

	protected SortByLengthDescAction() {
		super(SORT);
	}
}

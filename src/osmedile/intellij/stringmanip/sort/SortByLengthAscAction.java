package osmedile.intellij.stringmanip.sort;

public class SortByLengthAscAction extends SortAction {
	private static final Sort SORT = Sort.LINE_LENGTH_SHORT_LONG;

	public SortByLengthAscAction(boolean setupHandler) {
		super(setupHandler, SORT);
	}

	protected SortByLengthAscAction() {
		super(SORT);
	}
}

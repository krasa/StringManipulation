package osmedile.intellij.stringmanip.sort;

public class SortCaseSensitiveAzAction extends SortAction {
	private static final Sort SORT = Sort.CASE_SENSITIVE_A_Z;

	public SortCaseSensitiveAzAction(boolean setupHandler) {
		super(setupHandler, SORT);
	}

	protected SortCaseSensitiveAzAction() {
		super(SORT);
	}
}

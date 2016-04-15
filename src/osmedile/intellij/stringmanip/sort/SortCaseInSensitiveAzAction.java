package osmedile.intellij.stringmanip.sort;

public class SortCaseInSensitiveAzAction extends SortAction {
	private static final Sort SORT = Sort.CASE_INSENSITIVE_A_Z;

	public SortCaseInSensitiveAzAction(boolean setupHandler) {
		super(setupHandler, SORT);
	}

	protected SortCaseInSensitiveAzAction() {
		super(SORT);
	}
}

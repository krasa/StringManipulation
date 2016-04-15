package osmedile.intellij.stringmanip.sort;

public class SortCaseSensitiveZaAction extends SortAction {
	private static final Sort SORT = Sort.CASE_SENSITIVE_Z_A;

	public SortCaseSensitiveZaAction(boolean setupHandler) {
		super(setupHandler, SORT);
	}

	protected SortCaseSensitiveZaAction() {
		super(SORT);
	}
}

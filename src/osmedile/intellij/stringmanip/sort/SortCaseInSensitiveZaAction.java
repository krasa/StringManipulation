package osmedile.intellij.stringmanip.sort;

public class SortCaseInSensitiveZaAction extends SortAction {
	private static final Sort SORT = Sort.CASE_INSENSITIVE_Z_A;

	public SortCaseInSensitiveZaAction(boolean setupHandler) {
		super(setupHandler, SORT);
	}

	protected SortCaseInSensitiveZaAction() {
		super(SORT);
	}
}

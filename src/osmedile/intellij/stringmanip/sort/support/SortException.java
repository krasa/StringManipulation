package osmedile.intellij.stringmanip.sort.support;

public class SortException extends RuntimeException {
	public SortException() {
	}

	public SortException(String message) {
		super(message);
	}

	public SortException(String message, Throwable cause) {
		super(message, cause);
	}

	public SortException(Throwable cause) {
		super(cause);
	}

	public SortException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

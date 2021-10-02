package osmedile.intellij.stringmanip.filter;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Caret;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PreviewDialog implements Disposable {

	protected final ThreadPoolExecutor executor;

	public PreviewDialog() {

		//max 1 concurrent task + max 1 in queue
		executor = new ThreadPoolExecutor(1, 1,
				60, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1),
				new MyThreadFactory(),
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	/**
	 * Editor cannot handle too much
	 */
	protected String limitLength(String val) {
		if (val.length() > 10_000) {
			return val.substring(0, 10_000) + "\n...";
		}
		return val;
	}


	protected String getSelectedText(Caret caret) {
		final String originalText;
		if (caret.hasSelection()) {
			originalText = caret.getSelectedText() + "";//avoid null value
		} else {
			originalText = "";
		}
		return originalText;
	}

	static class MyThreadFactory implements ThreadFactory {
		public synchronized Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("StringManipulation.Preview");
			t.setDaemon(true);
			return t;
		}
	}

	@Override
	public void dispose() {
		executor.shutdown();
	}
}

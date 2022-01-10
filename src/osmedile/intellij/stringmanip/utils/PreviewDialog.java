package osmedile.intellij.stringmanip.utils;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ui.EDT;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.align.AlignToColumnsForm;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class PreviewDialog implements Disposable {
	private static final com.intellij.openapi.diagnostic.Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(AlignToColumnsForm.class);
	protected final ThreadPoolExecutor executor;
	private boolean disposed;

	public PreviewDialog() {

		//max 1 concurrent task + max 1 in queue
		executor = new ThreadPoolExecutor(1, 1,
				60, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1),
				new MyThreadFactory(),
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	protected void submitRenderPreview() {
		if (disposed) {
			return;
		}
		executor.submit(() -> {
			if (disposed) {
				return;
			}
			try {
				renderPreview();
			} catch (Throwable e) {
				LOG.error(e);
			}
		});
	}

	protected abstract void renderPreview();


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
		disposed = true;
		executor.shutdown();
	}


	protected void setPreviewTextOnEDT(String text, EditorImpl previewEditor, JPanel previewPanel) {
		if (disposed) {
			return;
		}
		ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
			if (disposed) {
				return;
			}
			previewEditor.getDocument().setText(text);

			inPreviewWriteAction(previewEditor);

			previewPanel.validate();
			previewPanel.repaint();
		}), ModalityState.any());
	}

	protected void inPreviewWriteAction(EditorImpl previewEditor) {
	}

	@NotNull
	public static List<String> getPreviewLines(Editor editor) {
		String text = getTextForPreview(editor);
		String[] split = text.split("\n");
		return new ArrayList<String>(Arrays.asList(split));
	}

	@NotNull
	public static String getTextForPreview(Editor editor) {
		if (EDT.isCurrentThreadEdt()) {
			return _getTextForPreview(editor);
		} else {
			Ref<String> ref = new Ref<>();
			ApplicationManager.getApplication().invokeAndWait(() -> ref.set(_getTextForPreview(editor)), ModalityState.any());
			return ref.get();
		}
	}

	@NotNull
	private static String _getTextForPreview(Editor editor) {
		List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
		StringBuilder sb = new StringBuilder();
		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			String text = editor.getDocument().getText(
					new TextRange(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd)));

			sb.append(text.trim());
			sb.append("\n");
//			if (sb.length() > MAX_PREVIEW) {
//				break;
//			}
		}
		String s = sb.toString();
//		s = s.substring(0, Math.min(MAX_PREVIEW, s.length()));
		return s;
	}

}

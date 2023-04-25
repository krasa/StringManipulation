package osmedile.intellij.stringmanip.utils;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ui.EDT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.align.AlignToColumnsForm;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class PreviewDialog<SettingsType, InputType> implements Disposable {
	private static final com.intellij.openapi.diagnostic.Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(AlignToColumnsForm.class);
	protected final ThreadPoolExecutor executor;
	protected final Editor editor;
	public boolean shown;
	private boolean disposed;

	public PreviewDialog(Editor editor) {
		this.editor = editor;

		//max 1 concurrent task + max 1 in queue
		executor = new ThreadPoolExecutor(1, 1,
				60, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1),
				new MyThreadFactory(),
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	@Nullable
	public boolean showAndGet(@Nullable Project project, String title, String dimensions) {
		return new MyDialogWrapper(this, title, dimensions, project).showAndGet();
	}

	public static class MyDialogWrapper extends DialogWrapper {

		private final PreviewDialog previewDialog;
		private String string;

		public MyDialogWrapper(PreviewDialog previewDialog, String title, String dimensions, @Nullable Project project) {
			super(project);
			this.previewDialog = previewDialog;
			init();
			setTitle(title);
			string = dimensions;
		}

		@Override
		protected void dispose() {
			super.dispose();
			Disposer.dispose(previewDialog);
		}

		@Nullable
		@Override
		public JComponent getPreferredFocusedComponent() {
			return previewDialog.getPreferredFocusedComponent();
		}

		@Nullable
		@Override
		protected String getDimensionServiceKey() {
			return string;
		}


		@Nullable
		@Override
		protected JComponent createCenterPanel() {
			return previewDialog.getRoot();
		}

		@Override
		public void show() {
			this.previewDialog.shown = true;
			this.previewDialog.submitRenderPreview();
			super.show();
		}

		@Override
		protected void doOKAction() {
			super.doOKAction();
		}
	}

	public void submitRenderPreview() {
		if (!shown) {
			return;
		}
		if (disposed) {
			return;
		}
		renderPreview();
	}

	protected void renderPreview() {
		InputType input = preparePreviewInput(this.editor);
		executor.submit(() -> {
			if (disposed) {
				return;
			}
			try {
				renderPreviewAsync(input);
			} catch (Throwable e) {
				LOG.error(e);
			}
		});
	}

	protected InputType preparePreviewInput(Editor editor) {
		return null;
	}

	;

	protected abstract void renderPreviewAsync(InputType input);

	@NotNull
	public abstract JComponent getPreferredFocusedComponent();

	@NotNull
	public abstract JComponent getRoot();


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

	protected void setPreviewTextOnEDT(String text, EditorImpl previewEditor, JPanel previewPanel, SettingsType settings) {
		if (disposed) {
			return;
		}

		ApplicationManager.getApplication().invokeLater(() -> WriteCommandAction.runWriteCommandAction(previewEditor.getProject(), () -> {

			if (disposed) {
				return;
			}
			previewEditor.getDocument().setText(text);

			inPreviewWriteAction(previewEditor, settings);

			previewPanel.validate();
			previewPanel.repaint();
		}), ModalityState.stateForComponent(previewPanel));
	}


	protected void inPreviewWriteAction(EditorImpl previewEditor, SettingsType settings) {
	}

	public static List<String> getPreviewLines(Editor editor) {
		if (editor == null) {
			return null;
		}
		String text = getTextForPreview(editor);
		String[] split = text.split("\n");
		return new ArrayList<>(Arrays.asList(split));
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

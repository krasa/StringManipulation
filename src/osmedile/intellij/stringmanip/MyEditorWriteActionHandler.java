package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.textarea.TextComponentEditor;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MyEditorWriteActionHandler<T> extends EditorActionHandler {
	public MyEditorWriteActionHandler() {
	}

	public MyEditorWriteActionHandler(boolean runForEachCaret) {
		super(runForEachCaret);
	}

	@Override
	public final void doExecute(final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {
		if (editor.isViewer()) return;
		if (!ApplicationManager.getApplication().isWriteAccessAllowed() && !EditorModificationUtil.requestWriting(editor)) return;


		final Pair<Boolean, T> additionalParam = beforeWriteAction(editor, dataContext);
		if (!additionalParam.first) {
			return;
		}

		DocumentRunnable runnable = new DocumentRunnable(editor.getDocument(), editor.getProject()) {
			@Override
			public void run() {
				final Document doc = editor.getDocument();

				doc.startGuardedBlockChecking();
				try {
					executeWriteAction(editor, caret, dataContext, additionalParam.second);
				} catch (ReadOnlyFragmentModificationException e) {
					EditorActionManager.getInstance().getReadonlyFragmentModificationHandler(doc).handle(e);
				} finally {
					doc.stopGuardedBlockChecking();
				}
			}
		};
		if (editor instanceof TextComponentEditor) {
			runnable.run();
		} else {
			ApplicationManager.getApplication().runWriteAction(runnable);
		}
	}


	public abstract void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, T additionalParam);

	@NotNull
	public Pair<Boolean, T> beforeWriteAction(Editor editor, DataContext dataContext) {
		return null;
	}

	protected final Pair<Boolean, T> stopExecution() {
		return new Pair<Boolean, T>(false, null);
	}

	protected final Pair<Boolean, T> continueExecution(T param) {
		return new Pair<Boolean, T>(true, param);
	}

	protected final Pair<Boolean, T> continueExecution() {
		return new Pair<Boolean, T>(true, null);
	}

}

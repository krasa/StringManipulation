package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MyEditorWriteActionHandler<T> extends EditorActionHandler {

	public MyEditorWriteActionHandler() {
		super(false);
	}

	@Override
	public final void doExecute(final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {
		final Pair<Boolean, T> additionalParam = beforeWriteAction(editor, dataContext);
		if (!additionalParam.first) {
			return;
		}

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				executeWriteAction(editor, caret, dataContext, additionalParam.second);
			}
		};
		new EditorWriteActionHandler(false) {
			@Override
			public void executeWriteAction(Editor editor1, @Nullable Caret caret1, DataContext dataContext1) {
				runnable.run();
			}
		}.doExecute(editor, caret, dataContext);
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

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
	protected final void doExecute(final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {
		final Pair<Boolean, T> additionalParameter = beforeWriteAction(editor, dataContext);
		if (!additionalParameter.first) {
			return;
		}

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				executeWriteAction(editor, caret, dataContext, additionalParameter.second);
			}
		};
		new EditorWriteActionHandler(false) {
			@Override
			public void executeWriteAction(Editor editor1, @Nullable Caret caret1, DataContext dataContext1) {
				runnable.run();
			}
		}.doExecute(editor, caret, dataContext);
	}


	protected abstract void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, @Nullable T additionalParameter);

	@NotNull
	protected Pair<Boolean, T> beforeWriteAction(Editor editor, DataContext dataContext) {
		return continueExecution();
	}

	protected final Pair<Boolean, T> stopExecution() {
		return new Pair<Boolean, T>(false, null);
	}

	protected final Pair<Boolean, T> continueExecution(T additionalParameter) {
		return new Pair<Boolean, T>(true, additionalParameter);
	}

	protected final Pair<Boolean, T> continueExecution() {
		return new Pair<Boolean, T>(true, null);
	}

}

package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;

/**
 * for showing dialogs before the action
 */
public abstract class MyEditorWriteActionHandler2<T> extends EditorWriteActionHandler {

	private final Class actionClass;
	private CustomActionModel customActionModel;
	protected T model;


	public MyEditorWriteActionHandler2(Class actionClass) {
		super(false);
		this.actionClass = actionClass;
	}

	@Override
	public final void doExecute(final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {
		MyApplicationService.setAction(actionClass, customActionModel);

		final Pair<Boolean, T> additionalParameter = beforeWriteAction(editor, dataContext);
		if (!additionalParameter.first) {
			return;
		}

		try {
			model = additionalParameter.second;
			super.doExecute(editor, caret, dataContext);
		} finally {
			model = null;
		}
	}

	@Override
	public boolean executeInCommand(@NotNull Editor editor, DataContext dataContext) {
		return false;
	}

	/**
	 * for fixing ctrl+z when there was a selection <a href="https://youtrack.jetbrains.com/issue/IDEA-288319/">...</a>
	 */
	public boolean executeLater(@NotNull Editor editor, DataContext dataContext) {
		return false;
	}

	@Override
	public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
		T modelParam = this.model;
		Runnable runnable = () -> {
			executeWriteAction(editor, modelParam);
		};

		if (executeLater(editor, dataContext)) {
			ApplicationManager.getApplication().invokeLater(() -> {
				WriteCommandAction.runWriteCommandAction(editor.getProject(), runnable
				);
			});
		} else {
			WriteCommandAction.runWriteCommandAction(editor.getProject(), runnable);
		}
	}

	protected abstract void executeWriteAction(Editor editor, @Nullable T additionalParameter);

	@NotNull
	protected Pair<Boolean, T> beforeWriteAction(Editor editor, DataContext dataContext) {
		return continueExecution();
	}

	protected final Pair<Boolean, T> stopExecution() {
		MyApplicationService.setAction(actionClass, null);
		return new Pair<Boolean, T>(false, null);
	}

	protected final Pair<Boolean, T> continueExecution(T additionalParameter) {
		MyApplicationService.setAction(actionClass, (Object) additionalParameter);
		return new Pair<Boolean, T>(true, additionalParameter);
	}

	protected final Pair<Boolean, T> continueExecution() {
		return new Pair<Boolean, T>(true, null);
	}

	public void setCustomActionModel(CustomActionModel customActionModel) {
		this.customActionModel = customActionModel;
	}

}

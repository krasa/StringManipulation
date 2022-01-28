package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.sort.support.SortException;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;

import javax.swing.*;

/**
 * for showing dialogs before the action
 */
public abstract class MyEditorWriteActionHandler<T> extends EditorActionHandler {

	private final Class actionClass;
	private CustomActionModel customActionModel;

	public MyEditorWriteActionHandler(Class actionClass) {
		super(false);
		this.actionClass = actionClass;
	}

	@Override
	protected final void doExecute(final Editor editor, @Nullable final Caret caret, final DataContext dataContext) {
		MyApplicationService.setAction(actionClass, customActionModel);

		final Pair<Boolean, T> additionalParameter = beforeWriteAction(editor, dataContext);
		if (!additionalParameter.first) {
			return;
		}

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					executeWriteAction(editor, dataContext, additionalParameter.second);
				} catch (SortException e) {
					SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), e.getMessage(), "Error"));
				}
			}
		};
		new EditorWriteActionHandler(false) {
			@Override
			public void executeWriteAction(Editor editor1, @Nullable Caret caret1, DataContext dataContext1) {
				runnable.run();
			}
		}.doExecute(editor, caret, dataContext);
	}


	protected abstract void executeWriteAction(Editor editor, DataContext dataContext, @Nullable T additionalParameter);

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

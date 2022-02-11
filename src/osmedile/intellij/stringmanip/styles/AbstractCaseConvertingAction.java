package osmedile.intellij.stringmanip.styles;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.ActionUtils;

/**
 * todo write some tests
 */
public abstract class AbstractCaseConvertingAction extends AbstractStringManipAction<Object> {
	public static final String FROM = "from";
	private final Logger LOG = Logger.getInstance("#" + getClass().getCanonicalName());

	public AbstractCaseConvertingAction() {
	}

	public AbstractCaseConvertingAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, Caret caret, DataContext dataContext, SelectionModel selectionModel) {
		return ActionUtils.selectSomethingUnderCaret(editor);
	}


}

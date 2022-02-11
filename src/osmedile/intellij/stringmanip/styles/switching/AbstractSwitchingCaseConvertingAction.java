package osmedile.intellij.stringmanip.styles.switching;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.utils.ActionUtils;

import java.util.Map;

public abstract class AbstractSwitchingCaseConvertingAction extends AbstractStringManipAction<Object> {
	public static final String FROM = "from";
	private final Logger LOG = Logger.getInstance("#" + getClass().getCanonicalName());

	public AbstractSwitchingCaseConvertingAction() {
	}

	public AbstractSwitchingCaseConvertingAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, Caret caret, DataContext dataContext, SelectionModel selectionModel) {
		return ActionUtils.selectSomethingUnderCaret(editor);
	}

	@Override
	protected void analyzeLines(Map<String, Object> actionContext, String[] textParts) {
		for (String s : textParts) {
			Style from = Style.from(s);
			if (contains(supportedStyles(), from)) {
				actionContext.put(FROM, from);
				return;
			}
		}
	}

	protected abstract Style[] supportedStyles();


	protected Style getFirstStyle(Map<String, Object> actionContext, String s) {
		Style from = (Style) actionContext.get(FROM);
		if (from == null) {
			from = Style.from(s);
			actionContext.put(FROM, from);
		}
		return from;
	}


	public static <T> boolean contains(final T[] array, final T v) {
		for (final T e : array)
			if (e == v || v != null && v.equals(e))
				return true;

		return false;
	}
}

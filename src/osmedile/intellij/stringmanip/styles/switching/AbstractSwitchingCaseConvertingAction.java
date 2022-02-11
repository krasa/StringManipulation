package osmedile.intellij.stringmanip.styles.switching;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.apache.commons.lang3.StringUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.utils.ActionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static osmedile.intellij.stringmanip.styles.Style.*;

public abstract class AbstractSwitchingCaseConvertingAction extends AbstractStringManipAction<Object> {

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
	protected void analyzeEditorInWriteAction(Map<String, Object> actionContext, Editor editor, DataContext dataContext) {
		editor.getCaretModel().runForEachCaret(new CaretAction() {
			@Override
			public void perform(Caret caret) {
				final SelectionModel selectionModel = editor.getSelectionModel();
				String selectedText = selectionModel.getSelectedText();

				boolean noSelection = selectedText == null;
				if (noSelection) {
					selectSomethingUnderCaret(editor, caret, dataContext, selectionModel);
					selectedText = selectionModel.getSelectedText();

					if (selectedText == null) {
						return;
					}
				}

				analyze(selectedText, actionContext);
			}
		});
	}

	private void analyze(String selectedText, Map<String, Object> actionContext) {
		String[] textParts = selectedText.split("\n");
		for (String s : textParts) {
			Style from = Style.from(s);
			actionContext.put(from.name(), true);
		}
	}

	/**
	 * can be reliably detected and transformed from
	 */
	public static final ArrayList<Style> MAIN_STYLES = new ArrayList<>(Arrays.asList(KEBAB_LOWERCASE,
			KEBAB_UPPERCASE,
			SNAKE_CASE,
			CAPITALIZED_SNAKE_CASE,
			SCREAMING_SNAKE_CASE,
			PASCAL_CASE,
			CAMEL_CASE,
			DOT,
			SENTENCE_CASE
	));

	protected boolean containsAnyMainStyleExcept(Style except, Map<String, Object> actionContext) {
		for (Style s : MAIN_STYLES) {
			if (s == except) {
				continue;
			}
			if (actionContext.containsKey(s.name())) {
				return true;
			}
		}
		return false;
	}

	protected boolean contains(Style style, Map<String, Object> actionContext) {
		return actionContext.get(style.name()) != null;
	}

	public static <T> boolean contains(final T[] array, final T v) {
		for (final T e : array)
			if (e == v || v != null && v.equals(e))
				return true;

		return false;
	}

	/**
	 * only for tests
	 */
	@Override
	public String test_transformByLine(String s) {
		HashMap<String, Object> actionContext = new HashMap<>();
		analyze(s, actionContext);
		return transformByLine(actionContext, s);
	}

	/**
	 * only for tests
	 */
	public String test_transform(String selectedText) {
		String[] textParts = selectedText.split("\n");
		HashMap<String, Object> actionContext = new HashMap<>();
		for (String textPart : textParts) {
			analyze(textPart, actionContext);
		}
		for (int i = 0; i < textParts.length; i++) {
			textParts[i] = transformByLine(actionContext, textParts[i]);
		}
		return StringUtils.join(textParts, "\n");
	}
}

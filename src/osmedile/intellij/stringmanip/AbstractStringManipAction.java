package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: AbstractStringManipAction.java 62 2008-04-20 11:11:54Z osmedile $
 */
public abstract class AbstractStringManipAction<T> extends MyEditorAction {

	protected final boolean setupHandler;
	protected MyEditorWriteActionHandler<T> myHandler = null;

	protected AbstractStringManipAction() {
		this(true);
	}

	protected AbstractStringManipAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(myHandler = new MyEditorWriteActionHandler<T>(getActionClass()) {
				@NotNull
				@Override
				protected Pair<Boolean, T> beforeWriteAction(Editor editor, DataContext dataContext) {
					return AbstractStringManipAction.this.beforeWriteAction(editor, dataContext);
				}

				@Override
				protected void executeWriteAction(Editor editor, @Nullable Caret caret, final DataContext dataContext, final T additionalParam) {
					executeMyWriteAction(editor, dataContext, additionalParam);
				}

			});
		}
		this.setupHandler = setupHandler;
	}

	@NotNull
	public Pair<Boolean, T> beforeWriteAction(Editor editor, DataContext dataContext) {
		return new Pair<Boolean, T>(true, null);
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

	protected void executeMyWriteAction(Editor editor, final DataContext dataContext, final T additionalParam) {
		Map<String, Object> actionContext = new HashMap<>();
		editor.getCaretModel().runForEachCaret(new CaretAction() {
			@Override
			public void perform(Caret caret) {
				executeMyWriteActionPerCaret(caret.getEditor(), caret, actionContext, dataContext, additionalParam);
			}
		});
	}

	protected void executeMyWriteActionPerCaret(Editor editor, Caret caret, Map<String, Object> actionContext, DataContext dataContext, T additionalParam) {
		final SelectionModel selectionModel = editor.getSelectionModel();
		String selectedText = selectionModel.getSelectedText();

		if (selectedText == null) {
			selectSomethingUnderCaret(editor, dataContext, selectionModel);
			selectedText = selectionModel.getSelectedText();

			if (selectedText == null) {
				return;
			}
		}

		String s = transformSelection(editor, actionContext, dataContext, selectedText, additionalParam);
		s = s.replace("\r\n", "\n");
		s = s.replace("\r", "\n");
        editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), s);
    }


	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, T additionalParam) {
		String[] textParts = selectedText.split("\n");

		for (int i = 0; i < textParts.length; i++) {
			if (!StringUtils.isBlank(textParts[i])) {
				textParts[i] = transformByLine(actionContext, textParts[i]);
			}
		}

		String join = StringUtils.join(textParts, '\n');

		if (selectedText.endsWith("\n")) {
			return join + "\n";
		}
		return join;
	}

	protected boolean selectSomethingUnderCaret(Editor editor, DataContext dataContext, SelectionModel selectionModel) {
		selectionModel.selectLineAtCaret();
		String selectedText = selectionModel.getSelectedText();
		if (selectedText != null && selectedText.endsWith("\n")) {
			selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
		}
		return true;
	}

	public final String transformByLine( String s) {
		return transformByLine(new HashMap<>(),s );
	}

	public abstract String transformByLine(Map<String, Object> actionContext, String s);
}

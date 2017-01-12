package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.utils.StringUtils;

/**
 * @author Olivier Smedile
 * @version $Id: AbstractStringManipAction.java 62 2008-04-20 11:11:54Z osmedile $
 */
public abstract class AbstractStringManipAction<T> extends EditorAction {

	protected AbstractStringManipAction() {
		this(true);
	}

	protected AbstractStringManipAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new MyEditorWriteActionHandler<T>() {
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
		editor.getCaretModel().runForEachCaret(new CaretAction() {
			@Override
			public void perform(Caret caret) {
				executeMyWriteActionPerCaret(caret.getEditor(), caret, dataContext, additionalParam);
			}
		});
	}

	protected void executeMyWriteActionPerCaret(Editor editor, Caret caret, DataContext dataContext, T additionalParam) {
		final SelectionModel selectionModel = editor.getSelectionModel();
		String selectedText = selectionModel.getSelectedText();

		if (selectedText == null) {
			selectSomethingUnderCaret(editor, dataContext, selectionModel);
			selectedText = selectionModel.getSelectedText();

			if (selectedText == null) {
				return;
			}
		}

		String s = transformSelection(editor, dataContext, selectedText, additionalParam);
		s = s.replace("\r\n", "\n");
		s = s.replace("\r", "\n");
		editor.getDocument().replaceString(selectionModel.getSelectionStart(),
				selectionModel.getSelectionEnd(), s);
	}


	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, T additionalParam) {
		String[] textParts = selectedText.split("\n");

		for (int i = 0; i < textParts.length; i++) {
			textParts[i] = transformByLine(textParts[i]);
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

	public abstract String transformByLine(String s);
}

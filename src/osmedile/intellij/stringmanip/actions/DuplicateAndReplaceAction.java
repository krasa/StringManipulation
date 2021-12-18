package osmedile.intellij.stringmanip.actions;

import com.intellij.find.EditorSearchSession;
import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.editor.actions.DocumentGuardedTextUtil;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorAction;

public class DuplicateAndReplaceAction extends MyEditorAction {

	protected DuplicateAndReplaceAction() {
		super(new DuplicateAndReplaceAction.Handler());
	}

	private static class Handler extends EditorWriteActionHandler {
		Handler() {
			super(true);
		}

		@Override
		public void executeWriteAction(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
			FindManager findManager = FindManager.getInstance(editor.getProject());
			FindModel model = findManager.createReplaceInFileModel();
			if (model.getStringToFind().isBlank() || model.getStringToReplace().isBlank()) {
				EditorSearchSession.start(editor, model, editor.getProject());
				return;
			}

			model = model.clone();
			model.setGlobal(false);
			duplicateLineOrSelectedBlockAtCaret(editor, model);
		}

		@Override
		public boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
			return !editor.isOneLineMode() || editor.getSelectionModel().hasSelection();
		}
	}

	public static void duplicateLineOrSelectedBlockAtCaret(Editor editor, FindModel model) {
		Document document = editor.getDocument();
		CaretModel caretModel = editor.getCaretModel();
		ScrollingModel scrollingModel = editor.getScrollingModel();
		if (editor.getSelectionModel().hasSelection()) {
			int start = editor.getSelectionModel().getSelectionStart();
			int end = editor.getSelectionModel().getSelectionEnd();
			String s = document.getCharsSequence().subSequence(start, end).toString();
			s = replace(editor.getProject(), s, model);
			document.insertString(end, s);
			caretModel.moveToOffset(end + s.length());
			scrollingModel.scrollToCaret(ScrollType.RELATIVE);
			editor.getSelectionModel().removeSelection();
			editor.getSelectionModel().setSelection(end, end + s.length());
		} else {
			duplicateLinesRange(editor, document, caretModel.getVisualPosition(), caretModel.getVisualPosition(), model);
		}

	}

	private static String replace(Project project, String s, FindModel model) {
		if (project == null) {
			return s;
		}
		DocumentImpl document = new DocumentImpl(s);
		MyFindUtil.doReplace(project, model, 0, document);
		return document.getText();
	}

	@Nullable
	static Couple<Integer> duplicateLinesRange(Editor editor, Document document, VisualPosition rangeStart, VisualPosition rangeEnd, FindModel model) {
		Pair<LogicalPosition, LogicalPosition> lines = EditorUtil.calcSurroundingRange(editor, rangeStart, rangeEnd);
		int offset = editor.getCaretModel().getOffset();

		LogicalPosition lineStart = lines.first;
		LogicalPosition nextLineStart = lines.second;
		int start = editor.logicalPositionToOffset(lineStart);
		int end = editor.logicalPositionToOffset(nextLineStart);
		if (end <= start) {
			return null;
		}
		String s = document.getCharsSequence().subSequence(start, end).toString();
		s = replace(editor.getProject(), s, model);
		int newOffset = end + offset - start;
		int selectionStart = end;
		if (nextLineStart.line == document.getLineCount() - 1 && nextLineStart.column > 0) { // last line
			s = "\n" + s;
			newOffset++;
			selectionStart++;
		}
		try {
			DocumentGuardedTextUtil.insertString(document, end, s);
		} catch (Throwable e) { //TODO old IDE
			document.insertString(end, s);
		}

		editor.getCaretModel().moveToOffset(newOffset);
		editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
		return Couple.of(selectionStart, end + s.length());
	}

//	@Override
//	public void update(final Editor editor, final Presentation presentation, final DataContext dataContext) {
//		super.update(editor, presentation, dataContext);
//		if (editor.getSelectionModel().hasSelection()) {
//			presentation.setText(EditorBundle.message("action.duplicate.selection"), true);
//		} else {
//			presentation.setText(EditorBundle.message("action.duplicate.line"), true);
//		}
//	}
}

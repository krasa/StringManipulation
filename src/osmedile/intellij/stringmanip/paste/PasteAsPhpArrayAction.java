package osmedile.intellij.stringmanip.paste;

import com.intellij.codeInsight.editorActions.TextBlockTransferable;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.editor.actions.BasePasteHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyApplicationService;
import osmedile.intellij.stringmanip.MyEditorAction;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.datatransfer.Transferable;

public class PasteAsPhpArrayAction extends MyEditorAction {

	protected PasteAsPhpArrayAction() {
		super(new PasteAsPhpArrayAction.Handler());
	}

	private static class Handler extends EditorWriteActionHandler {
		Handler() {
			super(false);
		}

		@Override
		public void executeWriteAction(@NotNull Editor editor, Caret caret, DataContext dataContext) {
			MyApplicationService.setAction(PasteAsPhpArrayAction.class);
			Transferable content = EditorModificationUtil.getContentsToPasteToEditor(null);
			if (content != null) {
				pasteTransferable(editor, content);
			}
		}

		@Override
		public boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
			return !editor.isOneLineMode() || editor.getSelectionModel().hasSelection();
		}
	}

	public static void pasteTransferable(final @NotNull Editor editor, @NotNull Transferable content) throws EditorCopyPasteHelper.TooLargeContentException {
		Project project = editor.getProject();
		if (project == null) {
			return;
		}
		String text = EditorModificationUtil.getStringContent(content);
		if (text == null) return;

		int textLength = text.length();
		if (BasePasteHandler.isContentTooLarge(textLength))
			throw new EditorCopyPasteHelper.TooLargeContentException(textLength);

		CaretModel caretModel = editor.getCaretModel();
//		if (caretModel.supportsMultipleCarets()) {
//			CaretStateTransferableData caretData = null;
//			int caretCount = caretModel.getCaretCount();
//			if (caretCount == 1 && editor.isColumnMode()) {
//				int pastedLineCount = LineTokenizer.calcLineCount(text, true);
//				if (pastedLineCount <= caretModel.getMaxCaretCount()) {
//					EditorModificationUtil.deleteSelectedText(editor);
//					Caret caret = caretModel.getPrimaryCaret();
//					for (int i = 0; i < pastedLineCount - 1; i++) {
//						caret = caret.clone(false);
//						if (caret == null) {
//							break;
//						}
//					}
//					caretCount = caretModel.getCaretCount();
//				}
//			} else {
//				caretData = CaretStateTransferableData.getFrom(content);
//			}
//			final TextRange[] ranges = new TextRange[caretCount];
//			final Iterator<String> segments = new ClipboardTextPerCaretSplitter().split(text, caretData, caretCount).iterator();
//			final int[] index = {0};
//			caretModel.runForEachCaret(caret -> {
//				String normalizedText = TextBlockTransferable.convertLineSeparators(editor, segments.next());
//				normalizedText = trimTextIfNeeded(editor, normalizedText);
//				int caretOffset = caret.getOffset();
//				ranges[index[0]++] = new TextRange(caretOffset, caretOffset + normalizedText.length());
//				EditorModificationUtil.insertStringAtCaret(editor, normalizedText, false, true);
//			});
//			return ranges;
//		} else {
		int caretOffset = caretModel.getOffset();
		String normalizedText = TextBlockTransferable.convertLineSeparators(editor, text);
		normalizedText = trimTextIfNeeded(editor, normalizedText);
		String[] split = normalizedText.split("\n");
		StringBuilder sb = new StringBuilder();

		sb.append("[\n");
		for (String s : split) {
			String trim = s.trim();
			if (trim.length() > 0) {
				sb.append("\t").append("'").append(trim).append("'").append(",\n");
			}
		}
		sb.append("]");

		String s = sb.toString();
		EditorModificationUtil.insertStringAtCaret(editor, s, false, true);

		Document document = editor.getDocument();
		PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
		documentManager.commitDocument(document);
		PsiFile psiFile = documentManager.getPsiFile(document);
		if (psiFile != null) {
			CodeStyleManager.getInstance(project).adjustLineIndent(psiFile, new TextRange(caretOffset, caretOffset + s.length()));
		}

//		}
	}

	private static String trimTextIfNeeded(Editor editor, String text) {
		JComponent contentComponent = editor.getContentComponent();
		if (contentComponent instanceof JTextComponent) {
			javax.swing.text.Document document = ((JTextComponent) contentComponent).getDocument();
			if (document != null && document.getProperty(EditorCopyPasteHelper.TRIM_TEXT_ON_PASTE_KEY) == Boolean.TRUE) {
				return text.trim();
			}
		}
		return text;
	}
}

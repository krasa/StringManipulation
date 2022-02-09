package osmedile.intellij.stringmanip.paste;

import com.intellij.codeInsight.editorActions.TextBlockTransferable;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.editor.actions.BasePasteHandler;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.LineTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyApplicationService;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.utils.StringUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.datatransfer.Transferable;
import java.util.Iterator;

public class PasteCamelCasedAction extends MyEditorAction {

	protected PasteCamelCasedAction() {
		super(new PasteCamelCasedAction.Handler());
	}

	private static class Handler extends EditorWriteActionHandler {
		Handler() {
			super(false);
		}

		@Override
		public void executeWriteAction(@NotNull Editor editor, Caret caret, DataContext dataContext) {
			MyApplicationService.setAction(PasteCamelCasedAction.class);
			Transferable content = EditorModificationUtil.getContentsToPasteToEditor(null);
			if (content != null) {
				pasteTransferable(editor, content);
			}
		}
	}

	/**
	 * com.intellij.openapi.editor.impl.EditorCopyPasteHelperImpl
	 */
	public static TextRange[] pasteTransferable(final @NotNull Editor editor, @NotNull Transferable content) throws EditorCopyPasteHelper.TooLargeContentException {
		String text = EditorModificationUtil.getStringContent(content);
		if (text == null) return null;

		int textLength = text.length();
		if (BasePasteHandler.isContentTooLarge(textLength))
			throw new EditorCopyPasteHelper.TooLargeContentException(textLength);

		CaretModel caretModel = editor.getCaretModel();
		if (caretModel.supportsMultipleCarets()) {
			CaretStateTransferableData caretData = null;
			int caretCount = caretModel.getCaretCount();
			if (caretCount == 1 && editor.isColumnMode()) {
				int pastedLineCount = LineTokenizer.calcLineCount(text, true);
				if (pastedLineCount <= caretModel.getMaxCaretCount()) {
					EditorModificationUtil.deleteSelectedText(editor);
					Caret caret = caretModel.getPrimaryCaret();
					for (int i = 0; i < pastedLineCount - 1; i++) {
						caret = caret.clone(false);
						if (caret == null) {
							break;
						}
					}
					caretCount = caretModel.getCaretCount();
				}
			} else {
				caretData = CaretStateTransferableData.getFrom(content);
			}
			final TextRange[] ranges = new TextRange[caretCount];
			final Iterator<String> segments = new ClipboardTextPerCaretSplitter().split(text, caretData, caretCount).iterator();
			final int[] index = {0};
			caretModel.runForEachCaret(caret -> {
				String normalizedText = TextBlockTransferable.convertLineSeparators(editor, segments.next());
				int caretOffset = caret.getOffset();
				normalizedText = trimTextIfNeeded(editor, normalizedText);
				normalizedText = transform(editor, caretOffset, caret.getSelectionStart(), normalizedText);
				ranges[index[0]++] = new TextRange(caretOffset, caretOffset + normalizedText.length());
				EditorModificationUtil.insertStringAtCaret(editor, normalizedText, false, true);
			});
			return ranges;
		} else {
			int caretOffset = caretModel.getOffset();
			String normalizedText = TextBlockTransferable.convertLineSeparators(editor, text);
			normalizedText = trimTextIfNeeded(editor, normalizedText);
			normalizedText = transform(editor, caretOffset, editor.getSelectionModel().getSelectionStart(), normalizedText);
			EditorModificationUtil.insertStringAtCaret(editor, normalizedText, false, true);
			return new TextRange[]{new TextRange(caretOffset, caretOffset + text.length())};
		}
	}

	private static String transform(Editor editor, int caretOffset, int selectionStart, String normalizedText) {
		String s = Style.CAMEL_CASE.transform(normalizedText);
		if (selectionStart >= 0 && caretOffset > selectionStart) {
			caretOffset = selectionStart;
		}
		String text = caretOffset == 0 ? "-" : editor.getDocument().getText(TextRange.create(caretOffset - 1, caretOffset));
		if (StringUtil.containsOnlyLettersAndDigits(text)) {
			return StringUtils.capitalize(s);
		} else {
			return StringUtils.uncapitalize(s);
		}
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

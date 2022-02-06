package osmedile.intellij.stringmanip.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionUtils {
	private static final Logger LOG = LoggerFactory.getLogger(ActionUtils.class);


	/**
	 * editor.getCaretModel().runForEachCaret(caret -> ActionUtils.selectSomethingUnderCaret(editor));
	 */
	public static boolean selectSomethingUnderCaret(Editor editor) {
		try {
			SelectionModel selectionModel = editor.getSelectionModel();
			if (selectionModel.hasSelection()) {
				return true;
			}

			Project project = editor.getProject();
			if (project == null) {
				return selectLineAtCaret(editor);
			}
			PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
			if (psiFile == null) {// select whole line in plaintext
				return selectLineAtCaret(editor);
			}
			FileType fileType = psiFile.getFileType();
			boolean handled = false;
			if (isJava(fileType)) {
				handled = javaHandling(editor, selectionModel, psiFile);
			}
			if (!handled && isProperties(fileType)) {
				handled = propertiesHandling(editor, selectionModel);
			}
			if (!handled && fileType.equals(PlainTextFileType.INSTANCE)) {
				handled = selectLineAtCaret(editor);
			}
			if (!handled) {
				handled = genericHandling(editor, selectionModel, psiFile);
			}
			return handled;
		} catch (Throwable e) {
			LOG.error("please report this, so I can fix it :(", e);
			return selectLineAtCaret(editor);
		}
	}

	public static boolean selectLineAtCaret(Editor editor) {
		SelectionModel selectionModel = editor.getSelectionModel();
		if (!selectionModel.hasSelection()) {
			selectionModel.selectLineAtCaret();
			String selectedText = selectionModel.getSelectedText();
			if (selectedText != null && selectedText.endsWith("\n")) {
				selectionModel.setSelection(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd() - 1);
			}
		}
		return selectionModel.hasSelection();
	}

	public static boolean isProperties(FileType fileType) {
		try {
			return "Properties".equals(fileType.getName());
		} catch (Throwable exception) {
			return false;
		}
	}

	public static boolean isJava(FileType fileType) {
		try {
			//noinspection ConstantConditions
			return "JAVA".equals(fileType.getName()) && Class.forName("com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl") != null;
		} catch (Throwable e) {
			return false;
		}
	}

	public static boolean propertiesHandling(Editor editor, SelectionModel selectionModel) {
		PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor);
		if (elementAtCaret instanceof PsiWhiteSpace) {
			return false;
		} else if (elementAtCaret instanceof LeafPsiElement) {
			IElementType elementType = ((LeafPsiElement) elementAtCaret).getElementType();
			if (elementType.toString().equals("Properties:VALUE_CHARACTERS")
					|| elementType.toString().equals("Properties:KEY_CHARACTERS")) {
				TextRange textRange = elementAtCaret.getTextRange();
				if (textRange.getLength() == 0) {
					return selectLineAtCaret(editor);
				}
				selectionModel.setSelection(textRange.getStartOffset(), textRange.getEndOffset());
				return true;
			}
		}
		return false;
	}

	public static boolean javaHandling(Editor editor, SelectionModel selectionModel, PsiFile psiFile) {
		boolean steppedLeft = false;
		int caretOffset = editor.getCaretModel().getOffset();

		PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor);
		if (elementAtCaret instanceof PsiWhiteSpace) {
			elementAtCaret = PsiUtilBase.getElementAtOffset(psiFile, caretOffset - 1);
			steppedLeft = true;
		} else if (elementAtCaret instanceof PsiJavaTokenImpl) {
			PsiJavaToken javaToken = (PsiJavaToken) elementAtCaret;
			if (javaToken.getTokenType() != JavaTokenType.STRING_LITERAL) {
				elementAtCaret = PsiUtilBase.getElementAtOffset(psiFile, caretOffset - 1);
				steppedLeft = true;
			}
		}

		if (steppedLeft && !(elementAtCaret instanceof PsiJavaToken)) {
			return false;
		}

		if (steppedLeft && elementAtCaret instanceof PsiJavaTokenImpl) {
			PsiJavaToken javaToken = (PsiJavaToken) elementAtCaret;
			if (javaToken.getTokenType() != JavaTokenType.STRING_LITERAL) {
				return false;
			}
		}

		if (elementAtCaret instanceof PsiComment) {
			PsiComment comment = (PsiComment) elementAtCaret;
			selectLineAtCaret(editor);
			String selectedText = selectionModel.getSelectedText();
			int selectionStart = selectionModel.getSelectionStart();
			int selectionEnd = selectionModel.getSelectionEnd();

			String s = com.intellij.openapi.util.text.StringUtil.trimLeading(selectedText);
			s = comment.getTokenType() == JavaTokenType.END_OF_LINE_COMMENT ? com.intellij.openapi.util.text.StringUtil.trimStart(s, "//") : s;
			s = comment.getTokenType() == JavaTokenType.C_STYLE_COMMENT ? com.intellij.openapi.util.text.StringUtil.trimStart(s, "/*") : s;
			s = comment.getTokenType() == JavaTokenType.C_STYLE_COMMENT ? com.intellij.openapi.util.text.StringUtil.trimStart(s, "*") : s;
			s = com.intellij.openapi.util.text.StringUtil.trimLeading(s);

			String s2 = comment.getTokenType() == JavaTokenType.C_STYLE_COMMENT ? com.intellij.openapi.util.text.StringUtil.trimEnd(selectedText, "*/") : selectedText;
			s2 = com.intellij.openapi.util.text.StringUtil.trimTrailing(s2);
			selectionModel.setSelection(selectionStart + (selectedText.length() - s.length()), selectionEnd - (selectedText.length() - s2.length()));
			return true;
		}

		if (elementAtCaret instanceof PsiJavaToken) {
			int offset = 0;
			PsiJavaToken javaToken = (PsiJavaToken) elementAtCaret;
			if (javaToken.getTokenType() == JavaTokenType.STRING_LITERAL) {
				offset = 1;
			}
			TextRange textRange = elementAtCaret.getTextRange();
			if (textRange.getLength() == 0) {
				return selectLineAtCaret(editor);
			}
			selectionModel.setSelection(textRange.getStartOffset() + offset, textRange.getEndOffset() - offset);
			if (caretOffset < selectionModel.getSelectionStart()) {
				editor.getCaretModel().moveToOffset(selectionModel.getSelectionStart());
			}
			if (caretOffset > selectionModel.getSelectionEnd()) {
				editor.getCaretModel().moveToOffset(selectionModel.getSelectionEnd());
			}
			return true;
		} else {
			return selectLineAtCaret(editor);
		}
	}

	public static boolean genericHandling(Editor editor, SelectionModel selectionModel, PsiFile psiFile) {
		int caretOffset = editor.getCaretModel().getOffset();
		PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor);
		if (elementAtCaret instanceof PsiPlainText) {
			return selectLineAtCaret(editor);
		} else if (elementAtCaret instanceof PsiWhiteSpace) {
			elementAtCaret = PsiUtilBase.getElementAtOffset(psiFile, caretOffset - 1);
		}

		if (elementAtCaret == null || elementAtCaret instanceof PsiWhiteSpace) {
			return selectLineAtCaret(editor);
		} else {
			TextRange textRange = elementAtCaret.getTextRange();
			if (textRange.getLength() == 0) {
				return selectLineAtCaret(editor);
			}
			selectionModel.setSelection(textRange.getStartOffset(), textRange.getEndOffset());
			String selectedText = selectionModel.getSelectedText();

			if (selectedText != null && selectedText.contains("\n")) {
				selectionModel.removeSelection();
				return selectLineAtCaret(editor);
			}
			if (StringUtil.isQuoted(selectedText)) {
				selectionModel.setSelection(selectionModel.getSelectionStart() + 1,
						selectionModel.getSelectionEnd() - 1);
			}

			if (caretOffset < selectionModel.getSelectionStart()) {
				editor.getCaretModel().moveToOffset(selectionModel.getSelectionStart());
			}
			if (caretOffset > selectionModel.getSelectionEnd()) {
				editor.getCaretModel().moveToOffset(selectionModel.getSelectionEnd());
			}
			return true;
		}
	}
}
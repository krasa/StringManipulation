package osmedile.intellij.stringmanip.styles;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilBase;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.Map;

/** todo write some tests for this shit */
public abstract class AbstractCaseConvertingAction extends AbstractStringManipAction<Object> {
	public static final String FROM = "from";
	private final Logger LOG = Logger.getInstance("#" + getClass().getCanonicalName());

	public AbstractCaseConvertingAction() {
	}

	public AbstractCaseConvertingAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	protected boolean selectSomethingUnderCaret(Editor editor, DataContext dataContext, SelectionModel selectionModel) {
		try {
			PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
			if (psiFile == null) {// select whole line in plaintext
				return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
			}
			FileType fileType = psiFile.getFileType();
			boolean handled = false;
			if (fileType.equals(StdFileTypes.JAVA) && isJavaInstalled()) {
				handled = javaHandling(editor, dataContext, selectionModel, psiFile);
			}
			if (!handled && fileType.equals(StdFileTypes.PROPERTIES)) {
				handled = propertiesHandling(editor, dataContext, selectionModel, psiFile);
			}
			if (!handled && fileType.equals(StdFileTypes.PLAIN_TEXT)) {
				handled = super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
			}
			if (!handled) {
				handled = genericHandling(editor, dataContext, selectionModel, psiFile);
			}
			return handled;
		} catch (Exception e) {
			LOG.error("please report this, so I can fix it :(", e);
			return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
		}
	}

	private boolean isJavaInstalled() {
		boolean javaInstalled;
		try {
			Class.forName("com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl");
			javaInstalled = true;
		} catch (ClassNotFoundException e) {
			javaInstalled = false;
		}
		return javaInstalled;
	}

	private boolean propertiesHandling(Editor editor, DataContext dataContext, SelectionModel selectionModel,
			PsiFile psiFile) {
		PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor);
		if (elementAtCaret instanceof PsiWhiteSpace) {
			return false;
		} else if (elementAtCaret instanceof LeafPsiElement) {
			IElementType elementType = ((LeafPsiElement) elementAtCaret).getElementType();
			if (elementType.toString().equals("Properties:VALUE_CHARACTERS")
					|| elementType.toString().equals("Properties:KEY_CHARACTERS")) {
				TextRange textRange = elementAtCaret.getTextRange();
				if (textRange.getLength() == 0) {
					return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
				}
				selectionModel.setSelection(textRange.getStartOffset(), textRange.getEndOffset());
				return true;
			}
		}
		return false;
	}

	private boolean javaHandling(Editor editor, DataContext dataContext, SelectionModel selectionModel, PsiFile psiFile) {
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

		if (elementAtCaret instanceof PsiJavaToken) {
			int offset = 0;
			PsiJavaToken javaToken = (PsiJavaToken) elementAtCaret;
			if (javaToken.getTokenType() == JavaTokenType.STRING_LITERAL) {
				offset = 1;
			}
			TextRange textRange = elementAtCaret.getTextRange();
			if (textRange.getLength() == 0) {
				return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
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
			return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
		}
	}

	private boolean genericHandling(Editor editor, DataContext dataContext, SelectionModel selectionModel,
			PsiFile psiFile) {
		int caretOffset = editor.getCaretModel().getOffset();
		PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor);
		if (elementAtCaret instanceof PsiPlainText) {
			return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
		} else if (elementAtCaret instanceof PsiWhiteSpace) {
			elementAtCaret = PsiUtilBase.getElementAtOffset(psiFile, caretOffset - 1);
		}

		if (elementAtCaret == null || elementAtCaret instanceof PsiWhiteSpace) {
			return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
		} else {
			TextRange textRange = elementAtCaret.getTextRange();
			if (textRange.getLength() == 0) {
				return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
			}
			selectionModel.setSelection(textRange.getStartOffset(), textRange.getEndOffset());
			String selectedText = selectionModel.getSelectedText();

			if (selectedText != null && selectedText.contains("\n")) {
				selectionModel.removeSelection();
				return super.selectSomethingUnderCaret(editor, dataContext, selectionModel);
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
	protected Style getStyle(Map<String, Object> actionContext, String s) {
		Style from = (Style) actionContext.get(FROM);
		if (from == null) {
			from = Style.from(s);
			actionContext.put(FROM, from);
		}
		return from;
	}
}

package osmedile.intellij.stringmanip.intentions;

import com.intellij.codeInsight.intention.FileModifier;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IntentionBase implements IntentionAction {
	@FileModifier.SafeFieldForPreview
	private final PsiElementPredicate predicate;

	protected IntentionBase() {
		super();
		predicate = getElementPredicate();
	}

	@NotNull
	protected abstract PsiElementPredicate getElementPredicate();

	@Override
	public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
		final PsiElement element = findMatchingElement(file, editor);
		if (element == null) {
			return;
		}
		assert element.isValid() : element;
		processIntention(element, project, editor);
	}

	protected abstract void processIntention(@NotNull PsiElement element, @NotNull Project project, Editor editor) throws IncorrectOperationException;

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
		return findMatchingElement(file, editor) != null;
	}

	@Override
	public boolean startInWriteAction() {
		return true;
	}


//	@Override
//	@NotNull
//	public @IntentionName String getText() {
//		return GroovyIntentionsBundle.message(getPrefix() + ".name");
//	}
//
//	@Override
//	@NotNull
//	public String getFamilyName() {
//		return GroovyIntentionsBundle.message(getPrefix() + ".family.name");
//	}

	@Nullable
	PsiElement findMatchingElement(PsiFile file, Editor editor) {
//		if (!file.getViewProvider().getLanguages().contains(GroovyLanguage.INSTANCE)) {
//			return null;
//		}

//		SelectionModel selectionModel = editor.getSelectionModel();
//		if (selectionModel.hasSelection()) {
//			int start = selectionModel.getSelectionStart();
//			int end = selectionModel.getSelectionEnd();
//
//			if (0 <= start && start <= end) {
//				TextRange selectionRange = new TextRange(start, end);
//				PsiElement element = CodeInsightUtil.findElementInRange(file, start, end, PsiElement.class);
//				while (element != null && element.getTextRange() != null && selectionRange.contains(element.getTextRange())) {
//					if (predicate.satisfiedBy(element)) return element;
//					element = element.getParent();
//				}
//			}
//		}

		final int position = editor.getCaretModel().getOffset();
		PsiElement element = file.findElementAt(position);
		while (element != null) {
			if (predicate.satisfiedBy(element)) return element;
			if (isStopElement(element)) break;
			element = element.getParent();
		}

		element = file.findElementAt(position - 1);
		while (element != null) {
			if (predicate.satisfiedBy(element)) return element;
			if (isStopElement(element)) return null;
			element = element.getParent();
		}

		return null;
	}

	protected boolean isStopElement(PsiElement element) {
		return element instanceof PsiFile;
	}

}

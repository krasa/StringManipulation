package osmedile.intellij.stringmanip.intentions;

import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectiveCConversionToFromRawStringLiteral extends IntentionBase {

	public static final @Nullable
	Language OBJECTIVE_C = Language.findLanguageByID("ObjectiveC");

	private PsiElementPredicate psiElementPredicate = element -> {
		if (element instanceof TreeElement) {
			return isStringLiteral((TreeElement) element) || isRawStringLiteral((TreeElement) element);
		}
		return false;
	};

	@Override
	public @IntentionName
	@NotNull
	String getText() {
		return "C++ conversion to/from raw string literal";
	}

	@Override
	public @NotNull
	@IntentionFamilyName String getFamilyName() {
		return "C++ conversion to/from raw string literal";
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
		if (OBJECTIVE_C == null) {
			return false;
		}
		if (!file.getViewProvider().getLanguages().contains(OBJECTIVE_C)) {
			return false;
		}
		return super.isAvailable(project, editor, file);
	}

	@NotNull
	@Override
	protected PsiElementPredicate getElementPredicate() {
		return psiElementPredicate;
	}

	private boolean isRawStringLiteral(TreeElement element) {
		return "RAW_STRING_LITERAL".equals(element.getElementType().toString());
	}

	private boolean isStringLiteral(TreeElement element) {
		return "STRING_LITERAL".equals(element.getElementType().toString());
	}

	@Override
	protected void processIntention(@NotNull PsiElement element, @NotNull Project project, Editor editor) throws IncorrectOperationException {
		if (element instanceof TreeElement) {
			TextRange textRange = element.getTextRange();
			String text = element.getText();
			String newText;
			if (isRawStringLiteral((TreeElement) element)) {
				int i = text.indexOf("(");
				int i1 = text.lastIndexOf(")");
				newText = "\"" + StringUtil.escapeStringCharacters(text.substring(i + 1, i1)) + "\"";
			} else if (isStringLiteral((TreeElement) element)) {
				newText = "R\"(" + StringUtil.unescapeStringCharacters(text.substring(1, text.length() - 1)) + ")\"";
			} else {
				return;
			}
			editor.getDocument().replaceString(textRange.getStartOffset(), textRange.getEndOffset(), newText);
		}
	}


}

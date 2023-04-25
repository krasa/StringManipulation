package osmedile.intellij.stringmanip.intentions;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface PsiElementPredicate {
	boolean satisfiedBy(@NotNull PsiElement element);
}

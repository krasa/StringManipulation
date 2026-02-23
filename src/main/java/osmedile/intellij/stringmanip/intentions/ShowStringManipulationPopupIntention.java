package osmedile.intellij.stringmanip.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PriorityAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.ide.ui.customization.CustomActionsSchema;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.StringManipulationBundle;

/**
 * Intention action that shows the String Manipulation popup menu in Quick Fixes (Alt+Enter).
 * This allows users to access String Manipulation features directly from the Quick Fix menu.
 */
public class ShowStringManipulationPopupIntention implements IntentionAction, PriorityAction {

    @Override
    public @IntentionName @NotNull String getText() {
        return StringManipulationBundle.message("intention.name.string.manipulation");
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return StringManipulationBundle.message("intentions.group.StringManipulation");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        // Always available when there's an editor
        return editor != null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        // Get the String Manipulation main action group
        ActionGroup actionGroup = (ActionGroup) CustomActionsSchema.getInstance()
                .getCorrectedAction("StringManipulation.Group.Main");
        
        if (actionGroup != null) {
            // Create and show the popup
            ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                    "String Manipulation",
                    actionGroup,
                    SimpleDataContext.builder()
                            .add(com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT, project)
                            .add(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR, editor)
                            .add(com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE, file)
                            .build(),
                    JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING,
                    true
            );
            
            popup.showInBestPositionFor(editor);
        }
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    public @NotNull Priority getPriority() {
        // Use NORMAL priority so it appears in a reasonable position in the Quick Fix menu
        return Priority.NORMAL;
    }
}

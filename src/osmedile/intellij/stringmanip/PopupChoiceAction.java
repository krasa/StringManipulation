package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;

/**
 * @author Olivier Smedile
 * @version $Id: File Header.java 3 2008-03-11 08:52:55Z osmedile $
 */
public class PopupChoiceAction extends DumbAwareAction {
    private ActionGroup actionGroup;


    public PopupChoiceAction() {
        actionGroup = (ActionGroup) ActionManager.getInstance().getAction("osmedile.ManipulateStringGroup");
    }

    public void actionPerformed(AnActionEvent e) {
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(null, actionGroup,
                e.getDataContext(), JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING, false);

        popup.showInBestPositionFor(e.getDataContext());

    }
}

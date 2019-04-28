package osmedile.intellij.stringmanip;

import com.intellij.ide.ui.customization.CustomActionsSchema;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwitchCasePopupAction extends PopupChoiceAction {
	public SwitchCasePopupAction() {
		super(new MyEditorWriteActionHandler(null) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
				WhatsNewPopup.whatsNewCheck(editor);

				ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(null, (ActionGroup) CustomActionsSchema.getInstance().getCorrectedAction("StringManipulation.Group.SwitchCase"),
					dataContext, JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING, true);

				popup.showInBestPositionFor(dataContext);
				return stopExecution();
			}

			@Override
			protected void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, @Nullable Object additionalParameter) {

			}
		});
	}
}

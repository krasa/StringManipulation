package osmedile.intellij.stringmanip;

import com.intellij.codeInsight.lookup.LookupEx;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.ide.ui.customization.CustomActionsSchema;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.wm.IdeFocusManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.InputEvent;

public class PopupChoiceAction extends EditorAction {

	public PopupChoiceAction(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}

	public PopupChoiceAction() {
		super(new MyEditorWriteActionHandler(null) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
				WhatsNewPopup.whatsNewCheck(editor);

				ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(null, (ActionGroup) CustomActionsSchema.getInstance().getCorrectedAction("StringManipulation.Group.Main"),
					dataContext, JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING, true);

				popup.showInBestPositionFor(dataContext);
				return stopExecution();
			}

			@Override
			protected void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, @Nullable Object additionalParameter) {

			}
		});
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		Editor editor = CommonDataKeys.EDITOR.getData(e.getDataContext());
		if (editor == null) {
			e.getPresentation().setEnabled(false);
			return;
		}
		Project project = getEventProject(e);
		if (project != null) {
			InputEvent inputEvent = e.getInputEvent();
			boolean onlyAltDown = false;
			if (inputEvent != null) {
				onlyAltDown = inputEvent.isAltDown() && !inputEvent.isShiftDown() && !inputEvent.isMetaDown() && !inputEvent.isControlDown();
			}
			LookupEx activeLookup = LookupManager.getInstance(project).getActiveLookup();
			boolean dialogOpen = isFromDialog(project);
			boolean popupCheck = activeLookup == null || (activeLookup != null && !onlyAltDown);
			boolean dialogCheck = !dialogOpen || (dialogOpen && !onlyAltDown);
			e.getPresentation().setEnabled((popupCheck && dialogCheck));
		}
	}

	public static boolean isFromDialog(Project project) {
		final Component owner = IdeFocusManager.getInstance(project).getFocusOwner();
		if (owner != null) {
			final DialogWrapper instance = DialogWrapper.findInstance(owner);
			return instance != null;
		}
		return false;
	}
}

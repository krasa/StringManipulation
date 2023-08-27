package osmedile.intellij.stringmanip;

import com.intellij.ide.IdeEventQueue;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RepeatAction extends MyEditorAction {

	public static Object model = null;

	public RepeatAction() {
		super(new MyEditorWriteActionHandler(null) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
				MyApplicationService service = MyApplicationService.getInstance();
				Pair<AnAction, Object> pair = service.getLastAction();
				AnAction anAction = pair.getFirst();
				if (anAction != null) {
					try {
						if (withModel()) {
							RepeatAction.model = pair.getSecond();
						}
						anAction.actionPerformed(new AnActionEvent(null, dataContext, "osmedile.intellij.stringmanip.RepeatAction", new Presentation(), ActionManager.getInstance(), 0));
					} finally {
						RepeatAction.model = null;
					}
				}
				return stopExecution();
			}

			@Override
			protected void executeWriteAction(Editor editor, DataContext dataContext, @Nullable Object additionalParameter) {

			}
		});
	}

	private static boolean withModel() {
		boolean repeatLastActionWithoutDialog = PluginPersistentStateComponent.getInstance().isRepeatLastActionWithoutDialog();
		AWTEvent trueCurrentEvent = IdeEventQueue.getInstance().getTrueCurrentEvent();
		if (trueCurrentEvent instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) trueCurrentEvent;
			if (mouseEvent.isControlDown() || mouseEvent.isAltDown()) {
				return !repeatLastActionWithoutDialog;
			}
		}
		if (trueCurrentEvent instanceof KeyEvent) {
			KeyEvent keyEvent = (KeyEvent) trueCurrentEvent;
			if (keyEvent.getKeyChar() == '1' && (keyEvent.isControlDown() || keyEvent.isAltDown())) {
				return !repeatLastActionWithoutDialog;
			}
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && (keyEvent.isControlDown() || keyEvent.isAltDown())) {
				return !repeatLastActionWithoutDialog;
			}
		}

		return repeatLastActionWithoutDialog;
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		UniversalActionModel anAction = PluginPersistentStateComponent.getInstance().getLastActionModel();
		if (anAction != null) {
			e.getPresentation().setEnabled(true);
			e.getPresentation().setText(StringManipulationBundle.message("repeat.text") + " - " + anAction.getTextWithMnemonic());
			String description = anAction.getDescription();
//			if (anAction.getModelData() != null) {
//				if (PluginPersistentStateComponent.getInstance().isRepeatLastActionWithoutDialog()) {
//					description += " - hold Ctrl or Alt to show dialog";
//				}else{
//					description += " - hold Ctrl or Alt to skip dialog";
//				}
//			}
			e.getPresentation().setDescription(description);
		} else {
			e.getPresentation().setText(StringManipulationBundle.message("repeat.last.action.text"));
			e.getPresentation().setDescription((String) null);
			e.getPresentation().setEnabled(false);
		}
	}
}

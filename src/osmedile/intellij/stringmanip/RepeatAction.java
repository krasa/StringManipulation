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
				Pair<AnAction, Object> pair = service.setAction();
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
		AWTEvent trueCurrentEvent = IdeEventQueue.getInstance().getTrueCurrentEvent();
		if (trueCurrentEvent instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) trueCurrentEvent;
			if (mouseEvent.isControlDown() || mouseEvent.isAltDown()) {
				return false;
			}
		}
		if (trueCurrentEvent instanceof KeyEvent) {
			KeyEvent keyEvent = (KeyEvent) trueCurrentEvent;
			if (keyEvent.getKeyChar() == '1' && (keyEvent.isControlDown() || keyEvent.isAltDown())) {
				return false;
			}
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && (keyEvent.isControlDown() || keyEvent.isAltDown())) {
				return false;
			}
		}

		return PluginPersistentStateComponent.getInstance().isRepeatLastActionWithoutDialog();
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		AnAction anAction = MyApplicationService.getInstance().setAction().getFirst();
		if (anAction != null) {
			e.getPresentation().setEnabled(true);
			e.getPresentation().setText(StringManipulationBundle.message("repeat.text") + " - " + anAction.getTemplatePresentation().getTextWithMnemonic());
			e.getPresentation().setDescription(anAction.getTemplatePresentation().getDescription());
		} else {
			e.getPresentation().setText(StringManipulationBundle.message("repeat.last.action.text"));
			e.getPresentation().setDescription((String) null);
			e.getPresentation().setEnabled(false);
		}
	}
}

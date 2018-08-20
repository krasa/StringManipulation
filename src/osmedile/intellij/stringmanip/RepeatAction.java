package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class RepeatAction extends AnAction {

	@Override
	public void actionPerformed(AnActionEvent e) {
		AnAction anAction = MyApplicationComponent.getInstance().getAnAction();
		if (anAction != null) {
			anAction.actionPerformed(e);
		}
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		AnAction anAction = MyApplicationComponent.getInstance().getAnAction();
		if (anAction != null) {
			e.getPresentation().setEnabled(true);
			e.getPresentation().setText("Repeat - " + anAction.getTemplatePresentation().getText());
		} else {
			e.getPresentation().setText("Repeat Last Action");
			e.getPresentation().setEnabled(false);
		}
	}
}

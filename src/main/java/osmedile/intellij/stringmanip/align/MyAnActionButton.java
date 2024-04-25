package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.AnActionButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class MyAnActionButton extends AnActionButton {

	public MyAnActionButton(@NlsContexts.Button String text, Icon icon) {
		super(text, icon);
	}


	@Override
	public final @NotNull ActionUpdateThread getActionUpdateThread() {
		return ActionUpdateThread.BGT;
	}
}

package com.github.krasa.stringmanipulation.intellij.swap;

import com.github.krasa.stringmanipulation.intellij.MyEditorAction;
import com.github.krasa.stringmanipulation.intellij.MyEditorWriteActionHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwapAction extends MyEditorAction {

	String lastSeparator = ",";

	protected SwapAction() {
		super(null);
		this.setupHandler(new MyEditorWriteActionHandler<SwapActionExecutor>(getActionClass()) {

			@NotNull
			protected Pair<Boolean, SwapActionExecutor> beforeWriteAction(Editor editor, DataContext dataContext) {
				SwapActionExecutor swapActionExecutor = new SwapActionExecutor(editor, dataContext, lastSeparator);
				if (swapActionExecutor.isSwappingTokens()) {
					String separator = swapActionExecutor.chooseSeparator();
					if (separator == null) {
						return stopExecution();
					} else {
						lastSeparator = separator;
					} 
				}
				return continueExecution(swapActionExecutor);
			}

			@Override
			protected void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext, SwapActionExecutor swapActionExecutor) {
				swapActionExecutor.execute();
			}

		});
	}

}

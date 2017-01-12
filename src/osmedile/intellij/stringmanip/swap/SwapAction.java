package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;

public class SwapAction extends EditorAction {

	String lastSeparator = ",";

	protected SwapAction() {
		super(null);
		this.setupHandler(new MyEditorWriteActionHandler<SwapActionExecutor>() {

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

package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;

public class SwapWordsAction extends MyEditorAction {

	protected SwapWordsAction() {
		super(null);
		this.setupHandler(new MyEditorWriteActionHandler<SwapWordsExecutor>(getActionClass()) {

			@NotNull
			protected Pair<Boolean, SwapWordsExecutor> beforeWriteAction(Editor editor, DataContext dataContext) {
				SwapWordsExecutor swapActionExecutor = new SwapWordsExecutor(editor, dataContext);
				return continueExecution(swapActionExecutor);
			}

			@Override
			protected void executeWriteAction(Editor editor, DataContext dataContext, SwapWordsExecutor swapActionExecutor) {
				swapActionExecutor.execute();
			}
		});
	}


}

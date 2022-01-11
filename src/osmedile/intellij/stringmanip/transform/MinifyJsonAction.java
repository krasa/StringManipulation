package osmedile.intellij.stringmanip.transform;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.ui.Messages;
import org.json.JSONObject;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.StringManipulationBundle;

import javax.swing.*;
import java.util.Map;

public class MinifyJsonAction extends AbstractStringManipAction<Object> {
	private static final Logger LOG = Logger.getInstance(MinifyJsonAction.class);

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		try {
			return new JSONObject(selectedText).toString();
		} catch (Throwable e) {
			SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), String.valueOf(e), StringManipulationBundle.message("dialog.title.minify.json")));
			LOG.info(e);
			throw new ProcessCanceledException(e);
		}
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new RuntimeException();
	}
}
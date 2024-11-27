package osmedile.intellij.stringmanip.transform;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.CharEncoding;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import ro.isdc.wro.model.resource.processor.support.JSMin;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MinifyJavaScriptAction extends AbstractStringManipAction<Object> {
	private static final Logger LOG = Logger.getInstance(MinifyJavaScriptAction.class);

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			new JSMin(new ByteArrayInputStream(selectedText.getBytes(StandardCharsets.UTF_8)), out).jsmin();
			return out.toString(CharEncoding.UTF_8).trim();
		} catch (Throwable e) {
			SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), String.valueOf(e), StringManipulationBundle.message("dialog.title.minify.javascript")));
			LOG.info(e);
			throw new ProcessCanceledException(e);
		}
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new RuntimeException();
	}
}
package osmedile.intellij.stringmanip.transform;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.util.Computable;
import com.intellij.ui.DocumentAdapter;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import shaded.org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Philipp Menke
 */
class DelimitedListDialog implements Disposable {
	private final DelimitedListAction action;
	private final Editor editor;
	private final EditorImpl previewEditor;

	JPanel contentPane;
	JTextField destDelimiter;
	private JTextField sourceDelimiter;
	private JRadioButton sourceSelection;
	private JRadioButton sourceClipboard;
	private JCheckBox autoQuote;
	private JTextField quote;
	private JTextField unquote;
	private JPanel previewPanel;

	private ThreadPoolExecutor executor;

	DelimitedListDialog(DelimitedListAction action, Editor editor) {
		this.action = action;
		this.editor = editor;

		//max 1 concurrent task + max 1 in queue
		executor = new ThreadPoolExecutor(1, 1,
			60, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(1),
			new DefaultThreadFactory("StringManipulation.DelimitedListDialog", true),
			new ThreadPoolExecutor.DiscardPolicy());


		this.previewEditor = IdeUtils.createEditorPreview("", false);
		this.previewPanel.add(previewEditor.getComponent());
		previewEditor.getComponent().setPreferredSize(new Dimension(0, 200));

		AtomicBoolean allSelectionsEmpty = new AtomicBoolean(true);
		editor.getCaretModel().runForEachCaret(caret -> allSelectionsEmpty.set(allSelectionsEmpty.get() && !caret.hasSelection()));
		if (allSelectionsEmpty.get()) {
			sourceClipboard.setSelected(true);
		}

		final DocumentAdapter documentAdapter = new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull DocumentEvent documentEvent) {
				renderPreview();
			}
		};
		sourceDelimiter.getDocument().addDocumentListener(documentAdapter);
		destDelimiter.getDocument().addDocumentListener(documentAdapter);
		quote.getDocument().addDocumentListener(documentAdapter);
		unquote.getDocument().addDocumentListener(documentAdapter);
		sourceClipboard.addChangeListener(e -> renderPreview());
		sourceSelection.addChangeListener(e -> renderPreview());
		autoQuote.addChangeListener(e -> renderPreview());
		autoQuote.addChangeListener(e -> {
			quote.setEditable(!autoQuote.isSelected());
			if (quote.isEditable()) {
				if (quote.getText().equals("<auto>")) {
					quote.setText("");
				}
				quote.requestFocus();
			}
		});
		renderPreview();
	}

	private void renderPreview() {
		final DelimitedListAction.Settings settings = toSettings();

		executor.submit(() -> {
			//Reads of large clipboards can take a second to complete
			String sourceText = limitLength(getSourceText(settings));

			if (sourceText.isEmpty()) {
				setPreviewTextOnEDT("Clipboard doesn't contain usable data");
			} else {
				String previewText = computePreviewText(sourceText, settings);
				setPreviewTextOnEDT(previewText);
			}
		});
	}

	private String getSourceText(DelimitedListAction.Settings settings) {
		return ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
			return action.getSourceText(editor.getCaretModel().getPrimaryCaret(), settings);
		});
	}

	private String computePreviewText(String sourceText, DelimitedListAction.Settings settings) {
		return action.getTransformedText(sourceText, settings)
			.replace("\r", "");//remove all \r, which are not allowed in the Editor
	}

	private void setPreviewTextOnEDT(String s) {
		ApplicationManager.getApplication().invokeLater(() -> setPreviewText(s), ModalityState.stateForComponent(previewPanel));
	}

	private void setPreviewText(String previewText) {
		ApplicationManager.getApplication().runWriteAction(() -> {
			previewEditor.getDocument().setText(previewText);
			previewPanel.validate();
			previewPanel.repaint();
		});
	}

	DelimitedListAction.Settings toSettings() {
		DelimitedListAction.Settings settings = new DelimitedListAction.Settings();
		settings.sourceDelimiter = StringEscapeUtils.unescapeJava(sourceDelimiter.getText());
		settings.destinationDelimiter = StringEscapeUtils.unescapeJava(destDelimiter.getText());
		settings.source = sourceClipboard.isSelected() ? "CLIP" : "CARET";
		settings.unquote = StringEscapeUtils.unescapeJava(unquote.getText());
		settings.quote = autoQuote.isSelected() ? DelimitedListAction.Settings.QUOTE_AUTO : StringEscapeUtils.unescapeJava(quote.getText());
		return settings;
	}

	/**
	 * Editor cannot handle too much
	 */
	private String limitLength(String val) {
		if (val.length() > 10_000) {
			return val.substring(0, 10_000) + "\n...";
		}
		return val;
	}

	@Override
	public void dispose() {
		executor.shutdown();
	}
}

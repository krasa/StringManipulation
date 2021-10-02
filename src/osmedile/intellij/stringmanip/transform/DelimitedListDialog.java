package osmedile.intellij.stringmanip.transform;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.util.Computable;
import com.intellij.ui.DocumentAdapter;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.filter.PreviewDialog;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Philipp Menke
 */
@Deprecated
class DelimitedListDialog extends PreviewDialog implements Disposable {
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

	DelimitedListDialog(DelimitedListAction action, Editor editor) {
		this.action = action;
		this.editor = editor;

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
		sourceClipboard.addActionListener(e -> renderPreview());
		sourceSelection.addActionListener(e -> renderPreview());
		autoQuote.addActionListener(e -> renderPreview());
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

			if (settings.isClipboard() && sourceText.isEmpty()) {
				setPreviewTextOnEDT("Clipboard doesn't contain usable data");
				return;
			}

			String previewText = computePreviewText(sourceText, settings);
			setPreviewTextOnEDT(previewText);
		});
	}

	private String getSourceText(DelimitedListAction.Settings settings) {
		if (settings.isClipboard()) {
			return action.getClipBoardText();
		} else {
			return ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
				return action.getSelectedText(editor.getCaretModel().getPrimaryCaret());
			});
		}
	}

	private String computePreviewText(String sourceText, DelimitedListAction.Settings settings) {
		return action.transformText(sourceText, settings)
				.replace("\r", "");//remove all \r, which are not allowed in the Editor
	}

	protected void setPreviewTextOnEDT(String s) {
		ApplicationManager.getApplication().invokeLater(() -> setPreviewText(s), ModalityState.any());
	}

	protected void setPreviewText(String previewText) {
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
		settings.source = sourceClipboard.isSelected() ? DelimitedListAction.Settings.CLIPBOARD : DelimitedListAction.Settings.CARET;
		settings.unquote = StringEscapeUtils.unescapeJava(unquote.getText());
		settings.quote = autoQuote.isSelected() ? DelimitedListAction.Settings.QUOTE_AUTO : StringEscapeUtils.unescapeJava(quote.getText());
		return settings;
	}

}

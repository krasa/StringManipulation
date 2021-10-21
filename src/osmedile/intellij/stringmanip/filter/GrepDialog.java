package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import osmedile.intellij.stringmanip.utils.PreviewUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrepDialog extends PreviewDialog {
	private static final Logger log = LoggerFactory.getLogger(GrepDialog.class);

	private GrepAction action;
	private Editor editor;
	private String sourceTextForPreview;
	private EditorImpl myPreviewEditor;


	public JComponent contentPane;
	private JPanel previewParent;
	private JPanel myPreviewPanel;
	JTextField pattern;
	private JCheckBox regexCheckBox;
	private JCheckBox caseSensitive;
	private JCheckBox fullWords;
	private JRadioButton grepRadioButton;
	private JCheckBox groupMatching;
	private JRadioButton invertedGrepRadioButton;
	private JButton historyButton;
	JPanel settingsPanel;
	private JButton donate;

	public GrepDialog(GrepAction action, GrepSettings grepSettings, Editor editor) {
		Donate.initDonateButton(donate);

		init(grepSettings);
		this.action = action;
		this.editor = editor;
		sourceTextForPreview = PreviewUtils.getTextForPreview(editor);

		final DocumentAdapter documentAdapter = new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull DocumentEvent documentEvent) {
				renderPreview();
			}
		};
		pattern.getDocument().addDocumentListener(documentAdapter);
		regexCheckBox.addActionListener(e -> renderPreview());
		caseSensitive.addActionListener(e -> renderPreview());
		grepRadioButton.addActionListener(e -> renderPreview());
		groupMatching.addActionListener(e -> renderPreview());
		invertedGrepRadioButton.addActionListener(e -> renderPreview());
		fullWords.addActionListener(e -> renderPreview());

		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final GrepHistoryForm historyForm = new GrepHistoryForm(editor);

				DialogWrapper dialogWrapper = new DialogWrapper(GrepDialog.this.contentPane, false) {
					{
						init();
						setTitle("History");
					}

					@Nullable
					@Override
					protected String getDimensionServiceKey() {
						return "StringManipulation.GrepHistoryForm";
					}

					@Nullable
					@Override
					protected JComponent createCenterPanel() {
						return historyForm.root;
					}

					@Override
					protected void doOKAction() {
						super.doOKAction();
					}
				};

				boolean b = dialogWrapper.showAndGet();
				if (b) {
					GrepSettings model = historyForm.getModel();
					if (model != null) {
						init(model);
						renderPreview();
					}
				}

			}
		});

		renderPreview();
	}

	public GrepDialog() {
		previewParent.setVisible(false);
		historyButton.setVisible(false);
	}

	AtomicBoolean initting = new AtomicBoolean();

	protected void init(GrepSettings grepSettings) {
		initting.set(true);

		regexCheckBox.setSelected(grepSettings.isRegex());
		caseSensitive.setSelected(grepSettings.isCaseSensitive());
		fullWords.setSelected(grepSettings.isFullWords());
		groupMatching.setSelected(grepSettings.isGroupMatching());
		pattern.setText(grepSettings.getPattern());
		if (grepSettings.isInverted()) {
			invertedGrepRadioButton.setSelected(true);
		} else {
			grepRadioButton.setSelected(true);
		}

		initting.set(false);
	}

	private void renderPreview() {
		if (initting.get()) {
			return;
		}
		final GrepSettings settings = getSettings();

		executor.submit(() -> {
			try {
				String previewText = action.transform(settings, sourceTextForPreview);
				setPreviewTextOnEDT(previewText);
			} catch (Throwable e) {
				setPreviewTextOnEDT(e.getMessage());
				log.warn(e.getMessage(), e);
			}
		});
	}

	public GrepSettings getSettings() {
		GrepSettings grepSettings = new GrepSettings();
		grepSettings.setPattern(pattern.getText());
		grepSettings.setFullWords(fullWords.isSelected());
		grepSettings.setRegex(regexCheckBox.isSelected());
		grepSettings.setCaseSensitive(caseSensitive.isSelected());
		grepSettings.setInverted(invertedGrepRadioButton.isSelected());
		grepSettings.setGroupMatching(groupMatching.isSelected());
		return grepSettings;
	}


	private void setPreviewTextOnEDT(String s) {
		ApplicationManager.getApplication().invokeLater(() -> setPreviewText(s), ModalityState.any());
	}

	private void setPreviewText(String previewText) {
		ApplicationManager.getApplication().runWriteAction(() -> {
			myPreviewEditor.getDocument().setText(previewText.replace("\r", ""));
			//remove all \r, which are not allowed in the Editor
			myPreviewPanel.validate();
			myPreviewPanel.repaint();
		});
	}


	public void setData(GrepSettings data) {
		pattern.setText(data.getPattern());
		regexCheckBox.setSelected(data.isRegex());
		caseSensitive.setSelected(data.isCaseSensitive());
		fullWords.setSelected(data.isFullWords());
	}

	public void getData(GrepSettings data) {
		data.setPattern(pattern.getText());
		data.setRegex(regexCheckBox.isSelected());
		data.setCaseSensitive(caseSensitive.isSelected());
		data.setFullWords(fullWords.isSelected());
	}

	public boolean isModified(GrepSettings data) {
		if (pattern.getText() != null ? !pattern.getText().equals(data.getPattern()) : data.getPattern() != null)
			return true;
		if (regexCheckBox.isSelected() != data.isRegex()) return true;
		if (caseSensitive.isSelected() != data.isCaseSensitive()) return true;
		if (fullWords.isSelected() != data.isFullWords()) return true;
		return false;
	}

	private void createUIComponents() {
		myPreviewEditor = IdeUtils.createEditorPreview("", false);
		myPreviewPanel = (JPanel) myPreviewEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));
	}
}

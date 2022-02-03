package osmedile.intellij.stringmanip.border;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import osmedile.intellij.stringmanip.utils.PreviewDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BorderDialog extends PreviewDialog {
	private static final Logger log = LoggerFactory.getLogger(BorderDialog.class);

	private AddBorderAction action;
	private String sourceTextForPreview;
	private EditorImpl myPreviewEditor;


	public JComponent contentPane;
	private JPanel previewParent;
	private JPanel myPreviewPanel;
	private JButton donate;
	private JTextField padding;
	private JRadioButton borderDouble;
	private JTextField borderWidth;
	private JTextField customBorder;
	private JRadioButton borderCustom;
	private JRadioButton borderSingle;
	private int tabSize;

	public BorderDialog(AddBorderAction action, BorderSettings borderSettings, Editor editor) {
		super(editor);
		tabSize = EditorUtil.getTabSize(editor);
		Donate.initDonateButton(donate);
		init(borderSettings);
		this.action = action;
		sourceTextForPreview = PreviewDialog.getTextForPreview(editor);

		final DocumentAdapter documentAdapter = new DocumentAdapter() {

			@Override
			protected void textChanged(@NotNull DocumentEvent documentEvent) {
				submitRenderPreview();
			}
		};

		borderSingle.addActionListener(e -> submitRenderPreview());
		borderCustom.addActionListener(e -> submitRenderPreview());
		borderDouble.addActionListener(e -> submitRenderPreview());

		padding.getDocument().addDocumentListener(documentAdapter);
		borderWidth.getDocument().addDocumentListener(documentAdapter);
		customBorder.getDocument().addDocumentListener(documentAdapter);
		submitRenderPreview();
	}

	public BorderDialog() {
		super(null);
		previewParent.setVisible(false);
		donate.setVisible(false);
	}

	AtomicBoolean initting = new AtomicBoolean();

	protected void init(BorderSettings borderSettings) {
		initting.set(true);
		setData(borderSettings);
		initting.set(false);
	}


	@Override
	protected void renderPreviewAsync(Object input) {
		if (initting.get()) {
			return;
		}
		String previewText;
		try {
			previewText = action.transform(getSettings(), sourceTextForPreview, tabSize);
		} catch (Throwable e) {
			previewText = e.getMessage();
			log.warn(e.getMessage(), e);
		}
		setPreviewTextOnEDT(previewText);
	}

	@NotNull
	@Override
	public JComponent getPreferredFocusedComponent() {
		return borderDouble;
	}

	@NotNull
	@Override
	public JComponent getRoot() {
		return contentPane;
	}

	public BorderSettings getSettings() {
		BorderSettings borderSettings = new BorderSettings();
		getData(borderSettings);
		return borderSettings;
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


	private void createUIComponents() {
		myPreviewEditor = IdeUtils.createEditorPreview("", false, this);
		myPreviewPanel = (JPanel) myPreviewEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));
	}

	public void
	setData(BorderSettings data) {
		padding.setText(data.getPadding());
		borderWidth.setText(data.getBorderWidth());
		customBorder.setText(data.getCustomBorder());
		borderCustom.setSelected(data.isBorderCustom());
		borderSingle.setSelected(data.isBorderSingle());
		borderDouble.setSelected(data.isBorderDouble());
	}

	public void getData(BorderSettings data) {
		data.setPadding(padding.getText());
		data.setBorderWidth(borderWidth.getText());
		data.setCustomBorder(customBorder.getText());
		data.setBorderCustom(borderCustom.isSelected());
		data.setBorderSingle(borderSingle.isSelected());
		data.setBorderDouble(borderDouble.isSelected());
	}

	public boolean isModified(BorderSettings data) {
		if (padding.getText() != null ? !padding.getText().equals(data.getPadding()) : data.getPadding() != null)
			return true;
		if (borderWidth.getText() != null ? !borderWidth.getText().equals(data.getBorderWidth()) : data.getBorderWidth() != null)
			return true;
		if (customBorder.getText() != null ? !customBorder.getText().equals(data.getCustomBorder()) : data.getCustomBorder() != null)
			return true;
		return false;
	}
}

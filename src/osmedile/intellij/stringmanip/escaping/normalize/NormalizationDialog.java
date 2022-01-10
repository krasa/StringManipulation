package osmedile.intellij.stringmanip.escaping.normalize;

import com.google.common.base.Joiner;
import com.intellij.diff.DiffContentFactoryEx;
import com.intellij.diff.DiffManagerEx;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.contents.DocumentContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.PreviewDialog;
import osmedile.intellij.stringmanip.utils.StringUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NormalizationDialog extends PreviewDialog implements Disposable {
	private static final Logger LOG = Logger.getInstance(NormalizationDialog.class);
	private final List<String> previewLines;

	public JPanel contentPane;

	public JRadioButton formNFD;
	protected JRadioButton formNFC;
	protected JRadioButton formNFKD;
	private JRadioButton formNFKC;
	private JRadioButton stripAccents;

	public JPanel donatePanel;
	private JPanel myPreviewPanel;
	private JPanel previewParent;
	public JPanel coreWithoutPreview;
	private JPanel myHexaPreviewPanel;
	private JCheckBox unescapeBeforeCheckBox;
	private JCheckBox escapeAfter;
	private LinkLabel linkLabel;
	private JRadioButton convertDiacritics;
	private LinkLabel stripAccentsLink;
	private JRadioButton nothing;
	private LinkLabel unicodeSupport;

	private final Editor editor;
	private DiffRequestPanel previewDiff;
	private DiffRequestPanel hexaDiff;

	private void updateComponents() {
		submitRenderPreview();
	}

	public NormalizationDialog(@NotNull NormalizationSettings settings, @NotNull Editor editor) {
		this.editor = editor;
		init(settings);
		previewLines = PreviewDialog.getPreviewLines(editor);

		linkLabel.setListener(
				(aSource, aLinkData) -> BrowserUtil.browse((String) aLinkData),
				"https://github.com/krasa/StringManipulation/blob/38a6af7385be0f17a9b3355b42a90a50d89f3cec/src/osmedile/intellij/stringmanip/escaping/DiacriticsToAsciiAction.java#L22-L93");

		stripAccentsLink.setListener(
				(aSource, aLinkData) -> BrowserUtil.browse((String) aLinkData),
				"http://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html#stripAccents-java.lang.String-");

		unicodeSupport.setListener(
				(aSource, aLinkData) -> BrowserUtil.browse((String) aLinkData),
				"https://youtrack.jetbrains.com/issue/JBR-2875");

		contentPane.setFocusTraversalPolicy(new ComponentsListFocusTraversalPolicy() {
			@NotNull
			@Override
			protected List<Component> getOrderedComponents() {
				List<Component> jRadioButtons = new ArrayList<Component>();
				jRadioButtons.add(nothing);
				jRadioButtons.add(formNFD);
				jRadioButtons.add(formNFC);
				jRadioButtons.add(formNFKD);
				jRadioButtons.add(formNFKC);
				jRadioButtons.add(stripAccents);
				jRadioButtons.add(convertDiacritics);

				jRadioButtons.add(unescapeBeforeCheckBox);
				jRadioButtons.add(escapeAfter);
				return jRadioButtons;
			}
		});
		updateComponents();
		donatePanel.add(Donate.newDonateButton());

		addPreviewListeners(this);
	}

	private void addPreviewListeners(Object object) {
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				Object o = field.get(object);
				if (o instanceof JToggleButton) {
					JToggleButton button = (JToggleButton) o;
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							updateComponents();
						}
					});
				}
				if (o instanceof JTextField) {
					JTextField jTextField = (JTextField) o;
					jTextField.getDocument().addDocumentListener(new DocumentAdapter() {
						@Override
						protected void textChanged(DocumentEvent e) {
							updateComponents();
						}
					});
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void renderPreviewAsync() {
		List<String> normalized = Normalizator.normalizeLines(previewLines, getSettings());

		String originalText = Joiner.on("\n").join(previewLines);
		String normalizedText = Joiner.on("\n").join(normalized);

		String originalHex = asciiToHex(previewLines);
		String normalizedHex = asciiToHex(normalized);

		ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
			DiffContentFactoryEx factoryEx = DiffContentFactoryEx.getInstanceEx();

			previewDiff.setRequest(new SimpleDiffRequest(null, factoryEx.create(originalText), factoryEx.create(normalizedText), "Before", "After"));

			hexaDiff.setRequest(new SimpleDiffRequest(null, factoryEx.create(originalHex), factoryEx.create(normalizedHex), "Before", "After"));
		}), ModalityState.any());
	}

	@NotNull
	@Override
	public JComponent getPreferredFocusedComponent() {
		return contentPane;
	}

	@NotNull
	@Override
	public JComponent getRoot() {
		return contentPane;
	}

	private String asciiToHex(List<String> lines) {
		StringBuilder hex = new StringBuilder();
		for (String line : lines) {
			line = StringUtil.escapedUnicodeToString(line);

			char[] chars = line.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				hex.append(Integer.toHexString((int) chars[i]));
				hex.append(" ");
			}
			hex.append("\n");
		}
		return hex.toString();
	}


	public void init(NormalizationSettings settings) {
		setData(settings);
		switch (settings.getType()) {

			case NOTHING:
				nothing.setSelected(true);
				break;
			case NFD:
				formNFD.setSelected(true);
				break;
			case NFC:
				formNFC.setSelected(true);
				break;
			case NFKD:
				formNFKD.setSelected(true);
				break;
			case NFKC:
				formNFKC.setSelected(true);
				break;
			case STRIP_ACCENTS:
				stripAccents.setSelected(true);
				break;
			case CONVERT_DIACRITICS:
				convertDiacritics.setSelected(true);
				break;
		}


	}


	public NormalizationSettings getSettings() {
		NormalizationSettings normalizationSettings = new NormalizationSettings();
		getData(normalizationSettings);
		if (nothing.isSelected()) {
			normalizationSettings.setType(NormalizationType.NOTHING);
		} else if (formNFD.isSelected()) {
			normalizationSettings.setType(NormalizationType.NFD);
		} else if (formNFC.isSelected()) {
			normalizationSettings.setType(NormalizationType.NFC);
		} else if (formNFKD.isSelected()) {
			normalizationSettings.setType(NormalizationType.NFKD);
		} else if (formNFKC.isSelected()) {
			normalizationSettings.setType(NormalizationType.NFKC);
		} else if (convertDiacritics.isSelected()) {
			normalizationSettings.setType(NormalizationType.CONVERT_DIACRITICS);
		} else if (stripAccents.isSelected()) {
			normalizationSettings.setType(NormalizationType.STRIP_ACCENTS);
		}
		return normalizationSettings;
	}


	private void createUIComponents() {
		previewDiff = createDiff("", "");
		hexaDiff = createDiff("", "");
		myPreviewPanel = (JPanel) previewDiff.getComponent();
		myHexaPreviewPanel = (JPanel) hexaDiff.getComponent();
	}

	@NotNull
	private DiffRequestPanel createDiff(String foo, String bar) {
		DocumentContent documentContent = DiffContentFactoryEx.getInstanceEx().create(foo);
		DocumentContent documentContent2 = DiffContentFactoryEx.getInstanceEx().create(bar);
		SimpleDiffRequest simpleDiffRequest = new SimpleDiffRequest(null, documentContent, documentContent2, "Before", "After");
		DiffRequestPanel requestPanel = DiffManagerEx.getInstance().createRequestPanel(editor.getProject(), this, null);
		requestPanel.setRequest(simpleDiffRequest);
		return requestPanel;
	}

	public void setData(NormalizationSettings data) {
		unescapeBeforeCheckBox.setSelected(data.isUnescapeBefore());
		escapeAfter.setSelected(data.isEscapeAfter());
	}

	public void getData(NormalizationSettings data) {
		data.setUnescapeBefore(unescapeBeforeCheckBox.isSelected());
		data.setEscapeAfter(escapeAfter.isSelected());
	}

	public boolean isModified(NormalizationSettings data) {
		if (unescapeBeforeCheckBox.isSelected() != data.isUnescapeBefore()) return true;
		if (escapeAfter.isSelected() != data.isEscapeAfter()) return true;
		return false;
	}

	@Override
	public void dispose() {

	}
}

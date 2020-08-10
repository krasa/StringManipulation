package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.nio.charset.Charset;

public class Base64EncodingDialog {
	protected JPanel contentPane;
	protected JRadioButton defaultRadioButton;
	protected JRadioButton urlSafe;
	protected JRadioButton urlSafeChunked;
	protected JRadioButton chunked;
	protected JComboBox myComboBox;
	JRadioButton zip;
	protected JPanel encodingOptions;
	JRadioButton inflateDeflate;
	protected JRadioButton noCompression;
	protected JRadioButton lf;
	protected JRadioButton crlf;
	protected JPanel lineEnd;
	public DefaultComboBoxModel model;
	public Color defaultColor;

	public Base64EncodingDialog() {
	}

	private void createUIComponents() {
		model = new DefaultComboBoxModel(new String[]{
				"UTF-8",
				"ASCII",
						"CP1256",
						"ISO-8859-1",
						"ISO-8859-2",
						"ISO-8859-6",
						"ISO-8859-15",
						"Windows-1252"});
		myComboBox = new ComboBox(model, 20);
		myComboBox.setEditable(true);
		myComboBox.setOpaque(true);
		defaultColor = myComboBox.getForeground();
		myComboBox.setSelectedItem("UTF-8");
		final JTextComponent tc = (JTextComponent) myComboBox.getEditor().getEditorComponent();

		tc.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(DocumentEvent documentEvent) {
				JTextField editorJComp = (JTextField) myComboBox.getEditor().getEditorComponent();
				try {
					Charset instance = Charset.forName(getCharset());
					editorJComp.setForeground(defaultColor);
				} catch (Exception ee) {
					editorJComp.setForeground(JBColor.RED);
				}
			}
		});

	}

	@NotNull
	protected String getCharset() {
		final JTextComponent tc = (JTextComponent) myComboBox.getEditor().getEditorComponent();
		return tc.getText().trim();
	}
}

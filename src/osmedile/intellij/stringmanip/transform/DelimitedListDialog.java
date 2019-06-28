package osmedile.intellij.stringmanip.transform;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Editor;
import com.intellij.ui.DocumentAdapter;

import shaded.org.apache.commons.text.StringEscapeUtils;

/**
 * @author Philipp Menke
 */
class DelimitedListDialog extends JDialog {
    private static final long                serialVersionUID = -9173380044265093635L;
    private final        DelimitedListAction action;
    private final        Editor              editor;

    JPanel     contentPane;
    JTextField tfDestDelimiter;
    private JTextField   tfSourceDelimiter;
    private JRadioButton rbSourceSelection;
    private JRadioButton rbSourceClipboard;
    private JCheckBox    cbAutoQuote;
    private JTextField   tfQuote;
    private JEditorPane  epPreview;
    private JTextField   tfUnquote;

    DelimitedListDialog(DelimitedListAction action, Editor editor) {
        this.action = action;
        this.editor = editor;
        setContentPane(contentPane);
        AtomicBoolean allSelectionsEmpty = new AtomicBoolean(true);
        editor.getCaretModel().runForEachCaret(caret -> allSelectionsEmpty.set(allSelectionsEmpty.get() && !caret.hasSelection()));
        if (allSelectionsEmpty.get()) {
            rbSourceClipboard.setSelected(true);
        }

        final DocumentAdapter documentAdapter = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent documentEvent) {
                renderPreview();
            }
        };
        tfSourceDelimiter.getDocument().addDocumentListener(documentAdapter);
        tfDestDelimiter.getDocument().addDocumentListener(documentAdapter);
        tfQuote.getDocument().addDocumentListener(documentAdapter);
        tfUnquote.getDocument().addDocumentListener(documentAdapter);
        rbSourceClipboard.addChangeListener(e -> renderPreview());
        rbSourceSelection.addChangeListener(e -> renderPreview());
        cbAutoQuote.addChangeListener(e -> renderPreview());
        cbAutoQuote.addChangeListener(e -> {
            tfQuote.setEditable(!cbAutoQuote.isSelected());
            if (tfQuote.isEditable()) {
                if (tfQuote.getText().equals("<auto>")) {
                    tfQuote.setText("");
                }
                tfQuote.requestFocus();
            }
        });
        renderPreview();
    }

    private void renderPreview() {
        final DelimitedListAction.Settings settings = toSettings();
        epPreview.setText(action.getTransformedText(
                limitLength(action.getSourceText(editor.getCaretModel().getPrimaryCaret(), settings)),
                settings));
    }

    DelimitedListAction.Settings toSettings() {
        DelimitedListAction.Settings settings = new DelimitedListAction.Settings();
        settings.sourceDelimiter = StringEscapeUtils.unescapeJava(tfSourceDelimiter.getText());
        settings.destinationDelimiter = StringEscapeUtils.unescapeJava(tfDestDelimiter.getText());
        settings.source = rbSourceClipboard.isSelected() ? "CLIP" : "CARET";
        settings.unquote = StringEscapeUtils.unescapeJava(tfUnquote.getText());
        settings.quote = cbAutoQuote.isSelected() ? DelimitedListAction.Settings.QUOTE_AUTO : StringEscapeUtils.unescapeJava(tfQuote.getText());
        return settings;
    }

    private String limitLength(String val) {
        if (val.length() > 1024) {
            return val.substring(0, 1024);
        }
        return val;
    }
}

package osmedile.intellij.stringmanip.transform;

import com.google.common.util.concurrent.MoreExecutors;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import shaded.org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Philipp Menke
 */
class DelimitedListDialog {
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

    private AtomicInteger pendingClipboardReads = new AtomicInteger();

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
        //Reads of large clipboards can take a second to complete,
        //so avoid build a queue of many reads...
        if (settings.source.equals("CLIP") && pendingClipboardReads.incrementAndGet() > 2) {
            pendingClipboardReads.decrementAndGet();
            return;
        }
        final CompletableFuture<String> sourceTextFuture = CompletableFuture
                .supplyAsync(() -> action.getSourceText(editor.getCaretModel().getPrimaryCaret(), settings),
                        settings.source.equals("CLIP") ? ForkJoinPool.commonPool() : MoreExecutors.directExecutor());

        sourceTextFuture.thenAccept(sourceText -> {
            if (settings.source.equals("CLIP")) {
                pendingClipboardReads.decrementAndGet();
                if (sourceText.isEmpty()) {
                    ApplicationManager.getApplication().invokeLater(() -> setPreviewText("Clipboard doesn't contain usable data"),
                            ModalityState.stateForComponent(previewPanel));
                    return;
                }
            }
            CompletableFuture.supplyAsync(() -> computePreviewText(sourceText, settings),
                    //perform transformation async, if text is large
                    sourceText.length() > 10240 ? ForkJoinPool.commonPool() : MoreExecutors.directExecutor())
                    .thenAccept(text -> ApplicationManager.getApplication().invokeLater(
                            () -> this.setPreviewText(text), ModalityState.stateForComponent(previewPanel)));
        });
    }

    private String computePreviewText(String sourceText, DelimitedListAction.Settings settings) {
        return action.getTransformedText(sourceText, settings)
                .replace("\r", "");//remove all \r, which are not allowed in the Editor
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
}

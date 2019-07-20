package osmedile.intellij.stringmanip.transform;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyApplicationComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Philipp Menke
 */
public class DelimitedListAction extends EditorAction {
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");

	public DelimitedListAction() {
		super(null);
		setupHandler(new EditorActionHandler() {
			@Override
			protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
				MyApplicationComponent.setAction(DelimitedListAction.class);
				final Pair<Boolean, Settings> dialogResult = DelimitedListAction.this.showDialog(editor);
				if (!dialogResult.first) {
					return;
				}

				new EditorWriteActionHandler() {
					@Override
					public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
						DelimitedListAction.this.execute(editor, caret, dataContext, dialogResult.second);
					}
				}.doExecute(editor, caret, dataContext);
			}
		});
	}

	static class Settings {
		//sentinel value for auto-quoting
		static final String QUOTE_AUTO = "\ue123AUTO\ue321";
		String unquote;
		String quote;
		String sourceDelimiter;
		String destinationDelimiter;
		String source;
	}

	private void execute(Editor editor, Caret mainCaret, DataContext dataContext, Settings settings) {
		editor.getCaretModel().runForEachCaret(caret -> {
			final String sourceText = getSourceText(caret, settings);
			final String transformedText = getTransformedText(sourceText, settings);
			editor.getDocument().replaceString(caret.getSelectionStart(), caret.getSelectionEnd(), transformedText);
			//if no text was selected, move the care past the inserted
			//text, as it would be during a normal Ctrl-V paste
			if (getSelectedText(caret).isEmpty()) {
				caret.moveToOffset(caret.getOffset() + transformedText.length());
			}
		});
	}

	String getTransformedText(String sourceText, Settings settings) {
		final String[] elements = sourceText.split(Pattern.quote(settings.sourceDelimiter));
		final StringBuilder result = new StringBuilder(sourceText.length() + 512);
		for (int i = 0; i < elements.length; i++) {
			if (i > 0) {
				result.append(settings.destinationDelimiter);
			}
			final String sourceElement = elements[i];
			final String unquotedElement;
			if (!settings.unquote.isEmpty()) {
				unquotedElement = sourceElement.substring(
					sourceElement.startsWith(settings.unquote) ? settings.unquote.length() : 0,
					sourceElement.endsWith(settings.unquote) ? sourceElement.length() - settings.unquote.length() : sourceElement.length()
				);
			} else {
				unquotedElement = sourceElement;
			}
			final String quote = Settings.QUOTE_AUTO.equals(settings.quote) ?
				!NUMBER_PATTERN.matcher(unquotedElement).matches() ?
					"'" :
					"" : settings.quote;
			result.append(quote);
			result.append(unquotedElement);
			result.append(quote);
		}
		return result.toString();
	}

	String getSourceText(Caret caret, Settings settings) {
		if (settings.source.equals("CLIP")) {
			return getClipBoardText();
		} else {
			return getSelectedText(caret);
		}
	}

	private String getSelectedText(Caret caret) {
		final String originalText;
		if (caret.hasSelection()) {
			originalText = caret.getSelectedText() + "";//avoid null value
		} else {
			originalText = "";
		}
		return originalText;
	}

	private String getClipBoardText() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException ignored) {
			return "";
		}
	}

	private Pair<Boolean, Settings> showDialog(Editor editor) {
		final DelimitedListDialog dialog = new DelimitedListDialog(this, editor);
		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Delimited List Options");
			}

			@Override
			protected void dispose() {
				super.dispose();
				dialog.dispose();
			}

			@Override
			public JComponent getPreferredFocusedComponent() {
				return dialog.destDelimiter;
			}

			@Override
			protected String getDimensionServiceKey() {
				return "StringManipulation.DelimitedListDialog";
			}

			@Override
			protected JComponent createCenterPanel() {
				return dialog.contentPane;
			}

			@Override
			protected void doOKAction() {
				super.doOKAction();
			}
		};

		boolean okPressed = dialogWrapper.showAndGet();
		if (!okPressed) {
			return Pair.create(false, null);
		}

		return Pair.create(true, dialog.toSettings());
	}
}
package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;
import osmedile.intellij.stringmanip.utils.StringUtils;

public class SwapCharactersAction extends EditorAction {

	protected SwapCharactersAction() {
		super(null);

		this.setupHandler(new EditorWriteActionHandler(true) {

			public void executeWriteAction(Editor editor, DataContext dataContext) {
				final SelectionModel selectionModel = editor.getSelectionModel();

				int selectionStart = selectionModel.getSelectionStart();
				int selectionEnd = selectionModel.getSelectionEnd();

				final Document document = editor.getDocument();
				final int textLength = document.getTextLength();
				if (selectionModel.hasBlockSelection()) {
					int[] blockStarts = selectionModel.getBlockSelectionStarts();
					int[] blockEnds = selectionModel.getBlockSelectionEnds();
					int plusOffset = 0;

					for (int i = 0; i < blockStarts.length; i++) {
						int blockStart = blockStarts[i];
						int blockEnd = blockEnds[i];
						if (blockStart == blockEnd) {
							blockStart--;
							blockEnd++;
						}
						if (blockStart < 0 || blockEnd > textLength) {
							continue;
						}
						String selectedText = document.getText(TextRange.create(blockStart, blockEnd));
						String newTextPart = transform(selectedText);

						document.replaceString(blockStart + plusOffset, blockEnd + plusOffset, newTextPart);

						int realOldTextLength = blockEnd - blockStart;
						plusOffset += newTextPart.length() - realOldTextLength;
					}
				} else {
					String selectedText = selectionModel.getSelectedText();
					if (selectedText == null) {
						selectionStart = selectionStart - 1;
						selectionEnd = selectionEnd + 1;
						if (selectionStart < 0 || selectionEnd > textLength) {
							return;
						}
						selectedText = document.getText(TextRange.create(selectionStart, selectionEnd));
					}

					if (selectedText == null) {
						return;
					}

					document.replaceString(selectionStart,
							selectionEnd, transform(selectedText));
				}
			}
		});
	}

	public String transform(String s) {
		if (s.contains("\n")) {
			return s;
		}
		if (s.length() != 2) {
			return s;
		}
		return s.charAt(1) + "" + s.charAt(0);
	}
}

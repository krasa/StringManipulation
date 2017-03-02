package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;

import java.util.ArrayList;
import java.util.List;

public class AlignToColumnsAction extends EditorAction {

	private String lastSeparator = ",";

	protected AlignToColumnsAction() {
		this(true);
	}

	protected AlignToColumnsAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new MultiCaretHandlerHandler<String>() {

				@NotNull
				@Override
				protected Pair<Boolean, String> beforeWriteAction(Editor editor, DataContext dataContext) {
					String separator = chooseSeparator();
					if (separator == null) {
						return stopExecution();
					}
					return continueExecution(separator);
				}

				private String chooseSeparator() {
					String separator = Messages.showInputDialog("Separator", "Separator", Messages.getQuestionIcon(),
							lastSeparator, null);
					if (separator != null) {
						if (separator.equals("")) {
							separator = " ";
						}
						lastSeparator = separator;
					} else {
						return null;
					}
					return separator;
				}

				@Override
				protected String processSingleSelection(String text, String separator) {
					return new ColumnAligner().reformat(separator, text);
				}

				@Override
				protected List<String> processMultiSelections(List<String> lines, String separator) {
					List<ColumnAlignerLine> columnAlignerLines = new ArrayList<ColumnAlignerLine>();
					for (String line : lines) {
						columnAlignerLines.add(new ColumnAlignerLine(separator, line, line.endsWith("\n")));
					}
					return new ColumnAligner().process(columnAlignerLines);
				}
			});
		}

	}


}

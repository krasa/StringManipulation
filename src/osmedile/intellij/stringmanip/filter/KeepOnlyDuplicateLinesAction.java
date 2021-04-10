package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.FilterUtils;

import java.util.Arrays;
import java.util.List;

public class KeepOnlyDuplicateLinesAction extends MyEditorAction {

	public KeepOnlyDuplicateLinesAction() {
		super(null);
		setupHandler(new MultiCaretHandlerHandler<Void>(getActionClass()) {
			@NotNull
			@Override
			protected Pair<Boolean, Void> beforeWriteAction(Editor editor, DataContext dataContext) {
				SelectionModel selectionModel = editor.getSelectionModel();
				if (!selectionModel.hasSelection()) {
					selectionModel.setSelection(0, editor.getDocument().getTextLength());
				}
				return super.beforeWriteAction(editor, dataContext);
			}

			@Override
			protected String processSingleSelection(Editor editor, String text, Void additionalParameter) {
				String[] split = text.split("\n");
				String[] uniqueLines = FilterUtils.keepDuplicates(split);
				return join(uniqueLines);
			}

			@Override
			protected List<String> processMultiSelections(Editor editor, List<String> lines, Void additionalParameter) {
				String[] split = lines.toArray(FilterUtils.EMPTY);
				String[] uniqueLines = FilterUtils.keepDuplicates(split);
				return Arrays.asList(uniqueLines);
			}


			private String join(String[] array) {
				StringBuilder buf = new StringBuilder();
				for (int i = 0; i < array.length; i++) {
					if (array[i] != null && i > 0) {
						buf.append("\n");
					}
					if (array[i] != null) {
						buf.append(array[i]);
					}
				}
				return buf.toString();
			}


		});
	}

}
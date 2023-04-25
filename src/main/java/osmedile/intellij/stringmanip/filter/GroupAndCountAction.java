package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.utils.FilterUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupAndCountAction extends MyEditorAction {

	public GroupAndCountAction() {
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
				List<FilterUtils.Group> uniqueLines = FilterUtils.groupAndSortByCount(split);
				return join(uniqueLines);
			}

			@Override
			protected List<String> processMultiSelections(Editor editor, List<String> lines, Void additionalParameter) {
				String[] split = lines.toArray(FilterUtils.EMPTY);
				List<FilterUtils.Group> uniqueLines = FilterUtils.groupAndSortByCount(split);
				return toString(uniqueLines);
			}

			private List<String> toString(List<FilterUtils.Group> uniqueLines) {
				ArrayList<String> strings = new ArrayList<>(uniqueLines.size());
				for (FilterUtils.Group uniqueLine : uniqueLines) {
					strings.add(uniqueLine.count + " " + uniqueLine.text);
				}
				return strings;
			}

			private String join(List<FilterUtils.Group> list) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < list.size(); i++) {
					FilterUtils.Group group = list.get(i);
					if (group != null && i > 0) {
						sb.append("\n");
					}
					if (group != null) {
						sb.append(group.count).append(" ").append(group.text);
					}
				}
				return sb.toString();
			}


		});
	}


}
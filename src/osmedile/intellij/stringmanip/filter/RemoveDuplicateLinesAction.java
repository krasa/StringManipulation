package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveDuplicateLinesAction extends EditorAction {

	public RemoveDuplicateLinesAction() {
		super(new MultiCaretHandlerHandler<Void>() {

			@Override
			protected String processSingleSelection(String text, Void additionalParameter) {
				String[] split = text.split("\n");
				String[] uniqueLines = filter(split);
				return join(uniqueLines);
			}

			@Override
			protected List<String> processMultiSelections(List<String> lines, Void additionalParameter) {
				String[] split = lines.toArray(new String[0]);
				String[] uniqueLines = filter(split);
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

			private String[] filter(String[] split) {
				if (split.length > 0) {
					Set<String> set = new HashSet<String>();
					for (int i = 0; i < split.length; i++) {
						boolean unique = set.add(split[i]);
						if (!unique) {
							split[i] = null;
						}
					}
					return split;
				}
				return split;
			}

		});
	}
}
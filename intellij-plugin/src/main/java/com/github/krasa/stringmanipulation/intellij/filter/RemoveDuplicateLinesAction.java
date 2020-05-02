package com.github.krasa.stringmanipulation.intellij.filter;

import com.github.krasa.stringmanipulation.intellij.MultiCaretHandlerHandler;
import com.github.krasa.stringmanipulation.intellij.MyEditorAction;
import com.github.krasa.stringmanipulation.utils.common.FilterUtils;

import java.util.Arrays;
import java.util.List;

public class RemoveDuplicateLinesAction extends MyEditorAction {

	public RemoveDuplicateLinesAction() {
		super(null);
		setupHandler(new MultiCaretHandlerHandler<Void>(getActionClass()) {

			@Override
			protected String processSingleSelection(String text, Void additionalParameter) {
				String[] split = text.split("\n");
				String[] uniqueLines = FilterUtils.filterDuplicates(split);
				return join(uniqueLines);
			}

			@Override
			protected List<String> processMultiSelections(List<String> lines, Void additionalParameter) {
				String[] split = lines.toArray(new String[0]);
				String[] uniqueLines = FilterUtils.filterDuplicates(split);
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
// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.find.FindBundle;
import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.replace.gui.commonn.MySearchTextArea;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FromTextArea extends MySearchTextArea {

	private final AtomicBoolean caseSensitive;
	private final AtomicBoolean wholeWords;
	private final AtomicBoolean regex;
	private ItemForm parent;
	private ReplaceItemModel replaceItemModel;


	public FromTextArea(ReplaceItemModel model, ItemForm itemForm) {
		super(createJbTextArea(), true, itemForm.history);
		parent = itemForm;
		caseSensitive = new AtomicBoolean();
		MySwitchStateToggleAction myCaseSensitiveAction =
				new MySwitchStateToggleAction(
						AllIcons.Actions.MatchCase, AllIcons.Actions.MatchCaseHovered, AllIcons.Actions.MatchCaseSelected,
						caseSensitive, () -> Boolean.TRUE, FindBundle.message("find.popup.case.sensitive"), itemForm.history);
		wholeWords = new AtomicBoolean();
		MySwitchStateToggleAction myWholeWordsAction =
				new MySwitchStateToggleAction(
						AllIcons.Actions.Words, AllIcons.Actions.WordsHovered, AllIcons.Actions.WordsSelected,
						wholeWords, () -> Boolean.TRUE, FindBundle.message("find.whole.words"), itemForm.history);
		regex = new AtomicBoolean();
		MySwitchStateToggleAction myRegexAction =
				new MySwitchStateToggleAction(
						AllIcons.Actions.Regex, AllIcons.Actions.RegexHovered, AllIcons.Actions.RegexSelected,
						regex, () -> Boolean.TRUE, FindBundle.message("find.regex"), itemForm.history);


		setExtraActions(myCaseSensitiveAction, myWholeWordsAction, myRegexAction);
		load(model);
	}

	private static JBTextArea createJbTextArea() {
		JBTextArea innerTextComponent = new JBTextArea();
		innerTextComponent.setRows(1);
		innerTextComponent.setColumns(12);
		innerTextComponent.setMinimumSize(new Dimension(100, 0));
		return innerTextComponent;
	}

	public void load(ReplaceItemModel replaceItemModel) {
		if (replaceItemModel == null) {
			return;
		}
		this.replaceItemModel = replaceItemModel;
		caseSensitive.set(this.replaceItemModel.isCaseSensitive());
		regex.set(replaceItemModel.isRegex());
		wholeWords.set(replaceItemModel.isCaseSensitive());
		getTextArea().setText(replaceItemModel.getFrom());
	}

	@Override
	protected void apply() {
		if (this.replaceItemModel != null) {
			replaceItemModel.setCaseSensitive(caseSensitive.get());
			replaceItemModel.setFrom(getTextArea().getText());
			replaceItemModel.setRegex(regex.get());
			replaceItemModel.setWholeWords(wholeWords.get());
		}
		if (parent != null && replaceItemModel != null) {
			parent.apply(replaceItemModel);
		}
	}

	@NotNull
	@Override
	protected String getText(ReplaceItemModel replaceCompositeModel) {
		return replaceCompositeModel.getFrom();
	}

	@Override
	protected void reload(ReplaceItemModel selectedValue) {
		load(selectedValue);
	}


}

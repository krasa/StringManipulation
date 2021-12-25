// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.find.FindBundle;
import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.replace.gui.commonn.MySearchTextArea;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToTextArea extends MySearchTextArea {

	private final AtomicBoolean preserveCase;
	private ItemForm parent;
	private ReplaceItemModel replaceItemModel;

	public ToTextArea(ReplaceItemModel model, ItemForm itemForm) {
		super(createJbTextArea(), false, itemForm.history);
		parent = itemForm;

		preserveCase = new AtomicBoolean();
		MySwitchStateToggleAction myCaseSensitiveAction =
				new MySwitchStateToggleAction(
						AllIcons.Actions.PreserveCase, AllIcons.Actions.PreserveCaseHover, AllIcons.Actions.PreserveCaseSelected,
						preserveCase, () -> Boolean.TRUE, FindBundle.message("find.options.replace.preserve.case"), itemForm.history);
		setExtraActions(myCaseSensitiveAction);
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
		preserveCase.set(replaceItemModel.isPreserveCase());
		getTextArea().setText(replaceItemModel.getTo());
	}

	@Override
	protected void apply() {
		if (this.replaceItemModel != null) {
			this.replaceItemModel.setPreserveCase(preserveCase.get());
			this.replaceItemModel.setTo(getTextArea().getText());
		}
		if (parent != null && replaceItemModel != null) {
			parent.apply(replaceItemModel);
		}
	}

	@NotNull
	@Override
	protected String getText(ReplaceItemModel replaceCompositeModel) {
		return replaceCompositeModel.getTo();
	}

	@Override
	protected void reload(ReplaceItemModel selectedValue) {
		load(selectedValue);
	}


}

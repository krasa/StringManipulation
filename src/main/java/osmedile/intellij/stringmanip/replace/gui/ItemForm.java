package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.ui.JBEmptyBorder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class ItemForm extends JPanel implements DataProvider {
	public static final DataKey<ItemForm> PANEL = DataKey.create("ReplacementItemForm");

	private JPanel fromPanel;
	private JPanel toPanel;

	private JPanel root;
	private JCheckBox enabledCheckBox;
	private JPanel buttons;
	private JTextField exclusiveGroup;
	private FromTextArea from;
	private ToTextArea to;
	private ReplaceItemModel model;
	boolean history;
	private CompositeForm compositeForm;

	public ItemForm(ReplaceItemModel model, boolean history) {
		this.model = model;
		this.history = history;
		add(root);
		enabledCheckBox.setEnabled(!history);
		enabledCheckBox.addActionListener(e -> model.setEnabled(enabledCheckBox.isSelected()));
		if (!history) {
			buttons.add(createToolbar().getComponent(), BorderLayout.NORTH);
		}
		enabledCheckBox.setSelected(model.isEnabled());

		exclusiveGroup.setText(model.getExclusiveGroup());
		exclusiveGroup.setEnabled(!history);
		exclusiveGroup.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull DocumentEvent e) {
				apply(model);
			}
		});
	}

	public ReplaceItemModel getModel() {
		return model;
	}

	@NotNull
	private ActionToolbar createToolbar() {
		final ActionManager actionManager = ActionManager.getInstance();

		DefaultActionGroup newGroup = new DefaultActionGroup();
		newGroup.add(actionManager.getAction("StringManipulation.ToolWindow.Replace.Item"));

		ActionToolbar actionToolbar = actionManager.createActionToolbar("StringManipulation-ReplaceToolWindow-Item", newGroup, true);
		final ActionToolbarImpl editorToolbar = ((ActionToolbarImpl) actionToolbar);
		editorToolbar.setOpaque(false);
		editorToolbar.setBorder(new JBEmptyBorder(0, 0, 0, 0));
		editorToolbar.setTargetComponent(this);
		actionToolbar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
		return editorToolbar;
	}

	private void createUIComponents() {
		from = new FromTextArea(model, this);
		fromPanel = from;
		fromPanel.setEnabled(!history);
		to = new ToTextArea(model, this);
		to.setEnabled(!history);
		toPanel = to;
	}

	public JPanel getRoot() {
		return root;
	}

	public ReplaceItemModel grepModel() {
		return model;
	}

	public void addListener(CompositeForm compositeForm) {
		this.compositeForm = compositeForm;
	}

	public void apply(ReplaceItemModel replaceItemModel) {
		if (compositeForm != null) {
			replaceItemModel.setEnabled(true);
			replaceItemModel.setExclusiveGroup(exclusiveGroup.getText());
			enabledCheckBox.setSelected(true);
			compositeForm.modelChanged(this);
		}

	}

	@Override
	public @Nullable
	Object getData(@NotNull @NonNls String s) {
		if (PANEL.is(s)) {
			return this;
		}
		return null;
	}
}

package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.components.JBPanel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.util.ui.JBEmptyBorder;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.utils.Cloner;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompositeForm extends JBPanel implements Disposable, DataProvider {

	public static final DataKey<CompositeForm> PANEL = DataKey.create("ReplacementCompositeForm");

	private static final Logger LOG = Logger.getInstance(CompositeForm.class);
	private JPanel rootComponent;
	JPanel expressions;
	JPanel items;
	JPanel buttons;
	private ReplaceCompositeModel compositeModel;
	private boolean history;

	public JPanel getRootComponent() {
		return rootComponent;
	}

	private void createUIComponents() {
	}

	public CompositeForm(ReplaceCompositeModel grepModel, boolean history) {
		this.history = history;
		expressions.add(items = new JPanel(), new GridConstraints(
				0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH, 0, 0,
				new Dimension(-1, -1),
				new Dimension(-1, -1),
				new Dimension(-1, -1),
				0, false
		));
		items.setLayout(new VerticalLayout(0));
		buttons.add(createToolbar().getComponent(), BorderLayout.NORTH);
		initModel(grepModel);
		setLayout(new BorderLayout());
		add(rootComponent, BorderLayout.CENTER);
	}


	@NotNull
	private ActionToolbar createToolbar() {
		final ActionManager actionManager = ActionManager.getInstance();

		DefaultActionGroup newGroup = new DefaultActionGroup();
		newGroup.add(actionManager.getAction("StringManipulation.ToolWindow.Replace"));

		ActionToolbar actionToolbar = actionManager.createActionToolbar("StringManipulation-ReplaceToolWindow-CompositeForm", newGroup, true);
		final ActionToolbarImpl editorToolbar = ((ActionToolbarImpl) actionToolbar);
		editorToolbar.setOpaque(false);
		editorToolbar.setBorder(new JBEmptyBorder(0, 0, 0, 0));
		editorToolbar.setTargetComponent(this);
		actionToolbar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
		return editorToolbar;
	}

	public void initModel(ReplaceCompositeModel compositeModel) {
		if (compositeModel == null) {
			return;
		}
		compositeModel = Cloner.deepClone(compositeModel);
		compositeModel.removeEmpty();
		items.removeAll();

		this.compositeModel = compositeModel;
		if (this.compositeModel.getItems().isEmpty()) {
			this.compositeModel.newItem();
		}

		boolean hasEmpty = false;
		List<ReplaceItemModel> models = this.compositeModel.getItems();
		for (int i = 0; i < models.size(); i++) {
			ReplaceItemModel model = models.get(i);
			if (history) {
				if (model.isEmpty()) {
					continue;
				}
			}
			addItemDetail(model);
			if (model.isEmpty()) {
				hasEmpty = true;
			}
		}

		if (!hasEmpty && !history) {
			addItemDetail(this.compositeModel.newItem());
		}
		items.revalidate();
		items.repaint();
	}


	public void addItemDetail(ReplaceItemModel model) {
		ItemForm comp = new ItemForm(model, history);
		comp.addListener(this);
		items.add(comp);
	}

	public ReplaceCompositeModel getModel() {
		return this.compositeModel;
	}


	@Override
	public void dispose() {
	}


	@Override
	public @Nullable
	Object getData(@NotNull @NonNls String s) {
		if (PANEL.is(s)) {
			return this;
		}
		return null;
	}


	public void modelChanged(ItemForm e) {
		Component[] components = items.getComponents();
		if (components.length > 0) {
			Component component = components[components.length - 1];
			if (component == e) {
				addItemDetail(this.compositeModel.newItem());
				items.revalidate();
				items.repaint();
			}
		}
	}

	public void remove(ItemForm c) {
		this.compositeModel.remove(c.grepModel());
		initModel(compositeModel);
	}

	public void moveUp(ItemForm data) {
		ReplaceItemModel model = data.getModel();
		compositeModel.moveUp(model);
		initModel(compositeModel);
	}

	public void moveDown(ItemForm data) {
		ReplaceItemModel model = data.getModel();
		compositeModel.moveDown(model);
		initModel(compositeModel);
	}

	public void addToHistory() {
		PluginPersistentStateComponent state = PluginPersistentStateComponent.getInstance();
		state.addToHistory(getModel());
		initModel(getModel());
	}
}

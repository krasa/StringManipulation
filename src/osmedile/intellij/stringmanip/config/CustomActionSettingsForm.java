package osmedile.intellij.stringmanip.config;

import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.ide.DataManager;
import com.intellij.ide.ui.customization.CustomActionsSchema;
import com.intellij.ide.ui.customization.CustomisedActionGroup;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ex.Settings;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.ui.components.labels.LinkListener;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.align.ColumnAligner;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.custom.CustomAction;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;
import osmedile.intellij.stringmanip.styles.custom.DefaultActions;
import osmedile.intellij.stringmanip.utils.Cloner;
import shaded.org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;

public class CustomActionSettingsForm implements Disposable {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CustomActionSettingsForm.class);

	private JBList actionsList;
	private CheckBoxList<CustomActionModel.Step> stepList;
	private JPanel root;
	private JTextField testField;
	private JButton testButton;
	private JButton add;
	private JButton remove;
	private JButton resetDefaultActions;
	private JTextField name;
	private JButton help;
	private JButton upStep;
	private JButton downStep;
	private JButton resetSteps;
	DefaultListModel model;
	JScrollPane scrollPane;
	CustomActionModel selectedItem;
	private EditorImpl myEditor;
	private JPanel myPreviewPanel;
	private LinkLabel link;
	private JPanel warningPanel;
	private LinkLabel link2;

	public CustomActionSettingsForm() {
		testField.setText("Foo Bar 1");

		updateWarningVisibility();
		LinkListener linkListener = new LinkListener() {
			@Override
			public void linkSelected(LinkLabel aSource, Object aLinkData) {

				Settings allSettings = Settings.KEY.getData(DataManager.getInstance().getDataContext(root));
				if (allSettings != null) {
					final Configurable configurable = allSettings.find("preferences.customizations");
					if (configurable != null) {
						allSettings.select(configurable);
					}
				}
			}
		};
		link.setListener(linkListener, null);
		link2.setListener(linkListener, null);
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s =
					"1) the current style of the text is detected" +
					"\n2) the text is then switched to the next enabled style" +
					"\n3) some <styles> cannot be enabled, they are here only for edge cases" +
					"\n\nFeel free to report any issues.";
				Messages.showInfoMessage(root, s, "How it works");
			}
		});
		initializeModel();
		final KeyAdapter keyAdapter = getDeleteKeyListener();
		actionsList.addKeyListener(keyAdapter);
		remove.addActionListener(deleteListener());
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomActionModel element = CustomActionModel.create();
				element.setName("New action");
				model.addElement(element);
				actionsList.setSelectedValue(element, true);
				updateWarningVisibility();
			}
		});
		upStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<CustomActionModel.Step> steps = selectedItem.getSteps();
				int selectedIndex = stepList.getSelectedIndex();
				if (selectedIndex > 0) {
					Collections.swap(steps, selectedIndex, selectedIndex - 1);
					initStepList();
					stepList.setSelectedIndex(selectedIndex - 1);
				}
			}
		});
		downStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<CustomActionModel.Step> steps = selectedItem.getSteps();
				int selectedIndex = stepList.getSelectedIndex();
				if (selectedIndex + 1 < steps.size()) {
					Collections.swap(steps, selectedIndex, selectedIndex + 1);
					initStepList();
					stepList.setSelectedIndex(selectedIndex + 1);
				}
			}
		});
		resetDefaultActions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PluginPersistentStateComponent.getInstance().resetDefaultActions();
				initializeModel();
			}
		});
		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField testField = CustomActionSettingsForm.this.testField;
				String text = testField.getText();
				if (StringUtils.isNotBlank(text)) {
					StringBuilder sb = new StringBuilder();
					for (CustomActionModel.Step step : selectedItem.getSteps()) {
						if (step.isEnabled()) {
							text = transform(text, sb);
						}
					}
					sb.append("\n");
					text = transform(text, sb);

					ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel();
					columnAlignerModel.setSequentialProcessing(false);
					columnAlignerModel.setAlignBy(ColumnAlignerModel.Align.SEPARATORS);
					columnAlignerModel.setSeparators(Arrays.asList("$$$"));
					String align = new ColumnAligner(columnAlignerModel).align(sb.toString());
					String finalAlign = align.replace("$$$", "");

					com.intellij.openapi.application.Application application = ApplicationManager.getApplication();
					application.runWriteAction(new Runnable() {
						@Override
						public void run() {
							myEditor.getDocument().setText(finalAlign);
							myPreviewPanel.validate();
							myPreviewPanel.repaint();
						}
					});
				}
			}

			protected String transform(String text, StringBuilder sb) {
				CustomAction customAction = new CustomAction(selectedItem);
				Style from = Style.from(text);

				String result = customAction.transformByLine(text);
				Style to = Style.from(result);

				sb.append(text).append(" $$$-> ").append(result).append(" $$$: (").append(from.getPresentableName()).append(" $$$-> ").append(to.getPresentableName()).append(")\n");
				return result;
			}
		});
		resetSteps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedItem != null) {
					selectedItem.setSteps(DefaultActions.getDefaultSteps());
				}
				initStepList();
			}
		});
		actionsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList sourceList = (JList) e.getSource();

				selectedItem = (CustomActionModel) sourceList.getSelectedValue();
				if (selectedItem != null) {
					PluginPersistentStateComponent.getInstance().setLastSelectedAction(actionsList.getSelectedIndex());
					name.setText(selectedItem.getName());
					initStepList();
				}
			}
		});
		name.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull final DocumentEvent e) {
				if (selectedItem != null) {
					selectedItem.setName(name.getText());
				}
			}
		});
	}

	protected void updateWarningVisibility() {
		warningPanel.setVisible(isGroupCustomized("StringManipulation.Group.Main") || isGroupCustomized("StringManipulation.Group.SwitchCase"));
	}

	private boolean isGroupCustomized(String id) {
		AnAction group1 = CustomActionsSchema.getInstance().getCorrectedAction(id);
		if (group1 instanceof CustomisedActionGroup) {
			CustomisedActionGroup customisedActionGroup = (CustomisedActionGroup) group1;
			AnAction[] children = customisedActionGroup.getChildren(null);
			AnAction[] originalChildren = customisedActionGroup.getOrigin().getChildren(null);
			return !Arrays.equals(children, originalChildren);
		}
		return false;
	}

	@NotNull
	private static Editor createEditorPreview() {
		EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
		ColorAndFontOptions options = new ColorAndFontOptions();
		options.reset();
		options.selectScheme(scheme.getName());
		return createPreviewEditor("", scheme, false);
	}

	static Editor createPreviewEditor(String text, EditorColorsScheme scheme, boolean editable) {
		EditorFactory editorFactory = EditorFactory.getInstance();
		Document editorDocument = editorFactory.createDocument(text);
		EditorEx editor = (EditorEx) (editable ? editorFactory.createEditor(editorDocument) : editorFactory.createViewer(editorDocument));
		editor.setColorsScheme(scheme);
		EditorSettings settings = editor.getSettings();
		settings.setLineNumbersShown(true);
		settings.setWhitespacesShown(false);
		settings.setLineMarkerAreaShown(false);
		settings.setIndentGuidesShown(false);
		settings.setFoldingOutlineShown(false);
		settings.setAdditionalColumnsCount(0);
		settings.setAdditionalLinesCount(0);
		settings.setRightMarginShown(false);

		return editor;
	}

	private ActionListener deleteListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		};
	}

	private KeyAdapter getDeleteKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 127) {
					delete();
				}
			}
		};
	}

	private void delete() {
		int selectedIndex = actionsList.getSelectedIndex();
		Object[] selectedValues = actionsList.getSelectedValues();
		for (Object goal : selectedValues) {
			model.removeElement(goal);
		}
		if (selectedIndex >= actionsList.getModel().getSize()) {
			actionsList.setSelectedIndex(selectedIndex - 1);
		} else {
			actionsList.setSelectedIndex(selectedIndex);
		}
	}

	public JPanel getRoot() {
		return root;
	}


	public boolean isModified() {
		return !getStyleActions().equals(PluginPersistentStateComponent.getInstance().getCustomActionModels());
	}

	public void setData() {
		initializeModel();
	}

	public void getData() {
		List<CustomActionModel> customActionModels = getStyleActions();

		PluginPersistentStateComponent.getInstance().setCustomActionModels(customActionModels);
	}

	@NotNull
	public List<CustomActionModel> getStyleActions() {
		List<CustomActionModel> customActionModels = new ArrayList<>();
		Enumeration elements = model.elements();
		while (elements.hasMoreElements()) {
			Object o = elements.nextElement();
			customActionModels.add((CustomActionModel) o);
		}
		return customActionModels;
	}


	private void createUIComponents() {
		model = new DefaultListModel();                                                   
		actionsList = createJBList(model);
		stepList = createStepList();

		myEditor = (EditorImpl) createEditorPreview();
		myPreviewPanel = (JPanel) myEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));

	}

	private CheckBoxList<CustomActionModel.Step> createStepList() {
		CheckBoxList<CustomActionModel.Step> list = new CheckBoxList<>();
		scrollPane = ScrollPaneFactory.createScrollPane(list);
		scrollPane.setMinimumSize(new Dimension(300, -1));
		list.setCheckBoxListListener((int index, boolean value) -> {
			CustomActionModel.Step itemAt = list.getItemAt(index);
			if (itemAt != null) {
				boolean b = !itemAt.getStyleAsEnum().name().startsWith("_");
				itemAt.setEnabled(value && b);
				if (value && !b) {
					list.setItemSelected(itemAt, false);
				}
			}
		});
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setDragEnabled(true);
		list.setDropMode(DropMode.INSERT);
		list.setTransferHandler(new MyListDropHandler(list) {
			@Override
			protected void swap(int index, int dropTargetIndex) {
				List<CustomActionModel.Step> steps = selectedItem.getSteps();
				if (index < dropTargetIndex) {//moving down
					Collections.rotate(steps.subList(index, dropTargetIndex), -1);
					--dropTargetIndex;
				} else {
//					dropTargetIndex--;
					Collections.rotate(steps.subList(dropTargetIndex, index + 1), 1);
				}
				initStepList();
				list.setSelectedIndex(dropTargetIndex);
			}
		});
		new MyDragListener(list);
		return list;
	}

	public void initStepList() {
		stepList.clear();

		for (CustomActionModel.Step style : selectedItem.getSteps()) {
			Style style1 = style.getStyleAsEnum();
			String presentableName = "<null>";
			if (style1 != null) {
				presentableName = style1.getPresentableName();
			}
			stepList.addItem(style, presentableName, style.isEnabled());
		}
	}


	private void initializeModel() {
		model.clear();
		PluginPersistentStateComponent stateComponent = PluginPersistentStateComponent.getInstance();
		List<CustomActionModel> customActionModels = stateComponent.getCustomActionModels();
		for (CustomActionModel customActionModel : customActionModels) {
			model.addElement(Cloner.deepClone(customActionModel));
		}
		if (model.size() > 0) {
			int lastSelectedAction = stateComponent.getLastSelectedAction();
			if (lastSelectedAction < 0 || lastSelectedAction > model.size()) {
				lastSelectedAction = 0;
			}
			actionsList.setSelectedIndex(lastSelectedAction);
		}
	}

	private JBList createJBList(DefaultListModel pluginsModel) {
		JBList jbList = new JBList(pluginsModel);
		jbList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
														  boolean cellHasFocus) {
				final Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				CustomActionModel goal = (CustomActionModel) value;
				setText(goal.getName());
				return comp;
			}
		});
		jbList.setDragEnabled(true);
		jbList.setDropMode(DropMode.INSERT);
		jbList.setTransferHandler(new MyListDropHandler(jbList));

		new MyDragListener(jbList);
		return jbList;
	}

	@Override
	public void dispose() {
		EditorFactory editorFactory = EditorFactory.getInstance();
		editorFactory.releaseEditor(myEditor);
	}
}

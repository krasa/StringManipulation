// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package osmedile.intellij.stringmanip.replace.gui.commonn;

import com.intellij.find.FindBundle;
import com.intellij.find.FindInProjectSettings;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionButtonLook;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider;
import com.intellij.openapi.actionSystem.ex.TooltipLinkProvider;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.actionSystem.impl.IdeaActionButtonLook;
import com.intellij.openapi.editor.EditorCopyPasteHelper;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.DumbAwareToggleAction;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.popup.PopupState;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Producer;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.replace.gui.ReplaceItemModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.awt.event.InputEvent.*;
import static javax.swing.ScrollPaneConstants.*;

public abstract class MySearchTextArea extends JPanel implements PropertyChangeListener {
	private static final JBColor BUTTON_SELECTED_BACKGROUND = JBColor.namedColor("SearchOption.selectedBackground", 0xDAE4ED, 0x5C6164);
	public static final String JUST_CLEARED_KEY = "JUST_CLEARED";
	public static final KeyStroke NEW_LINE_KEYSTROKE
			= KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, (SystemInfo.isMac ? META_DOWN_MASK : CTRL_DOWN_MASK) | SHIFT_DOWN_MASK);

	private static final ActionButtonLook FIELD_INPLACE_LOOK = new IdeaActionButtonLook() {
		@Override
		public void paintBorder(Graphics g, JComponent component, @ActionButtonComponent.ButtonState int state) {
			if (component.isFocusOwner() && component.isEnabled()) {
				Rectangle rect = new Rectangle(component.getSize());
				JBInsets.removeFrom(rect, component.getInsets());
				SYSTEM_LOOK.paintLookBorder(g, rect, JBUI.CurrentTheme.ActionButton.focusedBorder());
			} else {
				super.paintBorder(g, component, ActionButtonComponent.NORMAL);
			}
		}

		@Override
		public void paintBackground(Graphics g, JComponent component, int state) {
			if (((MyActionButton) component).isRolloverState()) {
				super.paintBackground(g, component, state);
				return;
			}
			if (state == ActionButtonComponent.SELECTED && component.isEnabled()) {
				Rectangle rect = new Rectangle(component.getSize());
				JBInsets.removeFrom(rect, component.getInsets());
				paintLookBackground(g, rect, BUTTON_SELECTED_BACKGROUND);
			}
		}
	};

	private final JTextArea myTextArea;
	private final boolean mySearchMode;
	private boolean disabled;
	private final JPanel myIconsPanel = new NonOpaquePanel();
	private final ActionButton myClearButton;
	private final NonOpaquePanel myExtraActionsPanel = new NonOpaquePanel();
	private final JBScrollPane myScrollPane;
	private final ActionButton myHistoryPopupButton;
	private boolean myMultilineEnabled = true;

	public MySearchTextArea(@NotNull JTextArea textArea, boolean searchMode, boolean disabled) {
		myTextArea = textArea;
		mySearchMode = searchMode;
		this.disabled = disabled;
		updateFont();
		myTextArea.addPropertyChangeListener("background", this);
		myTextArea.addPropertyChangeListener("font", this);
		DumbAwareAction.create(event -> myTextArea.transferFocus())
				.registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)), myTextArea);
		DumbAwareAction.create(event -> myTextArea.transferFocusBackward())
				.registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, SHIFT_DOWN_MASK)), myTextArea);
		KeymapUtil.reassignAction(myTextArea, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), NEW_LINE_KEYSTROKE, WHEN_FOCUSED);
		myTextArea.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getProperty("filterNewlines") == Boolean.TRUE && str.indexOf('\n') >= 0) {
					str = StringUtil.replace(str, "\n", " ");
				}
				if (!StringUtil.isEmpty(str)) super.insertString(offs, str, a);
			}
		});
		if (Registry.is("ide.find.field.trims.pasted.text", false)) {
			myTextArea.getDocument().putProperty(EditorCopyPasteHelper.TRIM_TEXT_ON_PASTE_KEY, Boolean.TRUE);
		}
		myTextArea.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull DocumentEvent e) {
				if (e.getType() == DocumentEvent.EventType.INSERT) {
					myTextArea.putClientProperty(JUST_CLEARED_KEY, null);
				}
				int rows = Math.min(Registry.get("ide.find.max.rows").asInteger(), myTextArea.getLineCount());
				myTextArea.setRows(Math.max(1, Math.min(25, rows)));
				updateIconsLayout();
				apply();
			}
		});
		myTextArea.setOpaque(false);
		myScrollPane = new JBScrollPane(myTextArea, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED) {
			@Override
			protected void setupCorners() {
				super.setupCorners();
				setBorder(JBUI.Borders.empty(2, 0, 2, 2));
			}

			@Override
			public void updateUI() {
				super.updateUI();
				setBorder(JBUI.Borders.empty(2, 0, 2, 2));
			}
		};
		myTextArea.setBorder(new Border() {
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			}

			@Override
			public Insets getBorderInsets(Component c) {
				if (SystemInfo.isMac) {
					return new JBInsets(3, 0, 2, 0);
				} else {
					int bottom = (StringUtil.getLineBreakCount(myTextArea.getText()) > 0) ? 2 : StartupUiUtil.isUnderDarcula() ? 1 : 0;
					int top = myTextArea.getFontMetrics(myTextArea.getFont()).getHeight() <= 16 ? 2 : 1;
					if (JBUIScale.isUsrHiDPI()) {
						bottom = 0;
						top = 2;
					}
					return new JBInsets(top, 0, bottom, 0);
				}
			}

			@Override
			public boolean isBorderOpaque() {
				return false;
			}
		});
		myScrollPane.getViewport().setBorder(null);
		myScrollPane.getViewport().setOpaque(false);
		myScrollPane.getHorizontalScrollBar().putClientProperty(JBScrollPane.IGNORE_SCROLLBAR_IN_INSETS, Boolean.TRUE);
		myScrollPane.setOpaque(false);

		myHistoryPopupButton = new MyActionButton(new ShowHistoryAction(), false);
		myClearButton = new MyActionButton(new ClearAction(), false);

		if (disabled) {
			myTextArea.setEnabled(false);
			myClearButton.setVisible(false);
			myHistoryPopupButton.setVisible(false);
		}
		updateLayout();
	}

	@Override
	public void updateUI() {
		super.updateUI();
		updateFont();
		setBackground(UIUtil.getTextFieldBackground());
	}

	private void updateFont() {
		if (myTextArea != null) {
			if (Registry.is("ide.find.use.editor.font", false)) {
				myTextArea.setFont(EditorUtil.getEditorFont());
			} else {
				myTextArea.setFont(UIManager.getFont("TextField.font"));
			}
		}
	}

	protected void updateLayout() {
		JPanel historyButtonWrapper = new NonOpaquePanel(new BorderLayout());
		historyButtonWrapper.setBorder(JBUI.Borders.empty(2, 3, 0, 0));
		historyButtonWrapper.add(myHistoryPopupButton, BorderLayout.NORTH);
		JPanel iconsPanelWrapper = new NonOpaquePanel(new BorderLayout());
		iconsPanelWrapper.setBorder(JBUI.Borders.emptyTop(2));
		JPanel p = new NonOpaquePanel(new BorderLayout());
		p.add(myIconsPanel, BorderLayout.NORTH);
		myIconsPanel.setBorder(JBUI.Borders.emptyRight(5));
		iconsPanelWrapper.add(p, BorderLayout.WEST);
		iconsPanelWrapper.add(myExtraActionsPanel, BorderLayout.CENTER);

		removeAll();
		setLayout(new BorderLayout(JBUIScale.scale(3), 0));
		setBorder(JBUI.Borders.empty(SystemInfo.isLinux ? JBUI.scale(2) : JBUI.scale(1)));
		add(historyButtonWrapper, BorderLayout.WEST);
		add(myScrollPane, BorderLayout.CENTER);
		add(iconsPanelWrapper, BorderLayout.EAST);
		updateIconsLayout();
	}

	private void updateIconsLayout() {
		if (myIconsPanel.getParent() == null) {
			return;
		}

//    boolean showClearIcon = !StringUtil.isEmpty(myTextArea.getText());
		boolean showClearIcon = true;
		boolean wrongVisibility =
				((myClearButton.getParent() == null) == showClearIcon);

		boolean multiline = StringUtil.getLineBreakCount(myTextArea.getText()) > 0;
		if (wrongVisibility) {
			myIconsPanel.removeAll();
			myIconsPanel.setLayout(new BorderLayout());
			myIconsPanel.add(myClearButton, BorderLayout.CENTER);
			myIconsPanel.setPreferredSize(myIconsPanel.getPreferredSize());
			if (!showClearIcon) myIconsPanel.remove(myClearButton);
			myIconsPanel.revalidate();
			myIconsPanel.repaint();
		}
		myScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		myScrollPane.setVerticalScrollBarPolicy(multiline ? VERTICAL_SCROLLBAR_AS_NEEDED : VERTICAL_SCROLLBAR_NEVER);
		myScrollPane.getHorizontalScrollBar().setVisible(multiline);
		myScrollPane.revalidate();
		doLayout();
	}

	public List<Component> setExtraActions(AnAction... actions) {
		myExtraActionsPanel.removeAll();
		myExtraActionsPanel.setBorder(JBUI.Borders.empty());
		ArrayList<Component> addedButtons = new ArrayList<>();
		if (actions != null && actions.length > 0) {
			JPanel buttonsGrid = new NonOpaquePanel(new GridLayout(1, actions.length, JBUI.scale(4), 0));
			for (AnAction action : actions) {
				if (action instanceof TooltipDescriptionProvider) {
					action.getTemplatePresentation().setDescription(FindBundle.message("find.embedded.buttons.description"));
				}
				ActionButton button = new MyActionButton(action, true);
				addedButtons.add(button);
				buttonsGrid.add(button);
			}
			buttonsGrid.setBorder(JBUI.Borders.emptyRight(2));
			myExtraActionsPanel.setLayout(new BorderLayout());
			myExtraActionsPanel.add(buttonsGrid, BorderLayout.NORTH);
			myExtraActionsPanel.setBorder(new CompoundBorder(JBUI.Borders.customLine(JBUI.CurrentTheme.CustomFrameDecorations.separatorForeground(), 0, 1, 0, 0), JBUI.Borders.emptyLeft(5)));
			myExtraActionsPanel.setBorder(new PseudoSeparatorBorder());
		}
		return addedButtons;
	}

	public void updateExtraActions() {
		for (ActionButton button : UIUtil.findComponentsOfType(myExtraActionsPanel, ActionButton.class)) {
			button.update();
		}
	}

	private final KeyAdapter myEnterRedispatcher = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER && MySearchTextArea.this.getParent() != null) {
				MySearchTextArea.this.getParent().dispatchEvent(e);
			}
		}
	};

	public void setMultilineEnabled(boolean enabled) {
		if (myMultilineEnabled == enabled) return;

		myMultilineEnabled = enabled;
		myTextArea.getDocument().putProperty("filterNewlines", myMultilineEnabled ? null : Boolean.TRUE);
		if (!myMultilineEnabled) {
			myTextArea.getInputMap().put(KeyStroke.getKeyStroke("shift UP"), "selection-begin-line");
			myTextArea.getInputMap().put(KeyStroke.getKeyStroke("shift DOWN"), "selection-end-line");
			myTextArea.addKeyListener(myEnterRedispatcher);
		} else {
			myTextArea.getInputMap().put(KeyStroke.getKeyStroke("shift UP"), "selection-up");
			myTextArea.getInputMap().put(KeyStroke.getKeyStroke("shift DOWN"), "selection-down");
			myTextArea.removeKeyListener(myEnterRedispatcher);
		}
		updateIconsLayout();
	}

	@NotNull
	public JTextArea getTextArea() {
		return myTextArea;
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("background".equals(evt.getPropertyName())) {
			repaint();
		}
		if ("font".equals(evt.getPropertyName())) {
			updateLayout();
		}
	}

	@Deprecated
	/**
	 * @deprecated use this wrapper component with JBTextArea and its getEmptyText() instead
	 */
	public void setInfoText(String info) {
	}

	protected abstract void apply();

	private class ShowHistoryAction extends DumbAwareAction {
		private final PopupState<JBPopup> myPopupState = PopupState.forPopup();

		ShowHistoryAction() {
			super(StringManipulationBundle.message("history"),
					StringManipulationBundle.message("history"),
					AllIcons.Actions.SearchWithHistory);
			registerCustomShortcutSet(KeymapUtil.getActiveKeymapShortcuts("ShowSearchHistory"), myTextArea);
		}

		@Override
		public void actionPerformed(@NotNull AnActionEvent e) {
			if (myPopupState.isRecentlyHidden()) return; // do not show new popup

			JBList<ReplaceItemModel> historyList = getHistoryList(e);
			showCompletionPopup(MySearchTextArea.this, historyList, null, myTextArea, null, myPopupState);
		}
	}

	@NotNull
	protected JBList<ReplaceItemModel> getHistoryList(AnActionEvent e) {
		FindInProjectSettings findInProjectSettings = FindInProjectSettings.getInstance(e.getProject());
		String[] recent = mySearchMode ? findInProjectSettings.getRecentFindStrings()
				: findInProjectSettings.getRecentReplaceStrings();
		String[] strings = ArrayUtil.reverseArray(recent);

		List<ReplaceItemModel> grepHistory = PluginPersistentStateComponent.getInstance().getReplaceModels();
		ReplaceItemModel[] array = grepHistory.toArray(ReplaceItemModel[]::new);
		ReplaceItemModel[] ts = ArrayUtil.reverseArray(array);

		ArrayList<ReplaceItemModel> replaceItemModels = new ArrayList<>();
		replaceItemModels.addAll(Arrays.asList(ts));
		for (String string : strings) {
			ReplaceItemModel replaceItemModel = new ReplaceItemModel();
			replaceItemModel.setFrom(string);
			replaceItemModel.setTo(string);
			replaceItemModels.add(replaceItemModel);
		}

		return new JBList<>(replaceItemModels);
	}

	public void showCompletionPopup(MySearchTextArea toolbarComponent,
									final JBList<ReplaceItemModel> list,
									@NlsContexts.PopupTitle String title,
									final JTextComponent textField,
									@NlsContexts.PopupAdvertisement String ad,
									@Nullable PopupState<JBPopup> popupState) {

		final Runnable callback = () -> {
			ReplaceItemModel selectedValue = list.getSelectedValue();
			if (selectedValue != null) {
				toolbarComponent.reload(selectedValue);
				IdeFocusManager.getGlobalInstance().requestFocus(textField, false);
			}
		};

		final PopupChooserBuilder builder = JBPopupFactory.getInstance().createListPopupBuilder(list);
		if (title != null) {
			builder.setTitle(title);
		}
		builder.setRenderer(new SimpleListCellRenderer<ReplaceItemModel>() {
			@Override
			public void customize(@NotNull JList<? extends ReplaceItemModel> jList, ReplaceItemModel replaceCompositeModel, int i, boolean b, boolean b1) {
				setText(MySearchTextArea.this.getText(replaceCompositeModel));
			}

		});
		final JBPopup popup = builder.setMovable(false).setResizable(false)
				.setRequestFocus(true).setItemChoosenCallback(callback).createPopup();

		if (ad != null) {
			popup.setAdText(ad, SwingConstants.LEFT);
		}

		if (popupState != null) popupState.prepareToShow(popup);
		if (toolbarComponent != null) {
			popup.showUnderneathOf(toolbarComponent);
		} else {
			popup.showUnderneathOf(textField);
		}
	}

	@NotNull
	protected abstract String getText(ReplaceItemModel replaceCompositeModel);

	protected abstract void reload(ReplaceItemModel selectedValue);

	private class ClearAction extends DumbAwareAction {
		ClearAction() {
			super(AllIcons.Actions.Close);
			getTemplatePresentation().setHoveredIcon(AllIcons.Actions.CloseHovered);
		}

		@Override
		public void actionPerformed(@NotNull AnActionEvent e) {
			myTextArea.putClientProperty(JUST_CLEARED_KEY, !myTextArea.getText().isEmpty());
			myTextArea.setText("");
//      MySearchTextArea mySearchTextArea = MySearchTextArea.this;
//      if (mySearchTextArea.getParent().getComponentCount() > 1) {
//        MySearchTextArea.this.getParent().remove(mySearchTextArea);
//      }
//      reload();
		}
	}

	private static final class MyActionButton extends ActionButton {

		private MyActionButton(@NotNull AnAction action, boolean focusable) {
			super(action, action.getTemplatePresentation().clone(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE);

			setLook(focusable ? FIELD_INPLACE_LOOK : ActionButtonLook.INPLACE_LOOK);
			setFocusable(focusable);
			updateIcon();
		}

		@Override
		protected DataContext getDataContext() {
			return DataManager.getInstance().getDataContext(this);
		}

		@Override
		public int getPopState() {
			return isSelected() ? SELECTED : super.getPopState();
		}

		boolean isRolloverState() {
			return super.isRollover();
		}

		@Override
		public Icon getIcon() {
			if (isEnabled() && isSelected()) {
				Icon selectedIcon = myPresentation.getSelectedIcon();
				if (selectedIcon != null) return selectedIcon;
			}
			return super.getIcon();
		}
	}

	private static class PseudoSeparatorBorder implements Border {
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(JBUI.CurrentTheme.CustomFrameDecorations.separatorForeground());
			g.fillRect(x + JBUI.scale(1), y + 1, 1, JBUI.scale(20));
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new JBInsets(0, 7, 0, 0);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}
	}

	public void updateControls() {
		updateExtraActions();
	}

	//
//	class ItemChangeListener implements ItemListener {
//		@Override
//		public void itemStateChanged(ItemEvent event) {
//			if (event.getStateChange() == ItemEvent.SELECTED) {
//				GrepOptionsItem item = (GrepOptionsItem) event.getItem();
//				updateGrepOptions(item);
//			}
//		}
//	}
//
//	protected void updateGrepOptions(GrepOptionsItem selectedItem) {
//		if (selectedItem != null) {
////			wholeLine.setSelected(selectedItem.isWholeLine());
////			regex.setSelected(selectedItem.isRegex());
////			matchCase.setSelected(selectedItem.isCaseSensitive());
//		}
//	}
	protected final class MySwitchStateToggleAction extends DumbAwareToggleAction implements TooltipLinkProvider, TooltipDescriptionProvider {
		private final AtomicBoolean myState;
		private final Producer<Boolean> myEnableStateProvider;
		private final TooltipLink myTooltipLink;
		private boolean disabled;

		public MySwitchStateToggleAction(@NotNull Icon icon, @NotNull Icon hoveredIcon, @NotNull Icon selectedIcon,
										 @NotNull AtomicBoolean state,
										 @NotNull Producer<Boolean> enableStateProvider, @Nls @NotNull String message1, boolean disabled) {
			this(icon, hoveredIcon, selectedIcon, state, enableStateProvider, null, message1, disabled);
		}

		private MySwitchStateToggleAction(@NotNull Icon icon, @NotNull Icon hoveredIcon, @NotNull Icon selectedIcon,
										  @NotNull AtomicBoolean state,
										  @NotNull Producer<Boolean> enableStateProvider,
										  @Nullable TooltipLink tooltipLink, @Nls @NotNull String message1, boolean disabled) {
			super(message1, null, icon);
			myState = state;
			myEnableStateProvider = enableStateProvider;
			myTooltipLink = tooltipLink;
			this.disabled = disabled;
			getTemplatePresentation().setHoveredIcon(hoveredIcon);
			getTemplatePresentation().setSelectedIcon(selectedIcon);
			ShortcutSet shortcut = ActionUtil.getMnemonicAsShortcut(this);
			if (shortcut != null) {
				setShortcutSet(shortcut);
				registerCustomShortcutSet(shortcut, MySearchTextArea.this);
			}
		}

		@Override
		public @Nullable
		TooltipLink getTooltipLink(@Nullable JComponent owner) {
			return myTooltipLink;
		}

		@Override
		public boolean isSelected(@NotNull AnActionEvent e) {
			return myState.get();
		}

		@Override
		public void update(@NotNull AnActionEvent e) {
			e.getPresentation().setEnabled(myEnableStateProvider.produce());
			Toggleable.setSelected(e.getPresentation(), myState.get());
		}

		@Override
		public @NotNull ActionUpdateThread getActionUpdateThread() {
			return ActionUpdateThread.EDT;
		}

		@Override
		public void setSelected(@NotNull AnActionEvent e, boolean selected) {
			if (disabled) {
				return;
			}
			myState.set(selected);
			updateControls();
			apply();
		}
	}
}

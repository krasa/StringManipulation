package osmedile.intellij.stringmanip.border;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CommonActionsPanel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.DialogUtils;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import osmedile.intellij.stringmanip.utils.PreviewDialog;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static osmedile.intellij.stringmanip.utils.ActionUtils.safeParse;

public class BorderDialog extends PreviewDialog {
	private static final Logger log = LoggerFactory.getLogger(BorderDialog.class);
	public static final int MAX_VALUE = 10;

	private CreateBorderAction action;
	private String sourceTextForPreview;
	private EditorImpl myPreviewEditor;


	public JComponent contentPane;
	private JPanel previewParent;
	private JPanel myPreviewPanel;
	private JButton donate;
	private JTextField padding;
	private JRadioButton borderDouble;
	private JTextField borderWidth;
	private JTextField customBorder;
	private JRadioButton borderCustom;
	private JRadioButton borderSingle;
	private JRadioButton fullBorder;
	private JPanel paddingPanel;
	private JPanel borderPanel;
	private JRadioButton topAndBottomBorder;
	private JRadioButton bottomBorder;
	private JRadioButton fullPadding;
	private JRadioButton sidePadding;
	private JRadioButton topAndBottomPadding;
	private int tabSize;
	private ActionToolbar borderActionToolbar;
	private ActionToolbar paddingActionToolbar;

	public BorderDialog(CreateBorderAction action, BorderSettings borderSettings, Editor editor) {
		super(editor);
		tabSize = EditorUtil.getTabSize(editor);
		Donate.initDonateButton(donate);
		init(borderSettings);
		this.action = action;
		sourceTextForPreview = PreviewDialog.getTextForPreview(editor);

		paddingPanel();
		borderPanel();

		DialogUtils.addListeners(this, this::updateComponents);

		updateComponents();
		borderActionToolbar.updateActionsImmediately();
		paddingActionToolbar.updateActionsImmediately();
		paddingPanel.revalidate();
		borderPanel.revalidate();
		paddingPanel.repaint();
		borderPanel.repaint();
	}

	private void updateComponents() {
		submitRenderPreview();
	}

	private void borderPanel() {
		AnActionButton plus = new AnActionButton("Increment", CommonActionsPanel.Buttons.UP.getIcon()) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
				int i;
				i = safeParse(borderWidth.getText(), 1);
				i++;
				if (i > MAX_VALUE) {
					i = MAX_VALUE;
				}
				borderWidth.setText(String.valueOf(i));
			}
		};
		plus.setContextComponent(getRoot());
		plus.addCustomUpdater(anActionEvent -> true);
//		plus.addCustomUpdater(anActionEvent -> safeParse(borderWidth.getText(), 1) < MAX_VALUE);

		AnActionButton minus = new AnActionButton("Decrement", CommonActionsPanel.Buttons.DOWN.getIcon()) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
				int i;
				i = safeParse(borderWidth.getText(), 1);
				i--;
				if (i <= 0) {
					i = 1;
				}
				borderWidth.setText(String.valueOf(i));
			}
		};
		minus.setContextComponent(getRoot());
		minus.addCustomUpdater(anActionEvent -> true);
//		minus.addCustomUpdater(anActionEvent -> safeParse(borderWidth.getText(), 1) > 1);

		DefaultActionGroup actionGroup = new DefaultActionGroup();
		borderActionToolbar = ActionManager.getInstance().createActionToolbar("StringManipulation-CreateBorder", actionGroup, true);
		actionGroup.addAction(minus);
		actionGroup.addAction(plus);
		borderActionToolbar.setTargetComponent(getRoot());
		borderActionToolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
		borderPanel.add(borderActionToolbar.getComponent(), BorderLayout.CENTER);
	}

	private void paddingPanel() {
		AnActionButton plus = new AnActionButton("Increment", CommonActionsPanel.Buttons.UP.getIcon()) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
				int i;
				i = safeParse(padding.getText(), 0);
				i++;
				if (i > MAX_VALUE) {
					i = MAX_VALUE;
				}
				padding.setText(String.valueOf(i));
			}

		};
		plus.setContextComponent(getRoot());
		plus.addCustomUpdater(anActionEvent -> true);
//		plus.addCustomUpdater(anActionEvent -> safeParse(padding.getText(), 1)  < MAX_VALUE);

		AnActionButton minus = new AnActionButton("Decrement", CommonActionsPanel.Buttons.DOWN.getIcon()) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
				int i;
				i = safeParse(padding.getText(), 0);
				i--;
				if (i < 0) {
					i = 0;
				}
				padding.setText(String.valueOf(i));
			}

		};
		minus.setContextComponent(getRoot());
		minus.addCustomUpdater(anActionEvent -> true);
//		minus.addCustomUpdater(anActionEvent -> safeParse(padding.getText(), 1)> 0);

		DefaultActionGroup actionGroup = new DefaultActionGroup();
		actionGroup.addAction(minus);
		actionGroup.addAction(plus);

		paddingActionToolbar = ActionManager.getInstance().createActionToolbar("StringManipulation-CreateBorder", actionGroup, true);
		paddingActionToolbar.setTargetComponent(getRoot());
		paddingActionToolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
		paddingPanel.add(paddingActionToolbar.getComponent(), BorderLayout.CENTER);
	}

	public BorderDialog() {
		super(null);
		previewParent.setVisible(false);
		donate.setVisible(false);
	}

	AtomicBoolean initting = new AtomicBoolean();

	protected void init(BorderSettings borderSettings) {
		initting.set(true);
		setData(borderSettings);
		initting.set(false);
	}


	@Override
	protected void renderPreviewAsync(Object input) {
		if (initting.get()) {
			return;
		}
		String previewText;
		try {
			previewText = action.transform(getSettings(), sourceTextForPreview, tabSize);
		} catch (Throwable e) {
			previewText = e.getMessage();
			log.warn(e.getMessage(), e);
		}
		setPreviewTextOnEDT(previewText);
	}

	@NotNull
	@Override
	public JComponent getPreferredFocusedComponent() {
		return borderDouble;
	}

	@NotNull
	@Override
	public JComponent getRoot() {
		return contentPane;
	}

	public BorderSettings getSettings() {
		BorderSettings borderSettings = new BorderSettings();
		getData(borderSettings);
		return borderSettings;
	}


	private void setPreviewTextOnEDT(String s) {
		ApplicationManager.getApplication().invokeLater(() -> setPreviewText(s), ModalityState.any());
	}

	private void setPreviewText(String previewText) {
		ApplicationManager.getApplication().runWriteAction(() -> {
			myPreviewEditor.getDocument().setText(previewText.replace("\r", ""));
			//remove all \r, which are not allowed in the Editor
			myPreviewPanel.validate();
			myPreviewPanel.repaint();
		});
	}


	private void createUIComponents() {
		myPreviewEditor = IdeUtils.createEditorPreview("", false, this);
		myPreviewPanel = (JPanel) myPreviewEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));
	}

	public void setData(BorderSettings data) {
		padding.setText(String.valueOf(data.getPaddingAsInt()));
		borderWidth.setText(String.valueOf(data.getBorderWidthAsInt()));

		customBorder.setText(data.getCustomBorder());
		borderCustom.setSelected(data.isBorderCustom());
		borderSingle.setSelected(data.isBorderSingle());
		borderDouble.setSelected(data.isBorderDouble());

		fullBorder.setSelected(data.isFullBorder());
		bottomBorder.setSelected(data.isBottomBorder());
		topAndBottomBorder.setSelected(data.isTopAndBottomBorder());

		fullPadding.setSelected(data.isFullPadding());
		sidePadding.setSelected(data.isSidePadding());
		topAndBottomPadding.setSelected(data.isTopAndBottomPadding());
	}

	public void getData(BorderSettings data) {
		data.setPadding(padding.getText());
		data.setBorderWidth(borderWidth.getText());
		data.setCustomBorder(customBorder.getText());
		data.setBorderCustom(borderCustom.isSelected());
		data.setBorderSingle(borderSingle.isSelected());
		data.setBorderDouble(borderDouble.isSelected());

		data.setBottomBorder(bottomBorder.isSelected());
		data.setFullBorder(fullBorder.isSelected());
		data.setTopAndBottomBorder(topAndBottomBorder.isSelected());

		data.setFullPadding(fullPadding.isSelected());
		data.setSidePadding(sidePadding.isSelected());
		data.setTopAndBottomPadding(topAndBottomPadding.isSelected());

	}

	public boolean isModified(BorderSettings data) {
		if (padding.getText() != null ? !padding.getText().equals(data.getPadding()) : data.getPadding() != null)
			return true;
		if (borderWidth.getText() != null ? !borderWidth.getText().equals(data.getBorderWidth()) : data.getBorderWidth() != null)
			return true;
		if (customBorder.getText() != null ? !customBorder.getText().equals(data.getCustomBorder()) : data.getCustomBorder() != null)
			return true;
		return false;
	}
}

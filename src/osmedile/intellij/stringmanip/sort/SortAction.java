package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.sort.support.SortLines;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.support.SortTypeDialog;

import javax.swing.*;
import java.util.List;

public class SortAction extends MyEditorAction {
	public static final String STORE_KEY = "StringManipulation.SortAction.SortSettings";
	private String storeKey = STORE_KEY;

	public SortAction(String storeKey) {
		this();
		this.storeKey = storeKey;
	}

	protected SortAction() {
		this(true);
	}

	protected SortAction(boolean setupHandler) {
		super(null);
		this.setupHandler(new MultiCaretHandlerHandler<SortSettings>(getActionClass()) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
				SortSettings settings = getSortSettings(editor);
				if (settings == null) return stopExecution();

				return continueExecution(settings);
			}

			@Override
			protected String processSingleSelection(String text, SortSettings settings) {
				return new SortLines(text, settings).sort();
			}

			@Override
			protected List<String> processMultiSelections(List<String> lines, SortSettings settings) {
				return new SortLines(lines, settings).sortLines();
			}

		});
	}

	@SuppressWarnings("Duplicates")
	@Nullable
	protected SortSettings getSortSettings(final Editor editor) {
		final SortTypeDialog dialog = new SortTypeDialog(getSortSettings(storeKey), true);
		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Sort Settings");
			}

			@Nullable
			@Override
			public JComponent getPreferredFocusedComponent() {
				return dialog.insensitive;
			}

			@Nullable
			@Override
			protected String getDimensionServiceKey() {
				return "StringManipulation.SortTypeDialog";
			}

			@Nullable
			@Override
			protected JComponent createCenterPanel() {
				return dialog.contentPane;
			}


			@Override
			protected void doOKAction() {
				super.doOKAction();
			}
		};

		boolean b = dialogWrapper.showAndGet();
		if (!b) {
			return null;
		}
		SortSettings settings = dialog.getSettings();
		PluginPersistentStateComponent.getInstance().setSortSettings(settings);
		return settings;
	}

	protected SortSettings getSortSettings(String storeKey) {
		return PluginPersistentStateComponent.getInstance().getSortSettings();	
	}

}

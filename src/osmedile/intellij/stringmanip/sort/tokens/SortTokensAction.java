package osmedile.intellij.stringmanip.sort.tokens;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.*;
import java.util.List;

public class SortTokensAction extends MyEditorAction {

	protected SortTokensAction() {
		this(true);
	}

	protected SortTokensAction(boolean setupHandler) {
		super(null);
		this.setupHandler(new MultiCaretHandlerHandler<SortTokensModel>(getActionClass()) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
				SortTokensModel settings = getSortSettings(editor);
				if (settings == null) return stopExecution();

				return continueExecution(settings);
			}

			@Override
			protected String processSingleSelection(String text, SortTokensModel settings) {
				return new SortTokens(text, settings).sortText();
			}

			@Override
			protected List<String> processMultiSelections(List<String> lines, SortTokensModel settings) {
				return new SortTokens(lines, settings).sortLines();
			}

		});
	}

	@SuppressWarnings("Duplicates")
	@Nullable
	protected SortTokensModel getSortSettings(final Editor editor) {
		final SortTokensGui dialog = new SortTokensGui(PluginPersistentStateComponent.getInstance().guessSortTokensModel(editor), editor);
		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Sort Tokens");
			}

			@Nullable
			@Override
			public JComponent getPreferredFocusedComponent() {
				return dialog.getPreferredFocusedComponent();
			}

			@Nullable
			@Override
			protected String getDimensionServiceKey() {
				return "StringManipulation.SortTokensDialog";
			}

			@Nullable
			@Override
			protected JComponent createCenterPanel() {
				return dialog.root;
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
		SortTokensModel settings = dialog.getModel();
		PluginPersistentStateComponent.getInstance().storeModel(settings);
		return settings;
	}
}

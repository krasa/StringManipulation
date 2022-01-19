package osmedile.intellij.stringmanip.sort;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.sort.support.SortLines;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.support.SortTypeDialog;
import osmedile.intellij.stringmanip.utils.Cloner;

import java.util.List;

public class SortAction extends MyEditorAction {
	public static final String STORE_KEY = "SortAction";
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
				SelectionModel selectionModel = editor.getSelectionModel();
				if (!selectionModel.hasSelection()) {
					selectionModel.setSelection(0, editor.getDocument().getTextLength());
				}
				SortSettings settings = getSortSettings(editor);
				if (settings == null) return stopExecution();

				return continueExecution(settings);
			}

			@Override
			protected String processSingleSelection(Editor editor, String text, SortSettings settings) {
				return new SortLines(editor.getProject(), text, settings).sort();
			}

			@Override
			protected List<String> processMultiSelections(Editor editor, List<String> lines, SortSettings settings) {
				return new SortLines(editor.getProject(), lines, settings).sortLines();
			}

		});
	}

	@SuppressWarnings("Duplicates")
	@Nullable
	protected SortSettings getSortSettings(final Editor editor) {
		SortSettings sortSettings = loadSortSettings();
		final SortTypeDialog dialog = new SortTypeDialog(sortSettings, true, editor);

		if (!dialog.showAndGet(editor.getProject(), StringManipulationBundle.message("sort.lines"), "StringManipulation.SortTypeDialog")) {
			return null;
		}
		SortSettings newSettings = dialog.getSettings();
		storeSortSettings(newSettings, storeKey);
		return newSettings;
	}

	protected void storeSortSettings(SortSettings newSettings, String storeKey) {
		PluginPersistentStateComponent.getInstance().storeSortSettings(storeKey, newSettings);
	}

	protected SortSettings loadSortSettings() {
		SortSettings sortSettings = PluginPersistentStateComponent.getInstance().getSortSettings(storeKey);
		return Cloner.deepClone(sortSettings);
	}

}

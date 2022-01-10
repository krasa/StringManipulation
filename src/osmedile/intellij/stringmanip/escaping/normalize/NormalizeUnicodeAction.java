package osmedile.intellij.stringmanip.escaping.normalize;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.utils.Cloner;

import java.util.List;

public class NormalizeUnicodeAction extends MyEditorAction {
	public static final String STORE_KEY = "StringManipulation.NormalizeUnicodeAction.NormalizationSettings";
	private String storeKey = STORE_KEY;

	public NormalizeUnicodeAction(String storeKey) {
		this();
		this.storeKey = storeKey;
	}

	protected NormalizeUnicodeAction() {
		this(true);
	}

	protected NormalizeUnicodeAction(boolean setupHandler) {
		super(null);
		this.setupHandler(new MultiCaretHandlerHandler<NormalizationSettings>(getActionClass()) {
			@NotNull
			@Override
			protected Pair beforeWriteAction(Editor editor, DataContext dataContext) {
				NormalizationSettings settings = getNormalizeSettings(editor);
				if (settings == null) return stopExecution();

				return continueExecution(settings);
			}

			@Override
			protected String processSingleSelection(Editor editor, String text, NormalizationSettings settings) {
				return Normalizator.normalizeText(text, settings);
			}

			@Override
			protected List<String> processMultiSelections(Editor editor, List<String> lines, NormalizationSettings settings) {
				return Normalizator.normalizeLines(lines, settings);
			}

		});
	}

	@SuppressWarnings("Duplicates")
	@Nullable
	protected NormalizationSettings getNormalizeSettings(final Editor editor) {
		final NormalizationDialog dialog = new NormalizationDialog(getNormalizeSettings(storeKey), editor);
		if (!dialog.showAndGet(editor.getProject(), "Unicode Normalization", "StringManipulation.NormalizationDialog")) {
			return null;
		}
		NormalizationSettings newSettings = dialog.getSettings();
		storeNormalizeSettings(newSettings);
		return newSettings;
	}

	protected void storeNormalizeSettings(NormalizationSettings newSettings) {
		PluginPersistentStateComponent.getInstance().setNormalizeSettings(newSettings);
	}

	protected NormalizationSettings getNormalizeSettings(String storeKey) {
		NormalizationSettings sortSettings = PluginPersistentStateComponent.getInstance().getNormalizeSettings();
		return Cloner.deepClone(sortSettings);
	}

}

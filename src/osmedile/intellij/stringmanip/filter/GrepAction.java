package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class GrepAction extends MyEditorAction {

	public static final String INITIAL_VALUE = "";

	public GrepAction() {
		super(null);
		setupHandler(new MyEditorWriteActionHandler<GrepSettings>(getActionClass()) {
			@NotNull
			@Override
			protected Pair<Boolean, GrepSettings> beforeWriteAction(Editor editor, DataContext dataContext) {
				VisualPosition visualPosition = null;
				String initialValue = INITIAL_VALUE;
				SelectionModel selectionModel = editor.getSelectionModel();
				if (!selectionModel.hasSelection()) {
					selectionModel.setSelection(0, editor.getDocument().getTextLength());
				} else {
					String selectedText = selectionModel.getSelectedText(true);
					if (selectedText != null && !selectedText.contains("\n")) {
						initialValue = selectedText;
						visualPosition = editor.getCaretModel().getVisualPosition();
						selectionModel.setSelection(0, editor.getDocument().getTextLength());
					}
				}
				GrepSettings settings = getSettings(editor, initialValue);
				if (settings == null) return stopExecution();

				settings.visualPosition = visualPosition;
				return continueExecution(settings);
			}


			@Override
			protected void executeWriteAction(Editor editor, DataContext dataContext, GrepSettings settings) {
				if (StringUtil.isEmptyOrSpaces(settings.getPattern())) {
					return;
				}
				List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
				IdeUtils.sort(caretsAndSelections);
				Collections.reverse(caretsAndSelections);

				for (CaretState caretsAndSelection : caretsAndSelections) {
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();

					int startOffset = editor.logicalPositionToOffset(selectionStart);
					int endOffset = editor.logicalPositionToOffset(selectionEnd);
					final String selectedText = editor.getDocument().getText(new TextRange(startOffset, endOffset));

					final String s = transform(settings, selectedText);
					editor.getDocument().replaceString(startOffset, endOffset, s);
					postProcess(editor, settings);
				}

			}
		});
	}

	protected void postProcess(Editor editor, GrepSettings grepSettings) {

	}

	protected GrepSettings getSettings(Editor editor, String initialValue) {
		GrepSettings settings = getSettings(initialValue);
		final GrepDialog dialog = new GrepDialog(this, settings, editor);

		if (!dialog.showAndGet(editor.getProject(), StringManipulationBundle.message("grep"), "StringManipulation.GrepDialog")) {
			return null;
		}
		GrepSettings newSettings = dialog.getSettings();
		storeGrepSettings(newSettings);
		return newSettings;

	}

	protected GrepSettings getSettings(String initialValue) {
		return PluginPersistentStateComponent.getInstance().guessSettings(initialValue);
	}

	protected void storeGrepSettings(GrepSettings newSettings) {
		PluginPersistentStateComponent.getInstance().addToHistory(newSettings);
	}

	protected String transform(GrepSettings settings, String selectedText) {
		MyGrepFilter grepFilter = new MyGrepFilter(settings);
		if (settings.isGroupMatching()) {
			String[] textParts = selectedText.split("\n");
			Collection<String> match = new ArrayList<String>();
			Collection<String> notMatch = new ArrayList<String>();

			for (String textPart : textParts) {
				if (grepFilter.execute(textPart)) {
					if (!StringUtils.isBlank(textPart)) {
						match.add(textPart);
					}
				} else {
					if (!StringUtils.isBlank(textPart)) {
						notMatch.add(textPart);
					}
				}
			}

			String matchS = StringUtils.join(match, '\n');

			StringBuilder sb = new StringBuilder(matchS);
			sb.append("\n");
			if (notMatch.size() > 0) {
				sb.append("\n");
				sb.append("\n");
				sb.append("\n");
				String notMatchS = StringUtils.join(notMatch, '\n');
				sb.append(notMatchS);
			}
			return sb.toString();
		} else {
			String[] textParts = selectedText.split("\n");
			Collection<String> result = new ArrayList<String>();

			for (String textPart : textParts) {
				if (grepFilter.execute(textPart)) {
					result.add(textPart);
				}
			}

			String[] res = result.toArray(new String[result.size()]);

			return StringUtils.join(res, '\n');
		}
	}


	private static class MyGrepFilter {

		private final GrepSettings settings;
		private Pattern compile;

		public MyGrepFilter(GrepSettings settings) {
			this.settings = settings;
			String pattern = this.settings.getPattern();

			if (settings.isRegex()) {
				if (settings.isFullWords()) {
					pattern = "\\b" + pattern + "\\b";
				}
			} else {
				if (settings.isFullWords()) {
					pattern = "\\b" + Pattern.quote(pattern) + "\\b";
				} else {
					pattern = Pattern.quote(pattern);
				}
			}

			if (settings.isCaseSensitive()) {
				compile = Pattern.compile(pattern);
			} else {
				compile = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			}
		}

		public boolean execute(String text) {
			boolean result = compile.matcher(text).find();
			if (settings.isInverted()) {
				result = !result;
			}
			return result;
		}

	}
}

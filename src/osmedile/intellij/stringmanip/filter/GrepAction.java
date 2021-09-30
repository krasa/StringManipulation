package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Olivier Smedile
 * @version $Id: GrepAction.java 60 2008-04-18 06:51:03Z osmedile $
 */
public class GrepAction extends MyEditorAction {

	protected static final GrepFilter GREP_FILTER = new GrepFilter() {
		@Override
		public boolean execute(String text, Pair<String, Boolean> options) {
			if (options.second) {
				return Pattern.compile(options.first).matcher(text).find();
			} else {
				return text.contains(options.first);
			}
		}
	};

	public GrepAction() {
		this(GREP_FILTER);
	}

	interface GrepFilter {
		boolean execute(String text, Pair<String, Boolean> options);
	}

	public GrepAction(final GrepFilter grepFilter) {
		super(null);
		setupHandler(new MyEditorWriteActionHandler<Pair<String, Boolean>>(getActionClass()) {
			@NotNull
			@Override
			protected Pair<Boolean, Pair<String, Boolean>> beforeWriteAction(Editor editor, DataContext dataContext) {
				String initialValue = "";
				SelectionModel selectionModel = editor.getSelectionModel();
				if (!selectionModel.hasSelection()) {
					selectionModel.setSelection(0, editor.getDocument().getTextLength());
				} else {
					String selectedText = selectionModel.getSelectedText(true);
					if (!selectedText.contains("\n")) {
						initialValue = selectedText;
						selectionModel.setSelection(0, editor.getDocument().getTextLength());
					}
				}
				InputDialogWithCheckbox dialog = new InputDialogWithCheckbox("", "Grep Text", "Regex", false, true, null, initialValue, new MyInputValidator() {
					@Nullable
					@Override
					public String getErrorText(String inputString) {
						if (isChecked()) {
							try {
								Pattern.compile(inputString);
								return null;
							} catch (Exception e) {
								return "Incorrect regular expression";
							}
						}

						return null;
					}

					@Override
					public boolean checkInput(String inputString) {
						return check(inputString);
					}

					@Override
					public boolean canClose(String inputString) {
						return check(inputString);
					}

					protected boolean check(String inputString) {
						if (isChecked()) {
							try {
								Pattern.compile(inputString);
								return true;
							} catch (Exception e) {
								return false;
							}
						} else {
							return !StringUtil.isEmptyOrSpaces(inputString);
						}
					}

				});

				dialog.show();
				Pair<String, Boolean> stringBooleanPair = Pair.create(dialog.getInputString(), dialog.isChecked());
				String grepos = stringBooleanPair.first;
				if (!StringUtil.isEmptyOrSpaces(grepos)) {
					return continueExecution(stringBooleanPair);
				}
				return stopExecution();
			}

			@Override
			protected void executeWriteAction(Editor editor, DataContext dataContext, Pair<String, Boolean> grepos) {
				if (StringUtil.isEmptyOrSpaces(grepos.first)) {
					return;
				}
				List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
				for (CaretState caretsAndSelection : caretsAndSelections) {
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();

					int startOffset = editor.logicalPositionToOffset(selectionStart);
					int endOffset = editor.logicalPositionToOffset(selectionEnd);
					final String selectedText = editor.getDocument().getText(new TextRange(startOffset, endOffset));

					final String s = transform(grepos, selectedText, grepFilter);
					editor.getDocument().replaceString(startOffset, endOffset, s);
				}

			}
		});
	}

	protected String transform(Pair<String, Boolean> grepos, String selectedText, GrepFilter grepFilter) {
		String[] textParts = selectedText.split("\n");
		Collection<String> result = new ArrayList<String>();

		for (String textPart : textParts) {
			if (grepFilter.execute(textPart, grepos)) {
				result.add(textPart);
			}
		}

		String[] res = result.toArray(new String[result.size()]);

		return StringUtils.join(res, '\n');
	}


	class InputDialogWithCheckbox extends Messages.InputDialog {
		private JCheckBox myCheckBox;

		InputDialogWithCheckbox(String message,
								@Nls(capitalization = Nls.Capitalization.Title) String title,
								String checkboxText,
								boolean checked,
								boolean checkboxEnabled,
								@Nullable Icon icon,
								@Nullable String initialValue,
								@Nullable MyInputValidator validator) {
			super(message, title, icon, initialValue, validator);
			myCheckBox.setText(checkboxText);
			myCheckBox.setSelected(checked);
			myCheckBox.setEnabled(checkboxEnabled);
			if (validator != null) {
				validator.setCheckBox(myCheckBox);
			}
		}

		@NotNull
		@Override
		protected JPanel createMessagePanel() {
			JPanel messagePanel = new JPanel(new BorderLayout());
			if (myMessage != null) {
				JComponent textComponent = createTextComponent();
				messagePanel.add(textComponent, BorderLayout.NORTH);
			}

			myField = createTextFieldComponent();
			messagePanel.add(createScrollableTextComponent(), BorderLayout.CENTER);

			myCheckBox = new JCheckBox();
			messagePanel.add(myCheckBox, BorderLayout.SOUTH);

			return messagePanel;
		}

		public Boolean isChecked() {
			return myCheckBox.isSelected();
		}
	}

	abstract class MyInputValidator implements InputValidatorEx {
		private JCheckBox myCheckBox;

		public void setCheckBox(JCheckBox myCheckBox) {
			this.myCheckBox = myCheckBox;
		}

		public boolean isChecked() {
			return myCheckBox != null && myCheckBox.isSelected();
		}
	}
}

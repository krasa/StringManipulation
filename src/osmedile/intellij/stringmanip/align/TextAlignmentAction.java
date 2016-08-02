package osmedile.intellij.stringmanip.align;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

import osmedile.intellij.stringmanip.utils.StringUtils;

public abstract class TextAlignmentAction extends EditorAction {

	protected TextAlignmentAction(Alignment alignment) {
		this(true, alignment);
	}

	protected TextAlignmentAction(boolean setupHandler, final Alignment alignment) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {

				@Override
				public void executeWriteAction(Editor editor, DataContext dataContext) {
					List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();

					if (caretsAndSelections.size() > 1) {
						processMultiCaret(editor, caretsAndSelections);
					} else if (caretsAndSelections.size() == 1) {
						processSingleSelection(editor, caretsAndSelections);
					}
				}

				private void processSingleSelection(Editor editor, List<CaretState> caretsAndSelections) {
					CaretState caretsAndSelection = caretsAndSelections.get(0);
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
					String text = editor.getDocument().getText(
							new TextRange(editor.logicalPositionToOffset(selectionStart),
									editor.logicalPositionToOffset(selectionEnd)));

					String charSequence = alignment.alignLines(text);

					editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd), charSequence);
				}

				private void processMultiCaret(Editor editor, List<CaretState> caretsAndSelections) {
					List<String> lines = new ArrayList<String>();
					for (CaretState caretsAndSelection : caretsAndSelections) {
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						String text = editor.getDocument().getText(
								new TextRange(editor.logicalPositionToOffset(selectionStart),
										editor.logicalPositionToOffset(selectionEnd)));
						lines.add(text);
					}

					lines = alignLines(lines, alignment);
					for (int i = lines.size() - 1; i >= 0; i--) {
						String line = lines.get(i);
						CaretState caretsAndSelection = caretsAndSelections.get(i);
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
								editor.logicalPositionToOffset(selectionEnd), line);
					}
				}
			});
		}
	}

	private List<String> alignLines(List<String> lines, Alignment alignment) {
		ArrayList<String> strings = new ArrayList<String>();
		for (String line : lines) {
			strings.add(alignment.alignLines(line));
		}
		return strings;
	}

	public enum Alignment {
		LEFT() {
			@Override
			@NotNull
			protected String align(String s) {
				return ltrim(s);
			}
		},
		RIGHT {
			@Override
			@NotNull
			protected String align(String s) {
				return rtrim(s);
			}
		},
		CENTER {
			@Override
			@NotNull
			protected String align(String s) {
				return center(s);
			}

		};

		public String alignLines(String text) {
			String[] split = text.split("\n");
			for (int i = 0; i < split.length; i++) {
				String s = split[i];
				split[i] = align(s);
			}
			String join = StringUtils.join(split, '\n');
			if (text.endsWith("\n")) {
				join = join + "\n";
			}
			return join;
		}

		protected abstract String align(String s);

	}

	private static String ltrim(String s) {
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i) + s.substring(0, i);
	}

	private static String rtrim(String s) {
		int i = s.length() - 1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}
		return s.substring(i + 1, s.length()) + s.substring(0, i + 1);
	}

	private static String center(String s) {
		return org.apache.commons.lang.StringUtils.center(s.trim(), s.length());
	}
}

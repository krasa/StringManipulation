package osmedile.intellij.stringmanip;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;

public class AlignToColumnsAction extends EditorAction {
	private String lastSeparator = ",";

	protected AlignToColumnsAction() {
		this(true);
	}

	protected AlignToColumnsAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
			this.setupHandler(new EditorWriteActionHandler(false) {

				public void executeWriteAction(Editor editor, DataContext dataContext) {
					String separator = chooseSeparator();
					if (separator == null)
						return;

					List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();

					if (caretsAndSelections.size() > 1) {
						processMultiCaret(editor, separator, caretsAndSelections);
					} else if (caretsAndSelections.size() == 1) {
						processSingleSelection(editor, separator, caretsAndSelections);
					}
				}

				private String chooseSeparator() {
					String separator = Messages.showInputDialog("Separator", "Separator", Messages.getQuestionIcon(),
							lastSeparator, null);
					if (separator != null) {
						if (separator.equals("")) {
							separator = " ";
						}
						lastSeparator = separator;
					} else {
						return null;
					}
					return separator;
				}

				private void processSingleSelection(Editor editor, String separator,
						List<CaretState> caretsAndSelections) {
					CaretState caretsAndSelection = caretsAndSelections.get(0);
					LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
					LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
					String text = editor.getDocument().getText(
							new TextRange(editor.logicalPositionToOffset(selectionStart),
									editor.logicalPositionToOffset(selectionEnd)));

					String charSequence = reformat(separator, text);
					editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd), charSequence);
				}

				private void processMultiCaret(Editor editor, String separator, List<CaretState> caretsAndSelections) {
					List<Line> lines = new ArrayList<Line>();
					for (CaretState caretsAndSelection : caretsAndSelections) {
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						String text = editor.getDocument().getText(
								new TextRange(editor.logicalPositionToOffset(selectionStart),
										editor.logicalPositionToOffset(selectionEnd)));
						lines.add(new Line(separator, text, text.endsWith("\n")));
					}

					process(lines);
					for (int i = lines.size() - 1; i >= 0; i--) {
						Line line = lines.get(i);
						CaretState caretsAndSelection = caretsAndSelections.get(i);
						LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
						LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
						editor.getDocument().replaceString(editor.logicalPositionToOffset(selectionStart),
								editor.logicalPositionToOffset(selectionEnd), line.sb.toString().trim());
					}
				}
			});
		}

	}

	protected String reformat(String separator, String text) {
		List<Line> lines = toLines(separator, text);
		process(lines);
		return toString(lines);
	}

	private String toString(List<Line> lines) {
		StringBuilder sb = new StringBuilder();
		for (Line s : lines) {
			sb.append(s.sb.toString());
			if (s.endsWithNextLine) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	private List<Line> toLines(String separator, String text) {
		List<Line> lines = new ArrayList<Line>();
		String[] split = text.split("\n");
		boolean lastTokenEndsWithNewLine = text.endsWith("\n");
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			boolean last = i == split.length - 1;
			lines.add(new Line(separator, s, last ? lastTokenEndsWithNewLine : true));
		}
		return lines;
	}

	private void process(List<Line> lines) {
		int initialSeparatorPosition = initialSeparatorPosition(lines);
		for (Line line : lines) {
			line.appendInitialSpace(initialSeparatorPosition);
		}

		boolean process = true;
		while (process) {
			process = false;

			int maxLength = 0;
			for (Line line : lines) {
				line.appendText();
				maxLength = Math.max(maxLength, line.resultLength());
			}
			for (Line line : lines) {
				line.appendSpace(maxLength);
			}
			for (Line line : lines) {
				line.appendSeparator();
			}

			for (Line line : lines) {
				line.next();
			}

			for (Line line : lines) {
				process = process || line.hasToken();
			}
		}
	}

	private int initialSeparatorPosition(List<Line> lines) {
		int i = 0;
		for (Line line : lines) {
			i = Math.max(i, line.currentTokenLength());
		}
		return i;
	}

	@SuppressWarnings("Duplicates")
	class Line {

		private final String[] split;
		protected boolean endsWithNextLine;
		private StringBuilder sb = new StringBuilder();
		private int index = 0;
		private boolean hasSeparatorBeforeFirstToken = false;
		private String separator;

		public Line(String separator, String textPart, boolean endsWithNextLine) {
			this.endsWithNextLine = endsWithNextLine;
			this.separator = separator;
			if (separator.equals(" ")) {
				split = StringUtils.splitByWholeSeparator(textPart, separator);
			} else {
				split = StringUtils.splitByWholeSeparatorPreserveAllTokens(textPart, separator);
			}
			hasSeparatorBeforeFirstToken = split.length>0 && split[0].length()==0;
		}

		public void appendInitialSpace(int initialSeparatorPosition) {
			if (hasToken() &&hasSeparatorBeforeFirstToken) {
				int initialSpaces = initialSeparatorPosition - 1; // -1 for empty space which is around separator
				for (int j = 0; j < initialSpaces; j++) {
					sb.append(" ");
				}
			}
		}

		public void appendText() {
			if (hasToken()) {
				sb.append(split[index].trim());
			}
		}

		public void appendSpace(int maxLength) {
			if (hasNextToken()) {
				int appendSpaces = Math.max(0, maxLength - sb.length());
				for (int j = 0; j < appendSpaces; j++) {
					sb.append(" ");
				}
			}
		}

		public void appendSeparator() {
			if (hasNextToken()) {
				if (!separator.equals(" ") && sb.length() > 0) {
					sb.append(" ");
				}
				sb.append(separator);
				if (!separator.equals(" ") && hasNextNotEmptyToken()) {
					sb.append(" ");
				}
			}
		}

		public int resultLength() {
			return sb.length();
		}

		public boolean hasToken() {
			return index < split.length;
		}

		public boolean hasNextToken() {
			return hasToken() && index + 1 < split.length;
		}

		public boolean hasNextNotEmptyToken() {
			return hasToken() && index + 1 < split.length && split[index + 1].length() > 0;
		}

		public void next() {
			index++;
		}

		public int currentTokenLength() {
			int result = -1;
			if (hasToken()) {
				result = split[index].length();
			}
			return result;
		}

		@Override
		public String toString() {
			return sb.toString();
		}
	}

}

package osmedile.intellij.stringmanip.filter;

public class SubSelectionLine {
	public final String line;
	public final String selection;
	public final int lineStartOffset;
	public final int lineEndOffset;
	public final int selectionStartLineOffset;
	public final int selectionEndLineOffset;

	public boolean remove = false;

	public SubSelectionLine(String line, String selection, int lineStartOffset, int lineEndOffset,
							int selectionStartLineOffset, int selectionEndLineOffset) {
		this.line = line;
		this.selection = selection;
		this.lineStartOffset = lineStartOffset;
		this.lineEndOffset = lineEndOffset;
		this.selectionStartLineOffset = selectionStartLineOffset;
		this.selectionEndLineOffset = selectionEndLineOffset;
	}
}
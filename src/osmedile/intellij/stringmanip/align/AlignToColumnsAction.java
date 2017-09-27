package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static osmedile.intellij.stringmanip.utils.StringUtils.isEmpty;

public class AlignToColumnsAction extends EditorAction {

    private List<String> lastSeparators;

	protected AlignToColumnsAction() {
		this(true);
	}

	protected AlignToColumnsAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
            this.setupHandler(new MultiCaretHandlerHandler<List<String>>() {

				@NotNull
				@Override
                protected Pair<Boolean, List<String>> beforeWriteAction(Editor editor, DataContext dataContext) {

                    final TextAlignmentForm textAlignmentForm = new TextAlignmentForm(lastSeparators);
                    DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
                        {
                            init();
                            setTitle("Separators");
                        }

                        @Nullable
                        @Override
                        public JComponent getPreferredFocusedComponent() {
                            return textAlignmentForm.getPreferredFocusedComponent();
                        }

                        @Nullable
                        @Override
                        protected String getDimensionServiceKey() {
                            return "StringManipulation.TextAlignmentForm";
                        }


                        @Nullable
                        @Override
                        protected JComponent createCenterPanel() {
                            return textAlignmentForm.root;
                        }

                        @Override
                        protected void doOKAction() {
                            super.doOKAction();
                        }
                    };

                    if (!dialogWrapper.showAndGet()) {
						return stopExecution();
					}

                    List<String> separators = textAlignmentForm.getSeparators();
                    lastSeparators = separators;
                    return continueExecution(separators);
				}

				@Override
                protected String processSingleSelection(String text, List<String> separators) {
                    String reformat = text;
                    for (String separator : separators) {
                        if (isEmpty(separator)) {
                            continue;
                        }
                        reformat = new ColumnAligner().reformat(separator, reformat);
                    }
                    return reformat;
				}

				@Override
                protected List<String> processMultiSelections(List<String> lines, List<String> separators) {
                    for (String separator : separators) {
                        if (isEmpty(separator)) {
                            continue;
                        }
                        List<ColumnAlignerLine> columnAlignerLines = new ArrayList<ColumnAlignerLine>();
                        for (String line : lines) {
                            columnAlignerLines.add(new ColumnAlignerLine(separator, line, line.endsWith("\n")));
                        }
                        lines = new ColumnAligner().process(columnAlignerLines);
					}
                    return lines;
				}
			});
		}

	}


}

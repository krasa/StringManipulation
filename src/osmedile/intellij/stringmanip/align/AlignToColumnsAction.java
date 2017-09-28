package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MultiCaretHandlerHandler;
import osmedile.intellij.stringmanip.PluginPersistentStateComponent;

import javax.swing.*;
import java.util.List;

public class AlignToColumnsAction extends EditorAction {


	protected AlignToColumnsAction() {
		this(true);
	}

	protected AlignToColumnsAction(boolean setupHandler) {
		super(null);
		if (setupHandler) {
            this.setupHandler(new MultiCaretHandlerHandler<ColumnAlignerModel>() {

				@NotNull
				@Override
                protected Pair<Boolean, ColumnAlignerModel> beforeWriteAction(Editor editor, DataContext dataContext) {
                    PluginPersistentStateComponent stateComponent = PluginPersistentStateComponent.getInstance();
                    final TextAlignmentForm textAlignmentForm = new TextAlignmentForm(stateComponent.getColumnAlignerModel());
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

                    ColumnAlignerModel model = textAlignmentForm.getModel();
                    stateComponent.setColumnAlignerModel(textAlignmentForm.getModel());
                    return continueExecution(model);
				}

				@Override
                protected String processSingleSelection(String text, ColumnAlignerModel model) {
                    return new ColumnAligner(model).align(text);
				}

                @Override
                protected List<String> processMultiSelections(List<String> lines, ColumnAlignerModel model) {
                    return new ColumnAligner(model).align(lines);
				}

            });
		}

	}


}

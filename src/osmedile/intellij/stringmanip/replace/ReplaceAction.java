/*
 * Copyright 2000-2017 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
package osmedile.intellij.stringmanip.replace;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.MyApplicationService;
import osmedile.intellij.stringmanip.MyEditorAction;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.replace.gui.CompositeForm;
import osmedile.intellij.stringmanip.replace.gui.ReplaceCompositeModel;
import osmedile.intellij.stringmanip.toolwindow.StringManipulationToolWindowFactory;
import osmedile.intellij.stringmanip.toolwindow.ToolWindowPanel;

public class ReplaceAction extends MyEditorAction {

	protected ReplaceAction() {
		super(new ReplaceAction.Handler());
	}

	private static class Handler extends EditorWriteActionHandler {
		Handler() {
			super(true);
		}

		@Override
		public void executeWriteAction(@NotNull Editor editor, Caret caret, DataContext dataContext) {
			MyApplicationService.setAction(ReplaceAction.class);
			Project project = editor.getProject();
			if (project == null) {
				return;
			}

			ToolWindowPanel toolWindowPanel = StringManipulationToolWindowFactory.getToolWindowPanel(project);
			ReplaceCompositeModel model = null;
			if (toolWindowPanel != null) {
				CompositeForm compositeForm = toolWindowPanel.getReplacementCompositeForm();
				if (compositeForm != null) {
					model = compositeForm.getModel();
				}
			}
			if (model != null && model.isAnyEnabledAndValid()) {
				PluginPersistentStateComponent.getInstance().addToHistory(model);
			}
			if (model == null) {
				model = PluginPersistentStateComponent.getInstance().getLastReplaceModel();
			}

			if (model == null || !model.isAnyEnabledAndValid()) {
				StringManipulationToolWindowFactory.showToolWindow(project);
			} else {
				replace(editor, model);
			}

		}

		@Override
		public boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
			return !editor.isOneLineMode() || editor.getSelectionModel().hasSelection();
		}
	}

	public static void replace(Editor editor, ReplaceCompositeModel model) {
		Document document = editor.getDocument();
		CaretModel caretModel = editor.getCaretModel();
//		ScrollingModel scrollingModel = editor.getScrollingModel();
		if (editor.getSelectionModel().hasSelection()) {
			int start = editor.getSelectionModel().getSelectionStart();
			int end = editor.getSelectionModel().getSelectionEnd();
			String s = document.getCharsSequence().subSequence(start, end).toString();
			s = replace(editor.getProject(), s, model);
			document.replaceString(start, end, s);
//			caretModel.moveToOffset(end + s.length());
//			scrollingModel.scrollToCaret(ScrollType.RELATIVE);
//			editor.getSelectionModel().removeSelection();
//			editor.getSelectionModel().setSelection(end, end + s.length());
		} else {
			replaceLinesRange(editor, document, caretModel.getVisualPosition(), caretModel.getVisualPosition(), model);
		}

	}

	private static String replace(Project project, String s, ReplaceCompositeModel compositeModel) {
		if (project == null) {
			return s;
		}
		return compositeModel.replace(project, s);
	}

	@Nullable
	static void replaceLinesRange(Editor editor, Document document, VisualPosition rangeStart, VisualPosition rangeEnd, ReplaceCompositeModel model) {
		Pair<LogicalPosition, LogicalPosition> lines = EditorUtil.calcSurroundingRange(editor, rangeStart, rangeEnd);

		LogicalPosition lineStart = lines.first;
		LogicalPosition nextLineStart = lines.second;
		int start = editor.logicalPositionToOffset(lineStart);
		int end = editor.logicalPositionToOffset(nextLineStart);
		if (end <= start) {
			return;
		}
		String s = document.getCharsSequence().subSequence(start, end).toString();
		s = replace(editor.getProject(), s, model);
		if (nextLineStart.line == document.getLineCount() - 1 && nextLineStart.column > 0) { // last line
			s = "\n" + s;
		}

		document.replaceString(start, end, s);

		editor.getSelectionModel().setSelection(start, start + s.length());
//		editor.getCaretModel().moveToOffset(newOffset);
//		editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);

	}

//	@Override
//	public void update(final Editor editor, final Presentation presentation, final DataContext dataContext) {
//		super.update(editor, presentation, dataContext);
//		if (editor.getSelectionModel().hasSelection()) {
//			presentation.setText(EditorBundle.message("action.duplicate.selection"), true);
//		} else {
//			presentation.setText(EditorBundle.message("action.duplicate.line"), true);
//		}
//	}
}

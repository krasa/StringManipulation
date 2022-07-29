package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.textarea.TextComponentEditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SpeedSearchBase;
import com.intellij.ui.speedSearch.SpeedSearchSupply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * use primarily {@link  osmedile.intellij.stringmanip.AbstractStringManipAction}
 */
public abstract class MyEditorAction extends EditorAction {


	protected MyEditorAction(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}

	protected Class getActionClass() {
		return getClass();
	}

	///from TextComponentEditorAction
	@Override
	@Nullable
	protected Editor getEditor(@NotNull final DataContext dataContext) {
		return getEditorFromContext(dataContext);
	}

	@Nullable
	private static Editor getEditorFromContext(@NotNull DataContext dataContext) {
		final Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
		if (editor != null) return editor;
		final Project project = CommonDataKeys.PROJECT.getData(dataContext);
		final Object data = PlatformCoreDataKeys.CONTEXT_COMPONENT.getData(dataContext);
		if (data instanceof EditorComponentImpl) {
			// can happen if editor is already disposed, or if it's in renderer mode
			return null;
		}
		if (data instanceof JTextComponent) {
			return new TextComponentEditorImpl(project, (JTextComponent) data);
		}
		if (data instanceof JComponent) {
			final JTextField field = findActiveSpeedSearchTextField((JComponent) data);
			if (field != null) {
				return new TextComponentEditorImpl(project, field);
			}
		}
		return null;
	}

	private static JTextField findActiveSpeedSearchTextField(JComponent c) {
		final SpeedSearchSupply supply = SpeedSearchSupply.getSupply(c);
		if (supply instanceof SpeedSearchBase) {
			return ((SpeedSearchBase) supply).getSearchField();
		}
		if (c instanceof DataProvider) {
			final Object component = PlatformDataKeys.SPEED_SEARCH_COMPONENT.getData((DataProvider) c);
			if (component instanceof JTextField) {
				return (JTextField) component;
			}
		}
		return null;
	}


	@Override
	public final @NotNull ActionUpdateThread getActionUpdateThread() {
		//https://github.com/krasa/StringManipulation/issues/182
		//Access is allowed from event dispatch thread only exception is thrown from MyEditorAction.findActiveSpeedSearchTextField in IntelliJ 2022.3
		return ActionUpdateThread.EDT;
	}
}

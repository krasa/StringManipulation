package com.github.krasa.stringmanipulation.intellij;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

public abstract class MyEditorAction extends EditorAction {


	protected MyEditorAction(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}

	protected Class getActionClass() {
		return getClass();
	}

}

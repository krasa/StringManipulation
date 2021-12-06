/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package osmedile.intellij.stringmanip.actions;

import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.find.FindResult;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import com.intellij.openapi.editor.actions.SelectOccurrencesActionHandler;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static com.intellij.openapi.editor.actions.IncrementalFindAction.SEARCH_DISABLED;

public class MySelectAllOccurrencesAction extends EditorAction {
	protected MySelectAllOccurrencesAction() {
		super(new Handler());
	}

	private static class Handler extends SelectOccurrencesActionHandler {
		@Override
		public boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
			return editor.getProject() != null
					&& editor.getCaretModel().supportsMultipleCarets()
					&& !SEARCH_DISABLED.get(editor, false);
		}

		@Override
		public void doExecute(@NotNull final Editor editor, @Nullable Caret c, DataContext dataContext) {
			HashSet<Pair<String, Boolean>> selectedTexts = new HashSet<>();
			List<MyModel> models = new ArrayList<>();

			List<Caret> allCarets = editor.getCaretModel().getAllCarets();
			for (Caret caret : allCarets) {
				boolean wholeWordsSearch = false;
				if (!caret.hasSelection()) {
					TextRange wordSelectionRange = getSelectionRange(editor, caret);
					if (wordSelectionRange != null) {
						setSelection(editor, caret, wordSelectionRange);
						wholeWordsSearch = true;
					}
				}

				String selectedText = caret.getSelectedText();

				Project project = editor.getProject();
				if (project == null || selectedText == null) {
					continue;
				}

				Pair<String, Boolean> pair = Pair.create(selectedText, wholeWordsSearch);
				if (selectedTexts.contains(pair)) {
					continue;
				} else {
					selectedTexts.add(pair);
				}

				int caretShiftFromSelectionStart = caret.getOffset() - caret.getSelectionStart();
				final FindManager findManager = FindManager.getInstance(project);

				final FindModel model = getFindModel(selectedText, wholeWordsSearch);

				models.add(new MyModel(editor, caretShiftFromSelectionStart, findManager, model));
			}

			selectSearchResultsInEditor(editor, models);
			editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
		}

		private void selectSearchResultsInEditor(Editor editor, List<MyModel> models) {
			List<CaretState> caretStates = editor.getCaretModel().getCaretsAndSelections();
			for (MyModel model : models) {
				Iterator<? extends FindResult> resultIterator = new FindResultIterator(model);
				while (resultIterator.hasNext()) {
					FindResult findResult = resultIterator.next();
					int caretOffset = getCaretPosition(findResult, model.getCaretShiftFromSelectionStart());
					int selectionStartOffset = findResult.getStartOffset();
					int selectionEndOffset = findResult.getEndOffset();
					EditorActionUtil.makePositionVisible(editor, caretOffset);
					EditorActionUtil.makePositionVisible(editor, selectionStartOffset);
					EditorActionUtil.makePositionVisible(editor, selectionEndOffset);
					caretStates.add(new CaretState(editor.offsetToLogicalPosition(caretOffset),
							editor.offsetToLogicalPosition(selectionStartOffset),
							editor.offsetToLogicalPosition(selectionEndOffset)));
				}

				try {
					if (caretStates.size() > editor.getCaretModel().getMaxCaretCount()) {
						EditorUtil.notifyMaxCarets(editor);
						return;
					}
				} catch (Throwable e) {
					//old api
				}

				if (!caretStates.isEmpty()) {
					editor.getCaretModel().setCaretsAndSelections(caretStates);
				}
			}
		}

	}

	private static class FindResultIterator implements Iterator<FindResult> {
		private final MyModel myModel;
		FindResult findResult;

		public FindResultIterator(MyModel myModel) {
			this.myModel = myModel;
			findResult = myModel.getFindManager().findString(myModel.getEditor().getDocument().getCharsSequence(), 0, myModel.getModel());
		}

		@Override
		public boolean hasNext() {
			return findResult.isStringFound();
		}

		@Override
		public FindResult next() {
			FindResult result = findResult;
			findResult = myModel.getFindManager().findString(myModel.getEditor().getDocument().getCharsSequence(), findResult.getEndOffset(), myModel.getModel());
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static int getCaretPosition(FindResult findResult, int caretShiftFromSelectionStart) {
		return caretShiftFromSelectionStart < 0
				? findResult.getEndOffset() : Math.min(findResult.getStartOffset() + caretShiftFromSelectionStart, findResult.getEndOffset());
	}

	public static class MyModel {
		@NotNull
		private final Editor editor;
		private final int caretShiftFromSelectionStart;
		private final FindManager findManager;
		private final FindModel model;

		public MyModel(@NotNull Editor editor, int caretShiftFromSelectionStart, FindManager findManager, FindModel model) {
			this.editor = editor;
			this.caretShiftFromSelectionStart = caretShiftFromSelectionStart;
			this.findManager = findManager;
			this.model = model;
		}

		public Editor getEditor() {
			return editor;
		}

		public int getCaretShiftFromSelectionStart() {
			return caretShiftFromSelectionStart;
		}

		public FindManager getFindManager() {
			return findManager;
		}

		public FindModel getModel() {
			return model;
		}
	}
}

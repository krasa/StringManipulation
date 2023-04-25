// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package osmedile.intellij.stringmanip.replace;

import com.intellij.find.FindBundle;
import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.find.FindResult;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MyFindUtil {

	private MyFindUtil() {
	}

	public static boolean doReplace(@NotNull Project project,
									@NotNull FindModel aModel,
									int caretOffset,
									Document document) {
		FindManager findManager = FindManager.getInstance(project);
		final FindModel model = aModel.clone();
		List<Pair<TextRange, String>> rangesToChange = new ArrayList<>();
		boolean replaced = false;

		int offset = caretOffset;
		while (offset >= 0 && offset < document.getTextLength()) {
			caretOffset = offset;
			FindResult result = findManager.findString(document.getCharsSequence(), offset, model, null);
			if (result == null || !result.isStringFound()) {
				break;
			}
			model.setFromCursor(true);

			int startOffset = result.getStartOffset();
			int endOffset = result.getEndOffset();
			String foundString = document.getCharsSequence().subSequence(startOffset, endOffset).toString();
			String toReplace;
			try {
				toReplace = findManager.getStringToReplace(foundString, model, startOffset, document.getCharsSequence());
			} catch (FindManager.MalformedReplacementStringException e) {
				if (!ApplicationManager.getApplication().isUnitTestMode()) {
					Messages.showErrorDialog(project, e.getMessage(), FindBundle.message("find.replace.invalid.replacement.string.title"));
				}
				break;
			}

			int newOffset;
			TextRange textRange = doReplace(document, model, result, toReplace, rangesToChange);
			replaced = true;
			newOffset = model.isForward() ? textRange.getEndOffset() : textRange.getStartOffset();
			if (textRange.isEmpty()) ++newOffset;

			if (newOffset == offset) {
				newOffset += model.isForward() ? 1 : -1;
			}
			offset = newOffset;
		}

		if (replaced) {
			CharSequence text = document.getCharsSequence();
			final StringBuilder newText = new StringBuilder(document.getTextLength());
			rangesToChange.sort(Comparator.comparingInt(o -> o.getFirst().getStartOffset()));
			int offsetBefore = 0;
			for (Pair<TextRange, String> pair : rangesToChange) {
				TextRange range = pair.getFirst();
				String replace = pair.getSecond();
				newText.append(text, offsetBefore, range.getStartOffset()); //before change
				newText.append(replace);
				offsetBefore = range.getEndOffset();
				if (offsetBefore < caretOffset) {
					caretOffset += replace.length() - range.getLength();
				}
			}
			newText.append(text, offsetBefore, text.length()); //tail
			ApplicationManager.getApplication().runWriteAction(() -> {
				document.setText(newText);
			});
		}

		return replaced;
	}


	private static TextRange doReplace(final Document document,
									   @NotNull FindModel model,
									   FindResult result,
									   @NotNull String stringToReplace,
									   List<? super Pair<TextRange, String>> rangesToChange) {
		final int startOffset = result.getStartOffset();
		final int endOffset = result.getEndOffset();

		int newOffset;
		final String converted = StringUtil.convertLineSeparators(stringToReplace);
		TextRange textRange = new TextRange(startOffset, endOffset);
		rangesToChange.add(Pair.create(textRange, converted));

		newOffset = endOffset;

		int start = startOffset;
		int end = newOffset;
		if (model.isRegularExpressions()) {
			String toFind = model.getStringToFind();
			if (model.isForward()) {
				if (StringUtil.endsWithChar(toFind, '$')) {
					int i = 0;
					int length = toFind.length();
					while (i + 2 <= length && toFind.charAt(length - i - 2) == '\\') i++;
					if (i % 2 == 0) end++; //This $ is a special symbol in regexp syntax
				} else if (StringUtil.startsWithChar(toFind, '^')) {
					while (end < document.getTextLength() && document.getCharsSequence().charAt(end) != '\n') end++;
				}
			} else {
				if (StringUtil.startsWithChar(toFind, '^')) {
					start--;
				} else if (StringUtil.endsWithChar(toFind, '$')) {
					while (start >= 0 && document.getCharsSequence().charAt(start) != '\n') start--;
				}
			}
		}
		return new TextRange(start, end);
	}

}

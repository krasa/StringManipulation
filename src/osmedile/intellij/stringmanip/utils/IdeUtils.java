package osmedile.intellij.stringmanip.utils;

import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IdeUtils {
	public static void sort(List<CaretState> caretsAndSelections) {
		Collections.sort(caretsAndSelections, new Comparator<CaretState>() {
			@Override
			public int compare(CaretState o1, CaretState o2) {
				return o1.getCaretPosition().compareTo(o2.getCaretPosition());
			}
		});
	}

	public static EditorImpl createEditorPreview(String text, boolean editable, Disposable disposable) {
		EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
		ColorAndFontOptions options = new ColorAndFontOptions();
		options.reset();
		options.selectScheme(scheme.getName());

		EditorFactory editorFactory = EditorFactory.getInstance();
		Document editorDocument = editorFactory.createDocument(text);
		EditorEx editor = (EditorEx) (editable ? editorFactory.createEditor(editorDocument) : editorFactory.createViewer(editorDocument));
		editor.setColorsScheme(scheme);
		EditorSettings settings = editor.getSettings();
		settings.setLineNumbersShown(true);
		settings.setWhitespacesShown(false);
		settings.setLineMarkerAreaShown(false);
		settings.setIndentGuidesShown(false);
		settings.setFoldingOutlineShown(false);
		settings.setAdditionalColumnsCount(0);
		settings.setAdditionalLinesCount(0);
		settings.setRightMarginShown(false);
		Disposer.register(disposable, () -> EditorFactory.getInstance().releaseEditor(editor));
		return (EditorImpl) editor;
	}

	public static String stacktraceToString(Throwable e) {
		StringWriter writer = new StringWriter();
		StackTraceElement[] trace = e.getStackTrace();
		printStackTrace(writer, trace);
		String s1 = writer.toString();
		return s1;
	}

	public static void printStackTrace(@NotNull Writer f, StackTraceElement[] stackTraceElements) {
		try {
			for (StackTraceElement element : stackTraceElements) {
				f.write("\tat " + element + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

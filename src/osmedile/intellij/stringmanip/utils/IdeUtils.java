package osmedile.intellij.stringmanip.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorImpl;

public class IdeUtils {
	public static void sort(List<CaretState> caretsAndSelections) {
		Collections.sort(caretsAndSelections, new Comparator<CaretState>() {
			@Override
			public int compare(CaretState o1, CaretState o2) {
				return o1.getCaretPosition().compareTo(o2.getCaretPosition());
			}
		});
	}

	public static EditorImpl createEditorPreview(String text, boolean editable) {
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

		return (EditorImpl) editor;
	}

}

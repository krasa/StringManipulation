package osmedile.intellij.stringmanip.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.intellij.openapi.editor.CaretState;

public class IdeUtils {
	public static void sort(List<CaretState> caretsAndSelections) {
		Collections.sort(caretsAndSelections, new Comparator<CaretState>() {
			@Override
			public int compare(CaretState o1, CaretState o2) {
				return o1.getCaretPosition().compareTo(o2.getCaretPosition());
			}
		});
	}

}

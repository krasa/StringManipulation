package osmedile.intellij.stringmanip.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
	/**
	 * https://stackoverflow.com/a/16966699/685796
	 */
	public static Iterable<Field> getFieldsUpTo(Class<?> startClass,
												@Nullable Class<?> exclusiveParent) {

		List<Field> currentClassFields = new ArrayList<>(List.of(startClass.getDeclaredFields()));
		Class<?> parentClass = startClass.getSuperclass();

		if (parentClass != null && (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
			List<Field> parentClassFields = (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
			currentClassFields.addAll(parentClassFields);
		}

		return currentClassFields;
	}
}

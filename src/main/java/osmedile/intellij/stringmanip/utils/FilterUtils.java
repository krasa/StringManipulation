package osmedile.intellij.stringmanip.utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FilterUtils {
	public static final String[] EMPTY = new String[0];

	@NotNull
	public static String[] keepDuplicates(@NotNull String[] split) {
		if (split.length > 0) {
			Set<String> set = new HashSet<String>();
			for (int i = 0; i < split.length; i++) {
				boolean unique = set.add(split[i]);
				if (unique) {
					split[i] = null;
				}
			}
			return filterDuplicates(split);
		}
		return EMPTY;
	}

	@NotNull
	public static List<Group> groupAndSortByCount(@NotNull String[] split) {
		if (split.length > 0) {
			LinkedHashMap<String, Group> map = new LinkedHashMap<>();
			for (String e : split) {
				Group count = map.get(e);
				if (count == null) {
					count = new Group(e);
					map.put(e, count);
				} else {
					count.increment();
				}
			}

			ArrayList<Group> groups = new ArrayList<>(map.values());
			groups.sort(Comparator.comparingInt((Group o) -> o.count).reversed());

			return groups;
		}
		return Collections.emptyList();
	}

	@NotNull
	public static String[] filterDuplicates(@NotNull String[] split) {
		if (split.length > 0) {
			Set<String> set = new HashSet<String>();
			for (int i = 0; i < split.length; i++) {
				boolean unique = set.add(split[i]);
				if (!unique) {
					split[i] = null;
				}
			}
			return split;
		}
		return EMPTY;
	}

	@NotNull
	protected static String[] filterNull(String[] firstArray) {
		List<String> list = new ArrayList<String>();

		for (String s : firstArray) {
			if (s != null && s.length() > 0) {
				list.add(s);
			}
		}

		return list.toArray(new String[list.size()]);
	}

	public static class Group {
		public String text;
		public int count = 1;

		public Group(String e) {
			text = e;
		}

		public Group increment() {
			count++;
			return this;
		}
	}
}

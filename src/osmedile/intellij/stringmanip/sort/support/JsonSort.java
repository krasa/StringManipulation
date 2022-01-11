package osmedile.intellij.stringmanip.sort.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JsonSort {
	private static final Logger log = LoggerFactory.getLogger(JsonSort.class);

	private Project project;
	private String originalLines;
	private final SortSettings sortSettings;

	public JsonSort(Project project, List<String> originalLines, SortSettings sortSettings) {
		this.project = project;
		this.originalLines = StringUtils.join(originalLines.toArray(), '\n');
		this.sortSettings = sortSettings;
	}

	public List<String> sort() {

		try {
			Comparator<Sortable> sortLineComparator = sortSettings.getSortLineComparator();
			Comparator<String> comparator = new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return sortLineComparator.compare(new SortLine(o1, sortSettings), new SortLine(o2, sortSettings));
				}
			};
			ObjectMapper mapper = new ObjectMapper();
			Object o = mapper.readValue(originalLines, Object.class);
			sort(comparator, o);
			String sortedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);


			if (project != null) {
				sortedJson = reformat(sortedJson, this.project);
			}
			return Arrays.asList(sortedJson.split("\r\n"));
		} catch (RuntimeException e) {
			throw e;
		}catch (Throwable e) {
			throw new SortException(e);
		}
	}

	@NotNull
	public static String reformat(String sortedJson, Project project) {
		if (!ApplicationManager.getApplication().isWriteAccessAllowed()) {
			return sortedJson;
		}
		if (CommandProcessor.getInstance().isUndoTransparentActionInProgress()) {
			return sortedJson;
		}
		PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(JsonLanguage.INSTANCE, sortedJson);
//		CommandProcessor.getInstance().executeCommand(project, () -> CodeStyleManager.getInstance(project).reformat(psiFile), "StringManipulation-JsonReformat",null);
		CodeStyleManager.getInstance(project).reformat(psiFile);
		Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
		return document.getText();
	}

	private void sort(Comparator<String> comparator, Object o) {
		if (o instanceof List) {
			sortList(comparator, (List) o);
		} else if (o instanceof Map) {
			sortMap(comparator, (Map) o);
		}
	}

	private void sortList(Comparator<String> comparator, List<?> o1) {
		o1.sort(new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof String && o2 instanceof String) {
					return comparator.compare((String) o1, (String) o2);
				}
				if (o1 instanceof String) {
					return -1;
				}
				if (o2 instanceof String) {
					return 1;
				}
				return 0;
			}
		});
		for (Object o : o1) {
			sort(comparator, o);
		}
	}

	private void sortMap(Comparator<String> comparator, Map<String, Object> jsonMap) {
		Map<String, Object> treeMap = new TreeMap<>(comparator);
		treeMap.putAll(jsonMap);

		for (Map.Entry<String, Object> stringObjectEntry : treeMap.entrySet()) {
			Object value = stringObjectEntry.getValue();
			sort(comparator, value);
		}

		jsonMap.clear();
		jsonMap.putAll(treeMap);
	}


}

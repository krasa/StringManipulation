package osmedile.intellij.stringmanip.sort.support;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
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

	protected Project project;
	protected final SortSettings sortSettings;

	public JsonSort(Project project, SortSettings sortSettings) {
		this.project = project;
		this.sortSettings = sortSettings;
	}

	public List<String> sort(List<String> originalLines) {
		String text = StringUtils.join(originalLines.toArray(), '\n');
		String sort = sort(text);
		String[] split = sort.split("\n");
		return Arrays.asList(split);
	}

	public String sort(String text) {
		try {
			Comparator<String> stringComparator = sortSettings.getStringComparator();
			ObjectMapper mapper = getMapper();
			Object o = mapper.readValue(text, Object.class);
			sort(stringComparator, o);

			String sortedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);

			if (project != null) {
				sortedJson = reformat(sortedJson, this.project);
			}
			return sortedJson;
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable e) {
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

	@NotNull
	protected ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		DefaultPrettyPrinter p = new DefaultPrettyPrinter();
		DefaultPrettyPrinter.Indenter i = new DefaultIndenter("  ", "\n");
		p.indentArraysWith(i);
		p.indentObjectsWith(i);
		mapper.setDefaultPrettyPrinter(p);
		return mapper;
	}

	protected void sort(Comparator<String> comparator, Object o) {
		if (o instanceof List) {
			sortList(comparator, (List) o);
		} else if (o instanceof Map) {
			sortMap(comparator, (Map) o);
		}
	}

	protected void sortList(Comparator<String> comparator, List<?> o1) {
		//do not sort arrays
//		o1.sort(new Comparator<Object>() {
//			@Override
//			public int compare(Object o1, Object o2) {
//				if (o1 instanceof String && o2 instanceof String) {
//					return comparator.compare((String) o1, (String) o2);
//				}
//				if (o1 instanceof String) {
//					return -1;
//				}
//				if (o2 instanceof String) {
//					return 1;
//				}
//				return 0;
//			}
//		});
		for (Object o : o1) {
			sort(comparator, o);
		}
	}

	protected void sortMap(Comparator<String> comparator, Map<String, Object> jsonMap) {
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

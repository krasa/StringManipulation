package osmedile.intellij.stringmanip.transform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.ui.Messages;
import org.json.JSONObject;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import javax.swing.*;
import java.util.Map;

public class ConvertJsonYamlAction extends AbstractStringManipAction<Object> {
    private static final Logger LOG = Logger.getInstance(ConvertJsonYamlAction.class);

    private final Dump yamlDump;
    private final Gson gson;

    public ConvertJsonYamlAction() {
        DumpSettings yamlDumpSettings = DumpSettings.builder()
            .setDefaultFlowStyle(FlowStyle.BLOCK)
            .build();
        yamlDump = new Dump(yamlDumpSettings);

        gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();
    }

    @Override
    protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
        try {
            String trimmedText = selectedText.trim();
            return trimmedText.startsWith("{") || trimmedText.startsWith("[")
                ? jsonToYaml(selectedText)
                : yamlToJson(selectedText);
        } catch (Throwable e) {
            SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), String.valueOf(e), "Convert Between JSON and YAML"));
            LOG.info(e);
            throw new ProcessCanceledException(e);
        }
    }

    protected String jsonToYaml(String text) {
        JSONObject jsonObject = new JSONObject(text);
        Map<String, Object> map = jsonObject.toMap();

        return yamlDump.dumpToString(map).trim();
    }

    protected String yamlToJson(String selectedText) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(selectedText);

        return gson.toJson(map);
    }

    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        throw new RuntimeException();
    }
}
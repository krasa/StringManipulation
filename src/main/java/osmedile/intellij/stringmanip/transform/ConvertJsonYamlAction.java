package osmedile.intellij.stringmanip.transform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.ui.Messages;
import org.json.JSONArray;
import org.json.JSONObject;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.StringManipulationBundle;

import javax.swing.*;
import java.util.Map;

public class ConvertJsonYamlAction extends AbstractStringManipAction<Object> {
    private static final Logger LOG = Logger.getInstance(ConvertJsonYamlAction.class);

    @Override
    protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
        try {
            String trimmedText = selectedText.trim();
            return trimmedText.startsWith("{") || trimmedText.startsWith("[")
                ? jsonToYaml(selectedText)
                : yamlToJson(selectedText);
        } catch (Throwable e) {
            SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), String.valueOf(e), StringManipulationBundle.message("dialog.title.convert.between.json.yaml")));
            LOG.info(e);
            throw new ProcessCanceledException(e);
        }
    }

    protected String jsonToYaml(String text) {
        Object obj;
        if (text.trim().startsWith("{")) {
            JSONObject jsonObject = new JSONObject(text);
            obj = jsonObject.toMap();
        } else {
            JSONArray jsonArray = new JSONArray(text);
            obj = jsonArray.toList();
        }

        return getYamlDump().dumpToString(obj).trim();
    }

    protected String yamlToJson(String selectedText) {
        Yaml yaml = new Yaml(new Constructor(new LoaderOptions()), new Representer(new DumperOptions()),
            new DumperOptions(), new CustomYamlTagResolver());

        Object obj = yaml.load(selectedText);

        return getGson().toJson(obj).trim();
    }

    private static class CustomYamlTagResolver extends Resolver {

        /*
         * Do not resolve timestamps, leave them as string.
         */
        protected void addImplicitResolvers() {
            addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
            addImplicitResolver(Tag.INT, INT, "-+0123456789");
            addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
            addImplicitResolver(Tag.MERGE, MERGE, "<");
            addImplicitResolver(Tag.NULL, NULL, "~nN\u0000");
            addImplicitResolver(Tag.NULL, EMPTY, null);
            addImplicitResolver(Tag.YAML, YAML, "!&*");
        }
    }

    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        throw new RuntimeException();
    }

    private Dump getYamlDump() {
        DumpSettings yamlDumpSettings = DumpSettings.builder()
            .setDefaultFlowStyle(FlowStyle.BLOCK)
            .build();
        Dump yamlDump = new Dump(yamlDumpSettings);
        return yamlDump;
    }

    private Gson getGson() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();
        return gson;
    }
}
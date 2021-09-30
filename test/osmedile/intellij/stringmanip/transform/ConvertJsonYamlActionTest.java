package osmedile.intellij.stringmanip.transform;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ConvertJsonYamlActionTest {

    @Test
    public void jsonObjectToYaml() {
        String inputJson = readFile("objectInput.json");
        String outputYaml = readFile("objectOutput.yml");

        String yaml = new ConvertJsonYamlAction().jsonToYaml(inputJson);
        assertEquals(outputYaml, yaml);
    }

    @Test
    public void jsonArrayToYaml() {
        String inputJson = readFile("array.json");
        String outputYaml = readFile("array.yml");

        String yaml = new ConvertJsonYamlAction().jsonToYaml(inputJson);
        assertEquals(outputYaml, yaml);
    }

    @Test
    public void yamlObjectToJson() {
        String inputYaml = readFile("objectInput.yml");
        String outputJson = readFile("objectOutput.json");

        String json = new ConvertJsonYamlAction().yamlToJson(inputYaml);
        assertEquals(outputJson, json.replace("\r", ""));
    }

    @Test
    public void yamlArrayToJson() {
        String inputYaml = readFile("array.yml");
        String outputJson = readFile("array.json");

        String json = new ConvertJsonYamlAction().yamlToJson(inputYaml);
        assertEquals(outputJson, json);
    }

    private String readFile(String fileName) {
        try {
            String s = FileUtils.readFileToString(new File("test/osmedile/intellij/stringmanip/transform/data/" + fileName), StandardCharsets.UTF_8);
            return s.replace("\r", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
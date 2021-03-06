package osmedile.intellij.stringmanip.transform;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ConvertJsonYamlActionTest {

    @Test
    public void jsonToYaml() {
        String inputJson = readFile("input.json");
        String outputYaml = readFile("output.yml");

        String yaml = new ConvertJsonYamlAction().jsonToYaml(inputJson);
        assertEquals(outputYaml, yaml);
    }

    @Test
    public void yamlToJson() {
        String inputYaml = readFile("input.yml");
        String outputJson = readFile("output.json");

        String json = new ConvertJsonYamlAction().yamlToJson(inputYaml);
        assertEquals(outputJson, json);
    }

    private String readFile(String fileName) {
        try {
            return FileUtils.readFileToString(new File("test/osmedile/intellij/stringmanip/transform/data/" + fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
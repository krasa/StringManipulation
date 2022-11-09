package osmedile.intellij.stringmanip.transform;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class MinifyJsonActionTest {
	@Test
	public void test() {
		String input = readFile("minifyJsonInput.json");
		String output = readFile("minifyJsonOutput.json");

		String actual = MinifyJsonAction.minify(input);
		assertEquals(output, actual);

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
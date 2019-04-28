package osmedile.intellij.stringmanip.styles.action;

import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.utils.Cloner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static osmedile.intellij.stringmanip.styles.Style.*;

public class DefaultActions {

	public static final String SWITCH_STYLE_ACTION = "StringManipulation.SwitchStyleAction";
	public static final List<StyleStep> DEFAULT = Helper.getStyleSteps();
	public static final Map<Style, Boolean> DEFAULT_AS_MAP = Helper.getStyleBooleanHashMap(DEFAULT);


	private static class Helper {

		@NotNull
		private static List<StyleStep> getStyleSteps() {
			return Arrays.asList(new StyleStep(true, KEBAB_LOWERCASE),
				new StyleStep(true, KEBAB_UPPERCASE),
				new StyleStep(true, SNAKE_CASE),
				new StyleStep(false, _SINGLE_WORD_CAPITALIZED),
				new StyleStep(true, SCREAMING_SNAKE_CASE),
				new StyleStep(true, DOT),
				new StyleStep(false, _ALL_UPPER_CASE),
				new StyleStep(true, WORD_LOWERCASE),
				new StyleStep(true, SENTENCE_CASE),
				new StyleStep(true, WORD_CAPITALIZED),
				new StyleStep(true, PASCAL_CASE),
				new StyleStep(false, _UNKNOWN),
				new StyleStep(true, CAMEL_CASE));
		}


		@NotNull
		private static HashMap<Style, Boolean> getStyleBooleanHashMap(List<StyleStep> aDefault) {
			HashMap<Style, Boolean> styleBooleanHashMap = new HashMap<>();
			for (StyleStep step : aDefault) {
				styleBooleanHashMap.put(step.getStyleAsEnum(), step.isEnabled());
			}
			return styleBooleanHashMap;
		}
	}

	@NotNull
	public static StyleActionModel defaultSwitchCase() {
		StyleActionModel e = StyleActionModel.create(DefaultActions.SWITCH_STYLE_ACTION);
		resetDefaultSwitchCase(e);
		return e;
	}

	@NotNull
	public static void resetDefaultSwitchCase(StyleActionModel styleActionModel) {
		styleActionModel.setName("Switch case");
		styleActionModel.setId(SWITCH_STYLE_ACTION);
		styleActionModel.setSteps(Cloner.deepClone(DEFAULT));
	}


}

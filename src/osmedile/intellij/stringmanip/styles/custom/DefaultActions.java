package osmedile.intellij.stringmanip.styles.custom;

import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.utils.Cloner;

import java.util.*;

import static osmedile.intellij.stringmanip.styles.Style.*;

public class DefaultActions {

	public static final String SWITCH_STYLE_ACTION = "StringManipulation.SwitchStyleAction";
	public static final List<CustomActionModel.Step> DEFAULT = Helper.getStyleSteps();
	public static final Map<Style, Boolean> DEFAULT_AS_MAP = Helper.getStyleBooleanHashMap(DEFAULT);

	@NotNull
	public static List<CustomActionModel> defaultActions() {
		List<CustomActionModel> customActionModels = new ArrayList<>();
		customActionModels.add(defaultSwitchCase());
		return customActionModels;
	}


	private static class Helper {

		@NotNull
		private static List<CustomActionModel.Step> getStyleSteps() {
			return Arrays.asList(
				new CustomActionModel.Step(false, _UNKNOWN),
				new CustomActionModel.Step(true, CAMEL_CASE),
				new CustomActionModel.Step(true, KEBAB_LOWERCASE),
				new CustomActionModel.Step(true, KEBAB_UPPERCASE),
				new CustomActionModel.Step(true, SNAKE_CASE),
				new CustomActionModel.Step(false, _SINGLE_WORD_CAPITALIZED),
				new CustomActionModel.Step(true, SCREAMING_SNAKE_CASE),
				new CustomActionModel.Step(true, DOT),
				new CustomActionModel.Step(false, _ALL_UPPER_CASE),
				new CustomActionModel.Step(true, WORD_LOWERCASE),
				new CustomActionModel.Step(true, SENTENCE_CASE),
				new CustomActionModel.Step(true, WORD_CAPITALIZED),
				new CustomActionModel.Step(true, PASCAL_CASE)
			);
		}


		@NotNull
		private static HashMap<Style, Boolean> getStyleBooleanHashMap(List<CustomActionModel.Step> aDefault) {
			HashMap<Style, Boolean> styleBooleanHashMap = new HashMap<>();
			for (CustomActionModel.Step step : aDefault) {
				styleBooleanHashMap.put(step.getStyleAsEnum(), step.isEnabled());
			}
			return styleBooleanHashMap;
		}
	}

	@NotNull
	public static CustomActionModel defaultSwitchCase() {
		CustomActionModel e = CustomActionModel.create(DefaultActions.SWITCH_STYLE_ACTION);
		resetDefaultSwitchCase(e);
		return e;
	}

	@NotNull
	public static void resetDefaultSwitchCase(CustomActionModel model) {
		model.setName("Switch case");
		model.setId(SWITCH_STYLE_ACTION);
		model.setSteps(Cloner.deepClone(DEFAULT));
	}


}

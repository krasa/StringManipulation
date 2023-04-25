package osmedile.intellij.stringmanip.styles.custom;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.switching.AbstractSwitchingCaseConvertingAction;

import java.util.List;
import java.util.Map;

public class CustomAction extends AbstractSwitchingCaseConvertingAction {
	public static final String FROM = "from";

	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CustomAction.class);
	private CustomActionModel customActionModel;

	public CustomAction(CustomActionModel customActionModel) {
		this(true, customActionModel);
	}

	public CustomAction(boolean setupHandler, CustomActionModel model) {
		super(setupHandler);
		this.customActionModel = model;
		getTemplatePresentation().setText(customActionModel.getName());
		String description = makeDescription();
		getTemplatePresentation().setDescription(description);

		if (setupHandler) {
			myHandler.setCustomActionModel(model);
		}
	}

	@NotNull
	protected String makeDescription() {
		StringBuilder sb = new StringBuilder();
		List<CustomActionModel.Step> steps = customActionModel.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			CustomActionModel.Step step = steps.get(i);
			if (step.isEnabled()) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				Style styleAsEnum = step.getStyleAsEnum();
				String presentableName = styleAsEnum.getPresentableName();
				sb.append(presentableName);
			}
		}
		return sb.toString();
	}

	protected Style currentStyle(Map<String, Object> actionContext, String text, List<CustomActionModel.Step> steps) {
		for (CustomActionModel.Step step : steps) {
			if (!step.isEnabled()) {
				continue;
			}
			if (actionContext.containsKey(step.getStyle())) {
				return step.getStyleAsEnum();
			}
		}

		//fallback
		Style from = (Style) actionContext.get(FROM);
		if (from == null) {
			from = Style.from(text);
			actionContext.put(FROM, from);
		}
		return from;
	}

	private Style nextStyle(Style from, List<CustomActionModel.Step> steps) {
		boolean found = false;
		for (int i = 0; i < steps.size(); i++) {
			CustomActionModel.Step step = steps.get(i);
			Style stepStyle = step.getStyleAsEnum();

			if (stepStyle == from) {
				found = true;
				continue;
			}

			if (found) {
				if (step.isEnabled()) {
					return step.getStyleAsEnum();
				}
			}
		}

		//return first
		for (int i = 0; i < steps.size(); i++) {
			CustomActionModel.Step step = steps.get(i);
			if (step.isEnabled()) {
				return step.getStyleAsEnum();
			}
		}
		throw new RuntimeException("No enabled steps. " + customActionModel);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		List<CustomActionModel.Step> steps = customActionModel.getSteps();

		Style from = currentStyle(actionContext, s, steps);
		Style to = nextStyle(from, steps);

		if (!setupHandler) {
			System.out.println("from " + from.getPresentableName() + " to " + to.getPresentableName());
		}
		return to.transform(s);
	}


}

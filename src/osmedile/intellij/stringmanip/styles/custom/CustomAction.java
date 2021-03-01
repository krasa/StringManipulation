package osmedile.intellij.stringmanip.styles.custom;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.styles.AbstractCaseConvertingAction;
import osmedile.intellij.stringmanip.styles.Style;

import java.util.List;
import java.util.Map;

public class CustomAction extends AbstractCaseConvertingAction {
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

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		List<CustomActionModel.Step> steps = customActionModel.getSteps();

		Style from = getStyle(actionContext, s);

		int currentStep = -1;
		for (int i = 0; i < steps.size(); i++) {
			CustomActionModel.Step step = steps.get(i);
			Style stepStyle = step.getStyleAsEnum();

			if (stepStyle == from) {
				currentStep = i;
				continue;
			}

			if (currentStep != -1) {
				if (step.isEnabled()) {
					if (!setupHandler) {
						System.out.println("from " + from + " to " + step.getStyle());
					}
					return stepStyle.transform( s);
				}
			}
		}
		if (currentStep == -1) {
			throw new RuntimeException("step not found " + from + " in " + steps);

		}
		if (currentStep != -1) {
			for (int i = 0; i < steps.size(); i++) {
				CustomActionModel.Step step = steps.get(i);
				if (step.isEnabled()) {
					if (!setupHandler) {
						System.out.println("from " + from + " to " + step.getStyle());
					}
					return step.getStyleAsEnum().transform( s);
				}
			}
		}
		throw new RuntimeException("No enabled steps. " + customActionModel);

	}


}

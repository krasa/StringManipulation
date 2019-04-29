package osmedile.intellij.stringmanip.styles.custom;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.MyEditorWriteActionHandler;
import osmedile.intellij.stringmanip.styles.AbstractCaseConvertingAction;
import osmedile.intellij.stringmanip.styles.Style;

import java.util.List;

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
			MyEditorWriteActionHandler handler = (MyEditorWriteActionHandler) getHandler();
			handler.setCustomActionModel(model);
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
				sb.append(step.getStyleAsEnum().getPresentableName());
			}
		}
		return sb.toString();
	}

	@Override
	public String transformByLine(String s) {
		List<CustomActionModel.Step> steps = customActionModel.getSteps();

		Style from = Style.from(s);
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
					return stepStyle.transform(from, s);
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
					return step.getStyleAsEnum().transform(from, s);
				}
			}
		}
		throw new RuntimeException("No enabled steps. " + customActionModel);

	}
}

package osmedile.intellij.stringmanip.styles.action;

import com.intellij.openapi.diagnostic.Logger;
import osmedile.intellij.stringmanip.styles.AbstractCaseConvertingAction;
import osmedile.intellij.stringmanip.styles.Style;

import java.util.List;

public class CustomStyleAction extends AbstractCaseConvertingAction {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CustomStyleAction.class);
	private StyleActionModel styleActionModel;

	public CustomStyleAction(StyleActionModel styleActionModel) {
		this(true, styleActionModel);
	}

	public CustomStyleAction(boolean b, StyleActionModel model) {
		super(b);
		this.styleActionModel = model;
		getTemplatePresentation().setText(styleActionModel.getName());
	}

	@Override
	public String transformByLine(String s) {
		List<StyleStep> steps = styleActionModel.getSteps();

		Style from = Style.from(s);
		int currentStep = -1;
		for (int i = 0; i < steps.size(); i++) {
			StyleStep step = steps.get(i);
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
				StyleStep step = steps.get(i);
				if (step.isEnabled()) {
					if (!setupHandler) {
						System.out.println("from " + from + " to " + step.getStyle());
					}
					return step.getStyleAsEnum().transform(from, s);
				}
			}
		}
		throw new RuntimeException("No enabled steps. " + styleActionModel);

	}
}

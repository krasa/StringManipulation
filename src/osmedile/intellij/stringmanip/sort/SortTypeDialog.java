package osmedile.intellij.stringmanip.sort;

import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SortTypeDialog {
	protected JPanel contentPane;

	protected JRadioButton insensitive;
	protected JRadioButton sensitive;
	protected JRadioButton length;

	private JRadioButton asc;
	private JRadioButton desc;

	public SortTypeDialog(SortAction.Sort sortType) {
		if (sortType != null) {
			switch (sortType) {
				case CASE_SENSITIVE_A_Z:
					sensitive.setSelected(true);
					asc.setSelected(true);
					break;
				case CASE_SENSITIVE_Z_A:
					sensitive.setSelected(true);
					desc.setSelected(true);
					break;
				case CASE_INSENSITIVE_A_Z:
					insensitive.setSelected(true);
					asc.setSelected(true);
					break;
				case CASE_INSENSITIVE_Z_A:
					insensitive.setSelected(true);
					desc.setSelected(true);
					break;
				case LINE_LENGTH_SHORT_LONG:
					length.setSelected(true);
					asc.setSelected(true);
					break;
				case LINE_LENGTH_LONG_SHORT:
					length.setSelected(true);
					desc.setSelected(true);
					break;
			}

		}
		contentPane.setFocusTraversalPolicy(new ComponentsListFocusTraversalPolicy() {
			@NotNull
			@Override
			protected java.util.List<Component> getOrderedComponents() {
				List<Component> jRadioButtons = new ArrayList<Component>();
				jRadioButtons.add(insensitive);
				jRadioButtons.add(sensitive);
				jRadioButtons.add(length);
				jRadioButtons.add(asc);
				jRadioButtons.add(desc);
				return jRadioButtons;
			}
		});
	}

	public SortAction.Sort getResult() {
		if (sensitive.isSelected()) {
			if (asc.isSelected()) {
				return SortAction.Sort.CASE_SENSITIVE_A_Z;
			} else {
				return SortAction.Sort.CASE_SENSITIVE_Z_A;
			}
		} else if (insensitive.isSelected()) {
			if (asc.isSelected()) {
				return SortAction.Sort.CASE_INSENSITIVE_A_Z;
			} else {
				return SortAction.Sort.CASE_INSENSITIVE_Z_A;
			}
		} else if (length.isSelected()) {
			if (asc.isSelected()) {
				return SortAction.Sort.LINE_LENGTH_SHORT_LONG;
			} else {
				return SortAction.Sort.LINE_LENGTH_LONG_SHORT;
			}
		}

		throw new IllegalStateException();
	}
}

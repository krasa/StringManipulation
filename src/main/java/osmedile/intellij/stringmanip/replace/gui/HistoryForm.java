package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.ui.SimpleListCellRenderer;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.align.ArrayListModel;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.util.function.Predicate.not;

public class HistoryForm {
	private JList<ReplaceCompositeModel> list;
	private JPanel detail;
	public JPanel root;
	private CompositeForm compositeForm;

	public HistoryForm() {
		List<ReplaceCompositeModel> history = new ArrayListModel<>(PluginPersistentStateComponent.getInstance().getReplaceHistory());
		history.removeIf(not(ReplaceCompositeModel::isValid));
		history.sort(Comparator.comparing(ReplaceCompositeModel::getDate).reversed());
		ArrayListModel<ReplaceCompositeModel> model = new ArrayListModel<>(history);
		list.setModel(model);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				compositeForm.initModel(list.getSelectedValue());
				detail.revalidate();
				detail.repaint();
			}
		});
		;
		list.setCellRenderer(new SimpleListCellRenderer<ReplaceCompositeModel>() {
			@Override
			public void customize(@NotNull JList<? extends ReplaceCompositeModel> jList, ReplaceCompositeModel replaceCompositeModel, int i, boolean b, boolean b1) {
				Date date = replaceCompositeModel.date;
				if (date != null) {
					setText(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date));
				} else {
					setText("---");
				}

			}
		});
		if (!history.isEmpty()) {
			list.setSelectedIndex(0);
			list.revalidate();
			list.repaint();
		}
	}

	private void createUIComponents() {
		compositeForm = new CompositeForm(null, true);
		detail = compositeForm.expressions;
	}

	public ReplaceCompositeModel getModel() {
		return compositeForm.getModel();
	}
}

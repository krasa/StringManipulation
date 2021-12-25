package osmedile.intellij.stringmanip.replace.gui.actions;

import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.replace.gui.CompositeForm;
import osmedile.intellij.stringmanip.replace.gui.ReplaceCompositeModel;
import osmedile.intellij.stringmanip.replace.gui.ReplaceItemModel;

public class LoadIntelliJReplaceModelAction extends DumbAwareAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		CompositeForm form = CompositeForm.PANEL.getData(e.getDataContext());
		if (form != null) {
			form.addToHistory();

			FindManager findManager = FindManager.getInstance(e.getProject());
			FindModel model = findManager.createReplaceInFileModel();
			ReplaceCompositeModel compositeModel = new ReplaceCompositeModel();
			ReplaceItemModel replaceItemModel = compositeModel.newItem();
			replaceItemModel.setTo(model.getStringToReplace());
			replaceItemModel.setPreserveCase(model.isPreserveCase());
			replaceItemModel.setWholeWords(model.isWholeWordsOnly());
			replaceItemModel.setCaseSensitive(model.isCaseSensitive());
			replaceItemModel.setFrom(model.getStringToFind());
			replaceItemModel.setRegex(model.isRegularExpressions());
			form.initModel(compositeModel);
		}
	}
}

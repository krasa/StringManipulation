package osmedile.intellij.stringmanip.align;

import com.intellij.ui.components.JBTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static osmedile.intellij.stringmanip.utils.StringUtils.isEmpty;

public class TextAlignmentForm {
    public JPanel root;
    private JPanel textfields;
    private JButton resetButton;
    private JCheckBox addSpaceBeforeSeparatorCheckBox;
    private JCheckBox trimLines;
    private JCheckBox trimValues;
    private JCheckBox addSpaceAfterSeparatorCheckBox;

    public TextAlignmentForm(ColumnAlignerModel lastSeparators) {
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textfields.removeAll();
                JBTextField field = addTextField(null);
                field.grabFocus();
                textfields.revalidate();
                textfields.repaint();
            }
        });
        setData(lastSeparators);
        init(lastSeparators.getSeparators());
    }

    private void createUIComponents() {
        textfields = new JPanel();
    }

    private void init(List<String> lastSeparators) {
        BoxLayout boxLayout = new BoxLayout(textfields, BoxLayout.Y_AXIS);
        textfields.setLayout(boxLayout);
        textfields.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (lastSeparators != null && !lastSeparators.isEmpty()) {
            for (String lastSeparator : lastSeparators) {
                if (isEmpty(lastSeparator)) {
                    continue;
                }
                addTextField(lastSeparator);
            }
            addTextField(null);
        } else {
            addTextField(",");
            addTextField(null);
        }
    }

    private JBTextField addTextField(final String lastSeparator) {
        final JBTextField comp = new JBTextField(lastSeparator);
        comp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        comp.selectAll();
                    }
                });
            }
        });   
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getMinimumSize().height));
        comp.getDocument().addDocumentListener(new DocumentListener() {
            boolean added = lastSeparator != null;

            @Override
            public void changedUpdate(DocumentEvent e) {
                add();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                add();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                add();
            }

            public void add() {
                if (!added) {
                    addTextField(null);
                    added = true;
                }
            }
        });
        textfields.add(comp);
        textfields.revalidate();
        textfields.repaint();
        return comp;
    }

    private List<String> getSeparators() {
        ArrayList<String> strings = new ArrayList<String>(textfields.getComponentCount());
        Component[] components = textfields.getComponents();
        for (Component component : components) {
            JBTextField field = (JBTextField) component;
            strings.add(field.getText());
        }
        return strings;
    }

    public JComponent getPreferredFocusedComponent() {
        return (JComponent) textfields.getComponent(0);

    }

    public void setData(ColumnAlignerModel data) {
        addSpaceBeforeSeparatorCheckBox.setSelected(data.isSpaceBefore());
        addSpaceAfterSeparatorCheckBox.setSelected(data.isSpaceAfter());
        trimValues.setSelected(data.isTrimValues());
        trimLines.setSelected(data.isTrimLines());
    }

    public ColumnAlignerModel getModel() {
        ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel();
        getData(columnAlignerModel);
        columnAlignerModel.setSeparators(getSeparators());
        return columnAlignerModel;
    }

    public void getData(ColumnAlignerModel data) {
        data.setSpaceBefore(addSpaceBeforeSeparatorCheckBox.isSelected());
        data.setSpaceAfter(addSpaceAfterSeparatorCheckBox.isSelected());
        data.setTrimValues(trimValues.isSelected());
        data.setTrimLines(trimLines.isSelected());
    }

    public boolean isModified(ColumnAlignerModel data) {
        if (addSpaceBeforeSeparatorCheckBox.isSelected() != data.isSpaceBefore()) return true;
        if (addSpaceAfterSeparatorCheckBox.isSelected() != data.isSpaceAfter()) return true;
        if (trimValues.isSelected() != data.isTrimValues()) return true;
        if (trimLines.isSelected() != data.isTrimLines()) return true;
        return false;
    }
}

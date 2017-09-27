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
    private final List<String> lastSeparators;
    public JPanel root;
    private JPanel textfields;
    private JButton resetButton;

    public TextAlignmentForm(List<String> lastSeparators) {
        this.lastSeparators = lastSeparators;
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textfields.removeAll();
                addTextField(null);
                textfields.revalidate();
                textfields.repaint();
            }
        });
    }

    private void createUIComponents() {
        textfields = new JPanel();
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

    private void addTextField(final String lastSeparator) {
        final JBTextField comp = new JBTextField(lastSeparator);
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
    }

    public List<String> getSeparators() {
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
}

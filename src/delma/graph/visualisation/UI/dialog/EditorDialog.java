/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.UI.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author aopkarja
 */
public abstract class EditorDialog extends JDialog implements ActionListener {

    public EditorDialog(Frame frame, String title, boolean modality) {
        super(frame, title, modality);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);
    }

    /**
     * Creates formated input field for allowing user to configurate option.
     * 
     * @param description
     * @param defaultValue
     * @param listener 
     */
    public void createInputField(String description, Object defaultValue, PropertyChangeListener listener) {
        JTextField desc = new JTextField(description);
        desc.setEditable(false);
        getContentPane().add(desc);

        final JFormattedTextField field = new JFormattedTextField(defaultValue);
        field.addMouseListener(new FormatedTextFieldTargetingFix());
        field.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        field.addPropertyChangeListener("value", listener);
        getContentPane().add(field);
    }

    /**
     * Creates group for radio buttons.
     * 
     * @return 
     */
    public ButtonGroup createButtonGroup() {
        return new ButtonGroup();
    }

    /**
     * Creates radio button.
     * 
     * @param description
     * @param group
     * @param listener
     * @return 
     */
    public JRadioButton createRadioButton(String description, ButtonGroup group, ActionListener listener) {
        JTextField desc = new JTextField(description);
        desc.setEditable(false);
        getContentPane().add(desc);

        JRadioButton button = new JRadioButton();
        button.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(description.charAt(0)));
        button.setActionCommand(description);
        button.addActionListener(listener);
        group.add(button);
        getContentPane().add(button);
        return button;
    }

    public ActionListener getVisibilityToggleListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleVisibility();
            }
        };
    }

    /**
     * Hides or shows dialog.
     */
    public void toggleVisibility() {
        setVisible(!isVisible());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Fixes mouse cursor going to start of FormatedTextField.
     */
    private static class FormatedTextFieldTargetingFix extends MouseAdapter {

        public FormatedTextFieldTargetingFix() {
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JTextField field = (JTextField) e.getSource();
                    int offset = field.viewToModel(e.getPoint());
                    field.setCaretPosition(offset);
                }
            });
        }
    }
}

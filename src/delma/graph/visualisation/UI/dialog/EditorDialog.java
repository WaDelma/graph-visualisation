/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.UI.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author aopkarja
 */
public abstract class EditorDialog extends JDialog implements ActionListener, FocusListener {

    public EditorDialog(Frame frame, String title, boolean modality) {
        super(frame, title, modality);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationByPlatform(true);
        //setFocusable(true);
        setResizable(false);
    }

    void createInputField(String description, Object defaultValue, PropertyChangeListener listener) {
        JTextField desc = new JTextField(description);
        desc.setEditable(false);
        getContentPane().add(desc);

        final JFormattedTextField field = new JFormattedTextField(defaultValue);
        field.addMouseListener(new MouseAdapter() {
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
        });
        //field.setValue(defaultValue);
        field.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        field.addPropertyChangeListener("value", listener);
        getContentPane().add(field);
    }

    public ActionListener getVisibilityToggleListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleVisibility();
            }
        };
    }

    public void toggleVisibility() {
        setVisible(!isVisible());
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        //setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}

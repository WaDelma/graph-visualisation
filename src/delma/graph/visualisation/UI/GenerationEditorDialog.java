package delma.graph.visualisation.UI;

import delma.graph.visualisation.GraphGenerator;
import java.awt.Frame;
import java.awt.GridLayout;
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
 * Dialog for configurating GraphGenerator
 * 
 * @author aopkarja
 */
public class GenerationEditorDialog extends JDialog implements ActionListener, FocusListener {

    public GenerationEditorDialog(Frame owner, String title, GraphGenerator generator) {
        super(owner, title, false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationByPlatform(true);
        //setFocusable(true);
        setResizable(false);
        getContentPane().setLayout(new GridLayout(3, 2));

        createInputField("Nodes: ", generator.getNodeCount(), generator.getNodeListener());
        createInputField("Edges: ", generator.getEdgeCount(), generator.getEdgeListener());
        createInputField("Max weight: ", generator.getMaxWeigth(), generator.getWeigthListener());

        setSize(200, 90);
    }

    private void createInputField(String description, int defaultValue, PropertyChangeListener listener) {
        JTextField desc = new JTextField(description);
        desc.setEditable(false);
        getContentPane().add(desc);

        final JFormattedTextField field = new JFormattedTextField(NumberFormat.getIntegerInstance());
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
        field.setValue(defaultValue);
        field.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        field.addPropertyChangeListener("value", listener);
        getContentPane().add(field);
    }

    public void toggleVisibility() {
        setVisible(!isVisible());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        //setVisible(false);
    }

    public ActionListener getVisibilityToggleListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleVisibility();
            }
        };
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.UI.dialog;

import delma.graph.visualisation.UI.dialog.EditorDialog;
import delma.graph.visualisation.generation.VisualGenerator;
import java.awt.GridLayout;
import javax.swing.JFrame;

/**
 *
 * @author aopkarja
 */
public class CalculationEditorDialog extends EditorDialog {

    public CalculationEditorDialog(JFrame frame, String title, VisualGenerator visualGenerator) {
        super(frame, title, false);
        getContentPane().setLayout(new GridLayout(1, 2));

        createInputField("Optimisation: ", visualGenerator.getOptimisation(), visualGenerator.getOptimisationListener());

        pack();
    }
    
}

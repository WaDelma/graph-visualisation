/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.UI.dialog;

import delma.graph.visualisation.UI.dialog.EditorDialog;
import delma.graph.visualisation.generation.GraphVisualGenerator;
import delma.graph.visualisation.generation.VisualGenerator;
import java.awt.GridLayout;
import javax.swing.JFrame;

/**
 *
 * @author aopkarja
 */
public class CalculationEditorDialog extends EditorDialog {

    public CalculationEditorDialog(JFrame frame, String title, VisualGenerator visualCalculator) {
        super(frame, title, false);
        getContentPane().setLayout(new GridLayout(1, 2));

        createInputField("Optimisation: ", visualCalculator.getOptimisation(), visualCalculator.getOptimisationListener());
       //createInputField("Edges: ", generator.getEdgeCount(), generator.getEdgeListener());
        //createInputField("Max weight: ", generator.getMaxWeigth(), generator.getWeigthListener());

        setSize(300, 50);
    }
    
}

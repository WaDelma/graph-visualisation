/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.UI;

import delma.graph.visualisation.GraphVisualCalculator;
import java.awt.GridLayout;
import javax.swing.JFrame;

/**
 *
 * @author aopkarja
 */
public class CalculationEditorDialog extends EditorDialog {

    public CalculationEditorDialog(JFrame frame, String title, GraphVisualCalculator visualCalculator) {
        super(frame, title, false);
        getContentPane().setLayout(new GridLayout(1, 2));

        createInputField("Repulsion optimisation: ", visualCalculator.getRepulsionOptimisation(), visualCalculator.getRepulsionListener());
       //createInputField("Edges: ", generator.getEdgeCount(), generator.getEdgeListener());
        //createInputField("Max weight: ", generator.getMaxWeigth(), generator.getWeigthListener());

        setSize(300, 30);
    }
    
}

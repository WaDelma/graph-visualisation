package delma.graph.visualisation.UI.dialog;

import delma.graph.GraphGenerator;
import java.awt.Frame;
import java.awt.GridLayout;

/**
 * Dialog for configurating GraphGenerator
 *
 * @author aopkarja
 */
public class GenerationEditorDialog extends EditorDialog {

    public GenerationEditorDialog(Frame owner, String title, GraphGenerator generator) {
        super(owner, title, false);
        getContentPane().setLayout(new GridLayout(3, 2));
        createInputField("Nodes: ", generator.getNodeCount(), generator.getNodeListener());
        createInputField("Edges: ", generator.getEdgeCount(), generator.getEdgeListener());
        createInputField("Max weight: ", generator.getMaxWeigth(), generator.getWeigthListener());

        pack();
    }
}

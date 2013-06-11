package delma.graph.visualisation.UI.dialog;

import delma.graph.visualisation.UI.dialog.EditorDialog;
import delma.graph.visualisation.GraphGenerator;
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

        setSize(200, 90);
    }
}

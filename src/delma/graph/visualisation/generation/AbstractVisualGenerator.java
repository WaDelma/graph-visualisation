/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.generation;

import delma.graph.visualisation.UI.UIGraphVisualisation.Requirement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author aopkarja
 */
public abstract class AbstractVisualGenerator<N> implements VisualGenerator<N> {

    @Override
    public ActionListener getInitialisationListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialise();
            }
        };
    }

    @Override
    public ActionListener getStepListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateStep();
            }
        };
    }

    @Override
    public Requirement getStepChecker() {
        return new Requirement() {
            @Override
            public boolean check() {
                return !isReady();
            }
        };
    }
}

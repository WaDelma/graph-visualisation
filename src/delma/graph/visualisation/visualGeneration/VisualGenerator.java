package delma.graph.visualisation.visualGeneration;

import delma.graph.visualisation.UI.UIGraphVisualisation;
import delma.graph.visualisation.UI.UIGraphVisualisation.Requirement;
import delma.graph.visualisation.Vector;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

/**
 *
 * @author aopkarja
 */
public interface VisualGenerator<N> {

    /**
     * Gets coordinates for node
     *
     * @param n
     * @return
     */
    public Vector getCoordinates(N n);

    /**
     * How many times calculateSteps have been called.
     */
    public int steps();

    /**
     * Initialises coordinate generation for the graph.
     */
    public void initialise();

    /**
     * Steps calculation of coordinates for the graph.
     */
    public void calculateStep();

    /**
     * Is calculation ready?
     *
     * @return
     */
    public boolean isReady();

    public ActionListener getInitialisationListener();

    public ActionListener getStepListener();

    public Requirement getStepChecker();
    
    public PropertyChangeListener getOptimisationListener();
    
    public double getOptimisation();
}

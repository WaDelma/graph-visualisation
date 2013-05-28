
package delma.graph.visualisation.UI;

import delma.graph.visualisation.Coordinates;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author aopkarja
 */
public class PanelMouseListener implements MouseMotionListener, MouseListener, MouseWheelListener {

    private final PanelGraphVisualisation panel;
    private Coordinates prev;

    public PanelMouseListener(PanelGraphVisualisation panel) {
        this.panel = panel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mask = MouseEvent.BUTTON1_DOWN_MASK;
        if ((e.getModifiersEx() & mask) == mask) {
            Coordinates cur = new Coordinates(e.getX(), e.getY());
            Coordinates focus = panel.getFocus();
            focus.move(Coordinates.diff(cur, prev));
            panel.getParent().repaint();
            prev = cur;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        prev = new Coordinates(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        panel.setScale(panel.getScale() + e.getPreciseWheelRotation() * Math.log(1.01 + panel.getScale()) * 0.5);
        if (panel.getScale() < 0) {
            panel.setScale(0);
        }
        panel.getParent().repaint();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.dequelist;

import java.util.Deque;
import java.util.List;

/**
 *
 * @author Antti
 */
public interface DequeList<E> extends List<E>, Deque<E> {

    /**
     * Amount of elements that can be stored without expanding storage.
     *
     * @return
     */
    public int capacity();
}

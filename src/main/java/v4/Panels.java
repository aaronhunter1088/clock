package v4;

/** An interface for the clock's panels. Each panel must
 * update the labels accordingly. This reduces the need
 * to create new labels. The panel also needs to make
 * sure it has updated itself accordingly.
 *
 * @author Michael Ball
 * @version 4
 */
public interface Panels {

    void updateLabels();
    void addComponentsToPanel();
}

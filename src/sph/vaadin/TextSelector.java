/**
 * 
 */
package sph.vaadin;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.AbstractTextField;

/**
 * Fired when a Field receives keyboard focus.
 * 
 * TextSelector selects the text from an AbstractTextField type object when the object is focused
 *
 * @author  Sami Holck
 * @author  Juha Mäkilä
 *
 */
public class TextSelector implements FieldEvents.FocusListener {

	/**
	 * version control in a Serializable class
	 */
	private static final long serialVersionUID = -6526848061882745023L;

	@Override
	public void focus(FocusEvent event) {
		AbstractTextField field = ((AbstractTextField) event.getComponent());
		field.selectAll();
	}
}

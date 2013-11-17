package sph.vaadin;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.ui.AbstractTextField;

/**
 * Can validate any <em>AbstractTextField</em> when the object is either blurred or the text content has changed.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 2.1
 * @since   11.09.2013
 * 
 */
public class TextFieldValidator implements FieldEvents.BlurListener, ValueChangeListener {

	private static final long serialVersionUID = -6526848061882745023L;

	/**
	 * Validates an <em>AbstractTextField</em> when the object is blurred.
	 *
	 * @param event the event fired.
	 * @see com.vaadin.event.FieldEvents.BlurListener#blur(com.vaadin.event.FieldEvents.BlurEvent)
	 */
	@Override
	public void blur(BlurEvent event) {
		try {
			AbstractTextField tf = ((AbstractTextField) event.getComponent());
			tf.isValid();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Validates an <em>AbstractTextField</em> when the value is changed.
	 *
	 * @param event the event fired.
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		try {
			AbstractTextField tf = (AbstractTextField) event.getProperty();
			tf.isValid();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

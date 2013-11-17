/**
 * <p><b>Copyright 2013 Juha Mäkilä and Sami Holck</b></p>
 * 
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at</p>
 * 
 *     <a href="http://www.apache.org/licenses/LICENSE-2.0"
 *     target="_new">http://www.apache.org/licenses/LICENSE-2.0</a>
 * 
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.</p>
 */
package sph.vaadin;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.ui.AbstractTextField;

/**
 * TextFieldTrimmer trims leading and trailing whitespaces from an <em>AbstractTextField</em> component.
 * 
 * <p><strong class="Red">NOTE:</strong> can be used as a blur listener or by static method
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 0.1
 * @since   15.11.2013
 */
public class TextFieldTrimmer implements FieldEvents.BlurListener {

	private static final long serialVersionUID = -6526848061882745023L;

	/**
	 * Trims leading and trailing whitespace omitted from an <em>AbstractTextField</em>'s
	 * value when the object is blurred.
	 *
	 * @param event the event fired.
	 * @see   com.vaadin.event.FieldEvents.BlurListener#blur(com.vaadin.event.FieldEvents.BlurEvent)
	 * @see  String#trim()
	 */
	@Override
	public void blur(BlurEvent event) {
		AbstractTextField tf = ((AbstractTextField) event.getComponent());
		TextFieldTrimmer.trim(tf);
	}

	/**
	 * Returns the given <em>AbstractTextField</em>, with leading and trailing whitespace omitted from its value.
	 * 
	 * @param tf the text field to trim.
	 * @return the same text field trimmed.
	 */
	public static AbstractTextField trim(AbstractTextField tf) {
		String newVal = tf.getValue().trim();
		tf.setValue(newVal);
		return tf;
	}

}

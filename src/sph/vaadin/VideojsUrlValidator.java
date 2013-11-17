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

/**
 * VideojsUrlValidator validates the given URL string for a {@link sph.vaadin.ui.videojs.Videojs Videojs} component.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.01
 * @since   12.10.2013
 *
 */
public class VideojsUrlValidator extends UrlValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7214704407965419958L;

	/**
	 * Constructs a validator with the default error message.
	 */
	public VideojsUrlValidator() {
		super("URL {0} is invalid for Videojs Component");
	}

	/**
	 * Constructs a validator with the given error message.
	 * 
	 * @param errorMessage the message to be included in an InvalidValueException
	 *        (with "{0}" replaced by the value that failed validation).
	 */
	public VideojsUrlValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected boolean isValidValue(String value) {
		if (super.isValidValue(value)) {
			String lcaseVal = value.toLowerCase();
			if (value.matches("http://www.youtube.com/(.*)") ||
					value.matches("http://vimeo.com/(.*)") ||
					value.matches("http://www.dailymotion.com/(.*)") ||
					lcaseVal.matches("(.*).mp4") ||
					lcaseVal.matches("(.*).ogg") ||
					lcaseVal.matches("(.*).ogv") ||
					lcaseVal.matches("(.*).webm")) {
				return true;
			} else {
				if (this.getErrorMessage() == null || this.getErrorMessage().equals("")) {
					this.setErrorMessage("The Stream type is not supprted");
				}
				return false;
			}
		} else {
			if (this.getErrorMessage() == null || this.getErrorMessage().equals("")) {
				this.setErrorMessage("URL {0} is invalid");
			}
			return false;
		}
	}
}

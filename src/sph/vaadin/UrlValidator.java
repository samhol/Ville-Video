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

import sph.UrlUtils;

import com.vaadin.data.validator.AbstractStringValidator;
/**
 * UrlValidator validates the given URL string.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  Sami Holck
 * @author  Juha Mäkilä
 * @version 1.0
 * @since   12.10.2013
 */
public class UrlValidator extends AbstractStringValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5880409124495467173L;

	/**
	 * Constructs a validator with the default error message.
	 */
	public UrlValidator() {
		super("URL {0} is invalid");
	}

	/**
	 * Constructs a validator with the given error message.
	 * 
	 * @param errorMessage the message to be included in an InvalidValueException
	 *        (with "{0}" replaced by the value that failed validation).
	 */
	public UrlValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected boolean isValidValue(String value) {
		if (value == null) {
			return false;
		}
		return UrlUtils.exists(value);
	}
}

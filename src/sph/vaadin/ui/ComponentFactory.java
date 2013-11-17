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
package sph.vaadin.ui;

import sph.vaadin.TextFieldValidator;
import sph.vaadin.TextSelector;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;

/**
 * ComponentFactory has some methods for basic component creation.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.0
 * @since   31.10.2013
 */
public class ComponentFactory {

	/**
	 * Creates a new image push Button an optional icon image.
	 * 
	 * @.post  <em>RESULT.getStyleNames().equals(com.vaadin.ui.themes.BaseTheme.BUTTON_LINK)</em>
	 * @param  description the description text of the image button.
	 * @param  pathToIcon the path to the icon image of the button.
	 * @param  enabled a boolean value specifying if the component should be enabled or not.
	 * @return the created component.
	 */
	public static Button createImageButton(String description, String pathToIcon, boolean enabled) {
		Button btn = new Button();
		btn.setDescription(description);
		btn.setIcon(new ThemeResource(pathToIcon));
		btn.setStyleName(BaseTheme.BUTTON_LINK);
		btn.setEnabled(enabled);
		return btn;
	}

	/**
	 * Creates a new image push Button an optional icon image.
	 * 
	 * @.post  <em>RESULT.getStyleNames().equals(com.vaadin.ui.themes.BaseTheme.BUTTON_LINK)</em>
	 * @param  caption button's caption String. Caption is the visible name of the button.
	 * @param  description the description text of the image button.
	 * @param  pathToIcon the path to the icon image of the button.
	 * @param  enabled a boolean value specifying if the component should be enabled or not.
	 * @return the created component.
	 */
	public static Button createImageButtonWithCaption(String caption, String description, String pathToIcon, boolean enabled) {
		Button btn = new Button(caption);
		btn.setDescription(description);
		btn.setIcon(new ThemeResource(pathToIcon));
		btn.setStyleName(BaseTheme.BUTTON_LINK);
		btn.setEnabled(enabled);
		return btn;
	}

	/**
	 * Creates a new blue push Button with an optional icon image.
	 * 
	 * @.post  <em>RESULT.getStyleNames().equals({@link SPH_Theme#BLUE_BUTTON})</em>
	 * @param  caption button's caption String. Caption is the visible name of the button.
	 * @param  enabled a boolean value specifying if the component should be enabled or not.
	 * @param  pathToIcon the path to the icon image of the button or null.
	 * @return the created component.
	 */
	public static Button createBlueButton(String caption, boolean enabled, String pathToIcon) {
		Button btn = new Button(caption);
		btn.setStyleName(SPH_Theme.BLUE_BUTTON);
		btn.setEnabled(enabled);
		if (pathToIcon != null) {
			btn.setIcon(new ThemeResource(pathToIcon));
		}
		return btn;
	}

	/**
	 * Creates a new large blue push Button with an optional icon image.
	 * 
	 * @.post  <em>RESULT.getStyleNames().equals({@link SPH_Theme#LARGE_BLUE_BUTTON})</em>
	 * @param  caption component's caption String. Caption is the visible name of the component.
	 * @param  enabled a boolean value specifying if the component should be enabled or not.
	 * @param  pathToIcon the path to the icon image of the button or null.
	 * @return the created component.
	 */
	public static Button createLargeBlueButton(String caption, boolean enabled, String pathToIcon) {
		Button btn = new Button(caption);
		btn.setStyleName(SPH_Theme.LARGE_BLUE_BUTTON);
		btn.setEnabled(enabled);
		if (pathToIcon != null) {
			btn.setIcon(new ThemeResource(pathToIcon));
		}
		return btn;
	}


	/**
	 * Creates a new small blue push Button with an optional icon image.
	 * 
	 * @.post  <em>RESULT.getStyleNames().equals({@link SPH_Theme#SMALL_BLUE_BUTTON})</em>
	 * @param  caption component's caption String. Caption is the visible name of the component.
	 * @param  enabled a boolean value specifying if the component should be enabled or not.
	 * @param  pathToIcon the path to the icon image of the button or null.
	 * @return the created component.
	 */
	public static Button createSmallBlueButton(String caption, boolean enabled, String pathToIcon) {
		Button btn = new Button(caption);
		btn.setStyleName(SPH_Theme.SMALL_BLUE_BUTTON);
		btn.setEnabled(enabled);
		if (pathToIcon != null) {
			btn.setIcon(new ThemeResource(pathToIcon));
		}
		return btn;
	}

	/**
	 * Creates a new TextField component.
	 * 
	 * @param  caption component's caption String. Caption is the visible name of the component.
	 * @param  prompt component's prompt String.
	 * @param  enabled a boolean value specifying if the component should be enabled or not.
	 * @return the created component.
	 */
	public static TextField createTextField(String caption, String prompt, boolean enabled) {
		TextField tf = new TextField();
		if (caption != null) {
			tf.setCaption(caption);
		}
		if (prompt != null) {
			tf.setInputPrompt(prompt);
		}
		tf.setEnabled(enabled);
		tf.addStyleName(SPH_Theme.TEXTFIELD_SMALL);
		tf.addValueChangeListener(new TextFieldValidator());
		tf.addFocusListener(new TextSelector());
		tf.setImmediate(true);
		return tf;
	}
}

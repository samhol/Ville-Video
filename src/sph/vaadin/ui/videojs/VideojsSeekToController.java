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
package sph.vaadin.ui.videojs;

import sph.Time;
import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.SPH_Theme;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * A controller for {@link sph.vaadin.ui.videojs.Videojs} component.
 * 
 * <p>VideojsSeekToController is an UI component for video position seeking.</p>
 *
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.1
 * @since   15.10.2013
 *
 */
public class VideojsSeekToController extends VideojsController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6189291102728553412L;
	/**
	 * text field for hours
	 */
	private TextField hoursField;
	/**
	 * text field for minutes
	 */
	private TextField minutesField;
	/**
	 * text field for seconds
	 */
	private TextField secondsField;
	/**
	 * seek button
	 */
	private final Button seekBtn = ComponentFactory.createImageButton("Seek to the given time", SPH_Theme.MAGNIFIER_ICON_16PX, true);
	/**
	 * @param vjs
	 */
	public VideojsSeekToController(Videojs vjs) {
		super(vjs);
	}

	/**
	 * Creates a new text field component.
	 * 
	 * @param  prompt component's prompt String.
	 * @param  columns number of columns in component.
	 * @return created text field component.
	 */
	private TextField createTextField(String prompt, int columns) {
		TextField tf = ComponentFactory.createTextField("", prompt, true);
		tf.setColumns(2);
		tf.setConverter(new StringToIntegerConverter());
		tf.setNullRepresentation("");
		return tf;
	}

	/**
	 * Creates and sets up all UI components.
	 */
	private void doLayout() {
		HorizontalLayout compositionRoot = new HorizontalLayout();
		compositionRoot.setMargin(true);
		compositionRoot.setSpacing(true);
		compositionRoot.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
		hoursField = createTextField("00", 2);
		minutesField = createTextField("00", 2);
		secondsField = createTextField("00", 2);
		//hoursField.addValidator(new IntegerRangeValidator("Virheellinen aika tunnit", 0, 2));
		minutesField.addValidator(new IntegerRangeValidator("Requires an integer between 0-59", 0, 59));
		secondsField.addValidator(new IntegerRangeValidator("Requires an integer between 0-59", 0, 59));
		compositionRoot.addComponents(hoursField, new Label(":"), minutesField, new Label(":"), secondsField, seekBtn);
		compositionRoot.setCaption("Seekto");
		this.setCompositionRoot(compositionRoot);
	}

	/**
	 * Returns the time value of the fields.
	 * 
	 * @return the time value of the fields or null if the fields are empty.
	 */
	public Time parseTime() {
		try {
			Time t = new Time();
			if (hoursField.getValue() == null) {
				t.setHours(0);
			} else {
				t.setHours(Integer.parseInt(hoursField.getValue()));
			}
			if (minutesField.getValue() == null) {
				t.setMinutes(0);
			} else {
				t.setMinutes(Integer.parseInt(minutesField.getValue()));
			}
			if (secondsField.getValue() == null) {
				t.setSeconds(0);
			} else {
				t.setSeconds(Integer.parseInt(secondsField.getValue()));
			}
			if (t.getTime() == this.getPlayer().getCurrentTime() || t.getTime() > this.getPlayer().getDuration()) {
				return null;
			}
			return t;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see sph.vaadin.ui.videojs.VideojsPlayerController#process()
	 */
	@Override
	public void setup() {
		this.doLayout();
		this.seekBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2342681885487438013L;

			@Override
			public void buttonClick(ClickEvent event) {
				Time seekToTime = parseTime();
				if (seekToTime != null) {
					System.out.println(seekToTime.getTime());
					getPlayer().seekTo(seekToTime.getTime());
				}
			}
		});
	}

}

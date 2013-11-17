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

import sph.vaadin.TextFieldValidator;
import sph.vaadin.TextSelector;
import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.SPH_Theme;

import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * A {@link VideojsController} for {@link Videojs} component.
 * 
 * <p>VideojsTimeJumper makes it possible for an user to traverse the video stream back and forth.</p>
 *
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.1
 * @since   15.10.2013
 * 
 */
public class VideojsTimeJumper extends VideojsController {

	private static final long serialVersionUID = -880640900527478589L;

	private final HorizontalLayout compositionRoot = new HorizontalLayout();

	private final TextField secondsField = new TextField("Movement");

	/**
	 * forward button
	 */
	private final Button forward = ComponentFactory.createImageButton("Forward", SPH_Theme.FORWARD_ICON_20PX, true);
	/**
	 * rewind button
	 */
	private final Button rewind = ComponentFactory.createImageButton("Rewind", SPH_Theme.REWIND_ICON_20PX, true);

	/**
	 * Constructs a new time jump controller for the given {@link Videojs} component.
	 * 
	 * @param target the target VideojsPlayer for the control
	 */
	public VideojsTimeJumper(final Videojs target) {
		super(target);
		// this.target = target;
	}

	private void doLayout() {
		this.compositionRoot.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
		this.compositionRoot.setMargin(true);
		this.compositionRoot.setSpacing(true);
		this.secondsField.setInputPrompt("1 second");
		this.secondsField.addStyleName(SPH_Theme.TEXTFIELD_SMALL);
		this.secondsField.setColumns(8);
		this.secondsField.setConverter(new StringToIntegerConverter());
		this.secondsField.setConverter(new StringToDoubleConverter());
		this.secondsField.addFocusListener(new TextSelector());
		this.secondsField.addBlurListener(new TextFieldValidator());
		this.secondsField.setNullRepresentation("");
		this.secondsField.setConversionError("A decimal number required");
		this.rewind.setDescription("Rewind the video");
		this.forward.setDescription("Forward the video");
		this.compositionRoot.addComponents(rewind, secondsField, forward);
		this.setCompositionRoot(this.compositionRoot);
	}

	/**
	 * 
	 * @param displacement the displacement in seconds.
	 */
	private void seekTo(double displacement) {
		double currentTime = this.getPlayer().getCurrentTime();
		double seekToTime = currentTime + displacement;
		if (seekToTime >= 0) {
			this.getPlayer().seekTo(seekToTime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sph.vaadin.ui.videojs.VideojsPlayerController#process()
	 */
	@Override
	public void setup() {
		this.doLayout();
		this.rewind.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 397704121930396501L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					double displacement = -(Double) secondsField.getConvertedValue();
					seekTo(displacement);
				} catch (Exception e) {
					if (secondsField.getValue() == null) {
						seekTo(-1);
					}
				}
			}
		});
		this.forward.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1740751201298255782L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					double displacement = (Double) secondsField.getConvertedValue();
					seekTo(displacement);
				} catch (Exception e) {
					if (secondsField.getValue() == null) {
						seekTo(1);
					}
				}
			}
		});

	}

}
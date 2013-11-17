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
import sph.vaadin.VideojsUrlValidator;
import sph.vaadin.ui.ComponentFactory;
import sph.vaadin.ui.SPH_Theme;

import com.vaadin.server.Page;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

/**
 * A controller for {@link sph.vaadin.ui.videojs.Videojs#setSource(String, String)} component.
 * 
 * <p>VideojsSourceLoader is an UI component where an user can set the source of
 * the {@link sph.vaadin.ui.videojs.Videojs#setSource(String, String)} component.</p>
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author Juha Mäkilä
 * @version v0.1
 * @since 15.10.2013
 * 
 */
public class VideojsSourceLoader extends VideojsController {

	/**
	 * Constant serialized ID used for compatibility.
	 */
	private static final long serialVersionUID = -1571516620549983694L;
	/**
	 * the composition root for the VideojsSourceLoader component
	 */
	private final HorizontalLayout compositionRoot = new HorizontalLayout();
	/**
	 * load button
	 */
	private final Button loadBtn = ComponentFactory.createSmallBlueButton("Load", true, SPH_Theme.LOAD_ICON_16PX);
	/**
	 * video stream URL field
	 */
	private final TextField videoUrlField = ComponentFactory.createTextField("URL:", "The URL of the video", true);

	/**
	 * @param player
	 */
	public VideojsSourceLoader(Videojs player) {
		super(player);
	}

	/**
	 * Sets the path to the video stream and the MIME type of the video stream
	 * 
	 * @param videoSrc the source path of the video file
	 * @param mimeType the MIME type of the video
	 * @see   sph.vaadin.ui.videojs.Videojs#setSource(String, String)
	 */
	public void setVideoSource(final String videoSrc, final String mimeType) {
		this.videoUrlField.setValue(videoSrc);
		this.getPlayer().setSource(videoSrc, mimeType);
	}

	/**
	 * Builds the loader part of the Video component
	 */
	private void doLayout() {
		this.videoUrlField.setInputPrompt("The URL of the video");
		this.videoUrlField.addValidator(new VideojsUrlValidator());
		this.videoUrlField.setNullRepresentation("");
		this.videoUrlField.setWidth("300px");
		this.compositionRoot.setSpacing(true);
		this.compositionRoot.addComponents(videoUrlField, loadBtn);
		this.compositionRoot.setComponentAlignment(videoUrlField, Alignment.BOTTOM_CENTER);
		this.compositionRoot.setComponentAlignment(loadBtn, Alignment.BOTTOM_CENTER);
		this.compositionRoot.setMargin(true);
		this.setCompositionRoot(compositionRoot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sph.vaadin.ui.videojs.VideojsPlayerController#process()
	 */
	@Override
	public void setup() {
		this.doLayout();
		this.videoUrlField.setImmediate(true);
		this.videoUrlField.addValueChangeListener(new TextFieldValidator());
		this.videoUrlField.addFocusListener(new TextSelector());
		this.videoUrlField.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
		this.loadBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 402515738995537894L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (videoUrlField.isValid()) {
					getPlayer().setSource(videoUrlField.getValue(), parseMIME());
				} else {
					Notification n = new Notification("Loading failed!",
							"<strong>Video URL:</strong> is not recognized as a valid video stream!",
							Notification.Type.WARNING_MESSAGE, true);
					n.show(Page.getCurrent());
				}
			}
		});
	}

	/**
	 * Returns the MIME type of the video stream URL if recognized.
	 * 
	 * @return the MIME type of the video stream URL if recognized.
	 * @see    sph.vaadin.ui.videojs.Videojs#setSource(String, String)
	 */
	private String parseMIME() {
		String src = videoUrlField.getValue();
		String lcaseVal = src.toLowerCase();
		if (src.matches("http://www.youtube.com/(.*)")) {
			return Videojs.YOUTUBE_MIME;
		}
		if (src.matches("http://vimeo.com/(.*)")) {
			return Videojs.VIMEO_MIME;
		}
		/*if (src.matches("http://www.dailymotion.com/(.*)")) {
			return Videojs.DAILYMOTION_MIME;
		}*/
		if (lcaseVal.matches("(.*).mp4")) {
			return Videojs.MP4_MIME;
		}
		if (lcaseVal.matches("(.*).ogg") || lcaseVal.matches("(.*).ogv")) {
			return Videojs.OGG_MIME;
		}
		if (lcaseVal.matches("(.*).webm")) {
			return Videojs.WEBM_MIME;
		}
		return null;
	}
}

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

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * A container for <b>{@link sph.vaadin.ui.videojs.Videojs}</b>
 * component and its basic controllers <b>{@link sph.vaadin.ui.videojs.VideojsSourceLoader}</b>,
 * <b>{@link sph.vaadin.ui.videojs.VideojsSeekToController}</b>
 * and <b>{@link sph.vaadin.ui.videojs.VideojsTimeJumper}</b>.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.0
 * @since   11.10.2013
 * 
 */
public class VideojsFrame extends CustomComponent {

	/**
	 * version control in a Serializable class
	 */
	private static final long serialVersionUID = 1832295496823246699L;
	/**
	 * the composition root for the VideoFrame component
	 */
	private final VerticalLayout compositionRoot = new VerticalLayout();
	/**
	 * the bottom container for the VideoFrame component
	 */
	private final HorizontalLayout controlsLayout = new HorizontalLayout();
	/**
	 * Video.js component
	 */
	private final Videojs player = new Videojs();
	/**
	 * the video source loader for the VideoFrame component
	 */
	private final VideojsSourceLoader vjsSL;

	private final VideojsTimeJumper vjsDC;
	private final VideojsSeekToController vjsSTC;

	/**
	 * 
	 */
	public VideojsFrame() {
		this("", "");
	}

	/**
	 * 
	 * @param videoSrc the source path of the video file
	 * @param mimeType the MIME type of the video
	 */
	public VideojsFrame(final String videoSrc, final String mimeType) {
		this.vjsSL = new VideojsSourceLoader(this.player);
		this.vjsSL.setup();
		this.vjsDC = new VideojsTimeJumper(this.player);
		this.vjsDC.setup();
		this.vjsSTC = new VideojsSeekToController(this.player);
		this.vjsSTC.setup();
		this.doLayout();
		this.vjsSL.setVideoSource(videoSrc, mimeType);
	}

	/**
	 * Sets the path to the video stream and the MIME type of the video stream
	 * 
	 * @param videoSrc the source path of the video file
	 * @param mimeType the MIME type of the video
	 */
	public void setVideoSource(final String videoSrc, final String mimeType) {
		this.vjsSL.setVideoSource(videoSrc, mimeType);
	}

	/**
	 * Sets the visibility of the Loader component.
	 * 
	 * <p>
	 * Visible components are drawn in the user interface, while invisible ones
	 * are not. The effect is not merely a cosmetic CSS change - no information
	 * about an invisible component will be sent to the client. The effect is
	 * thus the same as removing the component from its parent.
	 * </p>
	 * 
	 * <p>
	 * A component is visible only if all of its parents are also visible. If a
	 * component is explicitly set to be invisible, changes in the visibility of
	 * its parents will not change the visibility of the component.
	 * </p>
	 * 
	 * @param visible the boolean value specifying if the loader component
	 *            should be visible after the call or not.
	 */
	public void setLoaderVisible(boolean visible) {
		this.vjsSL.setVisible(visible);
	}

	/**
	 * Sets the visibility of the VideoStreamControls component.
	 * 
	 * <p>
	 * Visible components are drawn in the user interface, while invisible ones
	 * are not. The effect is not merely a cosmetic CSS change - no information
	 * about an invisible component will be sent to the client. The effect is
	 * thus the same as removing the component from its parent.
	 * </p>
	 * 
	 * <p>
	 * A component is visible only if all of its parents are also visible. If a
	 * component is explicitly set to be invisible, changes in the visibility of
	 * its parents will not change the visibility of the component.
	 * </p>
	 * 
	 * @param visible the boolean value specifying if the VideoStreamControls
	 *            component should be visible after the call or not.
	 */
	public void setVideoStreamControlsVisible(boolean visible) {
		this.vjsDC.setVisible(visible);
		this.vjsSTC.setVisible(visible);
	}

	/**
	 * <p>
	 * Enables or disables the Loader. The user can not interact disabled
	 * components, which are shown with a style that indicates the status,
	 * usually shaded in light gray color. Components are enabled by default.
	 * </p>
	 * 
	 * @param enabled a boolean value specifying if the Loader should be enabled
	 *            or not
	 */
	public void setLoaderEnabled(boolean enabled) {
		this.vjsSL.setEnabled(enabled);
	}

	/**
	 * <p>
	 * Enables or disables the component. The user can not interact disabled
	 * components, which are shown with a style that indicates the status,
	 * usually shaded in light gray color. Components are enabled by default.
	 * </p>
	 * 
	 * @param enabled a boolean value specifying if the component should be
	 *            enabled or not
	 */
	public void setVideoStreamControlsEnabled(boolean enabled) {
		this.vjsDC.setEnabled(enabled);
		this.vjsSTC.setEnabled(enabled);
	}

	/**
	 * Returns the source path of the video stream.
	 * 
	 * @return the source path of the video stream.
	 */
	public String getVideoSrc() {
		return this.player.getVideoSrc();
	}

	/**
	 * Returns the MIME Type of the video stream.
	 * 
	 * @return the MIME type of the video stream.
	 */
	public String getMimeType() {
		return this.player.getMimeType();
	}

	private void doLayout() {
		this.controlsLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
		this.controlsLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
		this.controlsLayout.addComponents(this.vjsSL, this.vjsSTC, this.vjsDC);
		//this.compositionRoot.addComponent(this.controlsLayout);
		this.compositionRoot.addComponents(this.controlsLayout, this.player);
		this.setCompositionRoot(this.compositionRoot);
	}

	/**
	 * Returns the Video.js player component.
	 * 
	 * @return the Video.js player component.
	 */
	public Videojs getVideojsPlayer() {
		return this.player;
	}
}

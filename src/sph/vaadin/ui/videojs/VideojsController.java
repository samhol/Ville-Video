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

import com.vaadin.ui.CustomComponent;

/**
 * <b>VideojsPlayerController</b> is a base class for all <b>{@link sph.vaadin.ui.videojs.Videojs}</b>
 * UI control components.
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version v0.1
 * @since   15.10.2013
 *
 */
public abstract class VideojsController extends CustomComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 213610985331044588L;
	/**
	 * controlled VideojsPlayer
	 */
	private Videojs player;

	/**
	 * 
	 * @param player the controlled video stream player.
	 */
	public VideojsController(final Videojs player) {
		super();
		this.setPlayer(player);
	}

	/**
	 * Returns the controlled VideojsPlayer.
	 * 
	 * @return the currently controlled player
	 */
	public final Videojs getPlayer() {
		return player;
	}

	/**
	 * Sets the controlled VideojsPlayer.
	 *
	 * @param player the controlled player to set
	 */
	public void setPlayer(final Videojs player) {
		this.player = player;
	}

	/**
	 * Sets the controller ready.
	 */
	public abstract void setup();

}

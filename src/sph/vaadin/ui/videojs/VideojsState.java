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

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * <strong>VideojsState object</strong> is a shared state object for <strong>
 * {@link VideojsState}</strong> component.
 * 
 * <p>The basic communication from a server-side VideojsPlayer to its the
 * client-side widget counterpart is handled using a VideojsPlayerState. The
 * VideojsPlayerState is serialized transparently. VideojsPlayerState should be
 * considered read-only on the client-side, as VideojsPlayerState is not
 * serialized back to the server-side.</p>
 * 
 * <p>Values in the Shared State and in RPC calls are converted between Java and
 * JavaScript using the following conventions:</p>
 * 
 * <ul>
 * <li>Primitive Java numbers (<em>byte, char, int, long, float, double</em>) and their
 * boxed types (<em>Byte, Character, Integer, Long, Float, Double</em>) are represented
 * by JavaScript numbers.</li>
 * <li>The primitive Java <em>boolean</em> and the boxed <em>Boolean</em> are represented by
 * JavaScript booleans.</li>
 * <li>Java Strings are represented by JavaScript strings.</li>
 * <li>List, Set and all arrays in Java are represented by JavaScript arrays.</li>
 * <li>Map<string, ?=""> in Java is represented by JavaScript object with fields
 * corresponding to the map keys.</string,></li>
 * <li>Any other Java Map is represented by a JavaScript array containing two
 * arrays, the first contains the keys and the second contains the values in the
 * same order.</li>
 * <li>A Java Bean is represented by a JavaScript object with fields
 * corresponding to the bean's properties.</li>
 * <li>A Java Connector is represented by a JavaScript string containing the
 * connector's id.</li>
 * <li>A plugable serialization mechanism is provided for types not described
 * here. Please refer to the documentation for specific types for serialization
 * information.</li>
 * </ul>
 * 
 *
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 2.1
 * @since   11.09.2013
 */
public class VideojsState extends JavaScriptComponentState {

	/**
	 * version control in a Serializable class
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the unique id of the Video.js component
	 */
	public String videoJsId;
	/**
	 * the path to the video as an URL string
	 */
	public String videoSrc;
	/**
	 * the mime type of the video
	 */
	public String mimetype;
	/**
	 * <strong>{@link Videojs} server to Video.js client action flag:</strong>
	 * <p>
	 * Indicates that a player dimension change action is required from a server-side {@link Videojs}
	 * to its the client-side widget counterpart.
	 * </p>
	 */
	/**
	 * the width of the Video.js component
	 */
	public int vjsWidth;
	/**
	 * the height of the Video.js component
	 */
	public int vjsHeight;
	/**
	 * Video.js component masking flag (true: video component is masked, false:
	 * video component is unmasked)
	 */
	public boolean isMasked;
	/**
	 * Video.js component mask label text
	 */
	public String maskLabel;
	/**
	 * Delay in milliseconds before Video.js component is masked
	 */
	public int maskDelay;
	/**
	 * Videojs component flag for video stream position seeking (true: seeking
	 * enabled, false: seeking disabled)
	 */
	public boolean seekingEnabled;
}
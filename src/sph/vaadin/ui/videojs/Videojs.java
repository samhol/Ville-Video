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

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;

import sph.event.BasicEventManager;
import sph.event.EventListener;

import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;

import edu.vserver.exercises.videoMcq.QuestionWindow;

/**
 * <p><strong>Videojs component </strong> is based on <a href="http://www.videojs.com/"
 * target="new" class="sph bold code">Video.js</a> video player.<a
 * href="http://www.videojs.com/" target="new" class="sph bold code">Video.js</a> is a free open source HTML5 video player. </p>
 * <p><strong>Videojs component</strong> supports following media types / sources: </p>
 * <ul>
 *   <li><a href="http://www.youtube.com/" target="new" class="sph red bold code">youtube</a></li>
 *   <li><span class="sph red bold">vimeo</span></li>
 *   <li><span>Native support from <code>HTML&lt;video&gt;</code> and <strong>Videojs</strong>:</span></li>
 *  <ul>
 *  <li><span class="sph red bold">video/ogg</span><span class="summary"> = Ogg files with Theora video codec and Vorbis audio codec</span></li>
 *  <ul>
 *    <li class="Red">*.ogg</li>
 *    <li class="Red">*.ogv</li>
 *  </ul>
 *  <li><span class="sph red bold">video/mp4</span><span class="summary"> = MPEG 4 files with H264 video codec and AAC audio codec</span></li>
 *  <ul>
 *    <li class="Red">*.mp4</li>
 *  </ul>
 *  <li><span class="sph red bold">video/webm</span></li>
 *</ul>
 *</ul>
 *
 *<p><span class="Red strong">IMPORTANT!:</span> Video format support depends highly on the used Web Browser, Operating system...</p>
 *<p> Links to <strong>Video.js</strong> resources: </p>
 *<ul>
 *  <li>Video.js: <a href="http://www.videojs.com/" target="new" class="sph bold">http://www.videojs.com/</a></li>
 *  <ul>
 *    <li>Video.js Github page: <a href="https://github.com/videojs" target="new" class="sph bold">https://github.com/videojs</a></li>
 *    <li>USED Video.js PLUGINS:</li>
 *    <ul>
 *      <li><a href="https://github.com/eXon/videojs-youtube" target="new">videojs-youtube</a>: Allows you to play YouTube videos within Videojs.</li>
 *      <li><a href="https://github.com/eXon/videojs-vimeo" target="new">videojs-vimeo</a>: Allows you to play Vimeo videos within VideoJS.</li>
 *    </ul>
 *  </ul>
 *</ul>
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 2.1
 * @since   11.09.2013
 * 
 */
@com.vaadin.annotations.JavaScript({
	"javascript/jquery-1.10.2.min.js",
	"javascript/underscore-min.js",
	"javascript/video.js",
	"javascript/vjs.youtube.js",
	"javascript/vjs.vimeo.js",
	/*"javascript/vjs.dailymotion.js",*/
	"javascript/jquery.loadmask.min.js",
"javascript/Videojs.js" })
public class Videojs extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 6419316253428765025L;
	/**
	 * MIME type for Youtube video sources {@link "https://www.youtube.com/"}
	 */
	public static final String YOUTUBE_MIME = "youtube";
	/**
	 * MIME type for Vimeo video sources {@link "https://vimeo.com/"}
	 */
	public static final String VIMEO_MIME = "vimeo";
	/**
	 * MIME type for Dailymotion video sources -
	 * {@link "https://dailymotion.com/"}
	 */
	//public static final String DAILYMOTION_MIME = "dailymotion";
	/**
	 * MIME type for *.ogg video sources (Ogg files with Theora video codec and Vorbis audio codec)
	 */
	public static final String OGG_MIME = "video/ogg";
	/**
	 * MIME type for *.mp4 video sources (MPEG 4 files with H264 video codec and AAC audio codec)
	 */
	public static final String MP4_MIME = "video/mp4";
	/**
	 * MIME type for *.webm video sources (WebM files with VP8 video codec and Vorbis audio codec)
	 */
	public static final String WEBM_MIME = "video/webm";
	/**
	 * the source path of the video file
	 */
	private String videoSrc;
	/**
	 * the type of the video (MIME type)
	 */
	private String mimeType;
	/**
	 * the DOM-id of the Video.js component
	 */
	private final String videoJsId;

	private double currentTime = 0.0;

	private double duration = 0.0;

	private final SortedSet<Double> pauseAts = new TreeSet<Double>();

	private boolean isMasked;

	private boolean isPaused = true;


	/**
	 * a Videojs EventManager
	 */
	private final BasicEventManager<String, Videojs, Double, VjsListener> videojsEventManager = new BasicEventManager<String, Videojs, Double, VjsListener>();

	/**
	 * Constructs an empty video player.
	 */
	public Videojs() {
		this("", "");
	}

	/**
	 * Constructs a video player with the given video stream source.
	 * 
	 * @param videoSrc the URL of the video stream.
	 * @param mimeType the MIME type of the video stream.
	 * @see   #setSource(String, String)
	 */
	public Videojs(final String videoSrc, String mimeType) {
		this.setSource(videoSrc, mimeType);
		this.init();
		this.setDefaults();
		this.videoJsId = "VideoJS_" + this.hashCode();
		getState().videoJsId = this.videoJsId;
	}

	protected void setDefaults() {
		this.setDimensions(768, 432);
		getState().seekingEnabled = true;
	}

	/**
	 * Sets the initial properties of the Video.js player
	 */
	protected void init() {
		this.addFunction("videoEventFired", new JavaScriptFunction() {

			private static final long serialVersionUID = -4245967454493315292L;

			@Override
			public void call(JSONArray arguments) throws JSONException {
				try {
					if (arguments.length() > 1) {
						currentTime = arguments.getDouble(1);
						String eventName = arguments.getString(0);
						double triggerTime = arguments.getDouble(1);
						//System.out.println(eventName + ":" + triggerTime);
						if (eventName.equals(VjsListener.DURATIONCHANGE_EVENT) && arguments.length() > 2) {
							duration = arguments.getDouble(2);
							//System.out.println("duration: " + duration);
						}
						if (eventName.equals(VjsListener.PLAY_EVENT)) {
							isPaused = false;
						}
						if (eventName.equals(VjsListener.PAUSE_EVENT)) {
							isPaused = true;
						}
						videojsEventManager.callListeners(VjsListener.ANY_EVENT, Videojs.this, triggerTime);
						videojsEventManager.callListeners(eventName, Videojs.this, triggerTime);
					}
				} catch (JSONException e) {
					// We'll log in the console, you might not want to
					JavaScript.getCurrent().execute(
							"console.error('" + e.getMessage() + "')");
					System.out.println(e.getMessage());
				}
			}
		});
		addDetachListener(new DetachListener() {

			private static final long serialVersionUID = 5583909485994600778L;

			@Override
			public void detach(DetachEvent event) {
				videojsEventManager.clear();
				JavaScript.getCurrent().execute("videojs('" + videoJsId +"').dispose();");
			}
		});
	}

	@Override
	protected VideojsState getState() {
		return (VideojsState) super.getState();
	}

	/**
	 * Adds a new {@link VjsListener} listener into the {@link QuestionWindow} component.
	 * 
	 * @param eventName The {@link VjsListener} event the listener will listen to.
	 * @param listener The event listener object itself.
	 */
	public void addVideojsListener(String eventName, VjsListener listener) {
		this.videojsEventManager.addListener(eventName, listener);
	}

	/**
	 * Remove listener from a specific {@link VjsListener} event.
	 * 
	 * @param eventName the name of the {@link VjsListener} event.
	 * @param listener {@link VjsListener} Event listener to remove.
	 */
	public void removeVideojsListener(String eventName, VjsListener listener) {
		this.videojsEventManager.removeListener(eventName, listener);
	}

	/**
	 * Remove listener from all {@link VjsListener} events it is registered
	 * by. Convenient way of cleaning up an listener object being destroyed.
	 * 
	 * @param listener {@link VjsListener} Event listener to remove.
	 */
	public void removeVideojsListener(VjsListener listener) {
		this.videojsEventManager.removeListener(listener);
	}

	/**
	 * Sets the video source and the video type
	 * 
	 * <p><strong>Videojs component</strong> supports following media types / sources: </p>
	 * <ul>
	 *   <li><a href="http://www.youtube.com/" target="new" class="sph red bold code">youtube</a></li>
	 *   <li><span class="sph red bold">vimeo</span></li>
	 *   <li><span>Native support from <code>HTML&lt;video&gt;</code> and <strong>Videojs</strong>:</span></li>
	 *   <ul>
	 *     <li><span class="sph red bold">video/ogg</span><span class="summary"> = Ogg files with Theora video codec and Vorbis audio codec</span></li>
	 *    <ul>
	 *      <li class="Red">*.ogg</li>
	 *     <li class="Red">*.ogv</li>
	 *   </ul>
	 *    <li><span class="sph red bold">video/mp4</span><span class="summary"> = MPEG 4 files with H264 video codec and AAC audio codec</span></li>
	 *    <ul>
	 *       <li class="Red">*.mp4</li>
	 *     </ul>
	 *     <li><span class="sph red bold">video/webm</span></li>
	 *   </ul>
	 * </ul>
	 * 
	 * @param videoSrc the URL of the video stream.
	 * @param mimeType the MIME type of the video stream.
	 */
	public void setSource(final String videoSrc, String mimeType) {
		this.videoSrc = videoSrc;
		this.mimeType = mimeType;
		getState().videoSrc = videoSrc;
		getState().mimetype = mimeType;
	}

	/**
	 * Returns the source of the video stream.
	 * 
	 * @return the source of the video stream.
	 */
	public String getVideoSrc() {
		return this.videoSrc;
	}

	/**
	 * Returns the MIME Type of the video stream.
	 * 
	 * @return the MIME type of the video stream.
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * Plays the video.
	 */
	public void play() {
		callFunction("play");
	}

	/**
	 * Pauses the video
	 */
	public void pause() {
		callFunction("pause");
	}

	/**
	 * Returns the
	 *
	 * @return the isPaused
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * Seeks to the supplied time in video (seconds).
	 * 
	 * @param time The new sought time of the video in seconds.
	 */
	public <T extends Number> void seekTo(T time) {
		callFunction("seekTo", time);
	}

	/**
	 * Pauses the video play back on the given time.
	 * 
	 * <p>
	 * <b>NOTE: </b> this method replaces all previously defined pause times.
	 * </p>
	 * 
	 * @param time Pausing time.
	 */
	public <T extends Number> void pauseAt(T time) {
		ArrayList<T> times = new ArrayList<T>();
		times.add(time);
		this.pauseAt(times);
	}

	/**
	 * Pauses the video play back on the given times.
	 * 
	 * <p>
	 * <b>NOTE: </b> this method replaces all previously defined pause times.
	 * </p>
	 * 
	 * @param times Pausing times.
	 */
	public <T extends Number> void pauseAt(Collection<T> times) {
		this.pauseAts.clear();
		if (times != null) {
			for(T time: times) {
				this.pauseAts.add(time.doubleValue());
			}
		}
		callFunction("pauseAt", this.pauseAts);
	}

	/**
	 * Removes an predefined pause time from the player.
	 * 
	 * @param time removed pausing time.
	 */
	public <T extends Number> void removePauseAtTime(T time) {
		if (this.pauseAts.remove(time.doubleValue())) {
			callFunction("pauseAt", this.pauseAts);
		}
	}

	/**
	 * Clears all predefined pause times from the player.
	 */
	public void clearPauseAtList() {
		this.pauseAts.clear();
		callFunction("pauseAt", this.pauseAts);
	}

	/**
	 * Returns the current time of the video in seconds.
	 * 
	 * @return The current time of the video in seconds.
	 */
	public double getCurrentTime() {
		return this.currentTime;
	}

	/**
	 * Returns the length of the video in seconds.
	 * 
	 * <p>
	 * <b>NOTE:</b> The video must have started loading before the duration can
	 * be known, and in the case of Flash, may not be known until the video
	 * starts playing. Therefore this function is not fully reliable!
	 * </p>
	 * 
	 * @return the length of the video in seconds.
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * Changes the width and height of the Video.js player to the supplied width and height.
	 * 
	 * @param width The width in pixels
	 * @param height The height in pixels
	 */
	public void setDimensions(int width, int height) {
		this.callFunction("setDimensions", width, height);
		getState().vjsWidth = width;
		getState().vjsHeight = height;
		this.setHeight(height, Unit.PIXELS);
		this.setWidth(width, Unit.PIXELS);
	}

	/**
	 * Specifies whether this {@link Videojs} player is set to full window mode.
	 * 
	 * @param fullWindow specifies whether this {@link Videojs} player is set to full window mode.
	 */
	public void fullWindowEnabled(boolean fullWindow) {
		this.callFunction("setFullWindow", fullWindow);
	}

	/**
	 * Specifies whether this {@link Videojs} player supports full screen mode.
	 * 
	 * @param supported specifies whether this {@link Videojs} player supports full screen mode.
	 */
	public void supportFullscreenMode(boolean supported) {
		//this.fullWindow = fullWindow;
		this.callFunction("supportFullscreenMode", supported);
	}

	/**
	 * Specifies whether this {@link Videojs} player has minimal user interface controls.
	 * 
	 * <p><strong class="Red">NOTE: </strong> Minimal user interface controls are all on player's controls panel:</p>
	 * <ul>
	 * <li>Play/Pause button</li>
	 * <li>Volume slider</li>
	 * <li>Volume muting button</li>
	 * </ul>
	 * <p>...and hidden controls are:</p>
	 * <ul>
	 * <li>Big play button</li>
	 * <li>Progress bar with all of its functionality</li>
	 * </ul>
	 * 
	 * @param minimalUserControls specifies whether this {@link Videojs} player has minimal controls.
	 */
	public void minimalUserControlsEnabled(boolean minimalUserControls) {
		this.callFunction("minimalUserControlsEnabled", minimalUserControls);
	}

	/**
	 * Displays a mask over this {@link Videojs} player element. Only the mask will be displayed without a label
	 */
	public void mask() {
		this.mask(null);
	}

	/**
	 * Displays a mask over this {@link Videojs} player element.
	 * 
	 * @param label text message that will be displayed on top of the mask  besides an info icon.
	 */
	public void mask(String label) {
		this.mask(label, 0);
	}

	/**
	 * Displays a mask over this {@link Videojs} player element.
	 * 
	 * @param label Text message that will be displayed on top of the mask besides an info icon.
	 * @param delay Delay in milliseconds before player element is masked. If
	 *            {@link #unmask()} is called before the delay times out, no
	 *            mask is displayed. This can be used to prevent unnecessary
	 *            mask display for quick processes.
	 */
	public void mask(String label, int delay) {
		this.callFunction("mask", label, delay);
		this.isMasked = true;
		getState().isMasked = true;
		getState().maskLabel = label;
	}

	/**
	 * Removes the mask from this {@link Videojs} player element.
	 */
	public void unmask() {
		this.callFunction("unmask");
		this.isMasked = false;
		getState().isMasked = false;
		getState().maskLabel = "";
	}

	/**
	 * Checks if this {@link Videojs} player is masked.
	 * 
	 * @return false if mask is delayed or not displayed, true otherwise.
	 */
	public boolean isMasked() {
		return this.isMasked;
	}

	/**
	 * {@link sph.event.EventListener} interface for listening to client side Video.js Events
	 * delegated to and fired by a {@link Videojs} player component.
	 * 
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 2.01
	 * @since   11.10.2013
	 */
	public interface VjsListener extends EventListener<String, Videojs, Double> {

		/**
		 * loadstart event (Fired when the user agent begins looking for media data)
		 */
		public static final String LOADSTART_EVENT = "loadstart";
		/**
		 * loadedmetadata event (Fired when the player has initial duration and dimension information)
		 */
		public static final String LOADEDMETADATA_EVENT = "loadedmetadata";
		/**
		 * loadeddata event (Fired when the player has downloaded data at the current playback position)
		 */
		public static final String LOADEDDATA_EVENT = "loadeddata";
		/**
		 * loadedalldata event (Fired when the player has finished downloading the source data)
		 */
		public static final String LOADEDALLDATA_EVENT = "loadedalldata";
		/**
		 * play event (Fired whenever the media begins or resumes playback)
		 */
		public static final String PLAY_EVENT = "play";
		/**
		 * pause event (Fired whenever the media has been paused)
		 */
		public static final String PAUSE_EVENT = "pause";
		/**
		 * timeupdate event (Fired when the current playback position has
		 * changed. During playback this is fired every 15-250 milliseconds,
		 * depending on the playback technology in use.)
		 */
		public static final String TIMEUPDATE = "timeupdate";
		/**
		 * ended event (Fired when the end of the media resource is reached)
		 */
		public static final String ENDED_EVENT = "ended";
		/**
		 * durationchange event (Fired when the duration of the media resource is changed, or known for the first time)
		 */
		public static final String DURATIONCHANGE_EVENT = "durationchange";
		/**
		 * progress event (Fired while the user agent is downloading media data)
		 */
		public static final String PROGRESS_EVENT = "progress";
		/**
		 * resize event (Fired when the width and/or height of the video window changes)
		 */
		public static final String RESIZE_EVENT = "resize";
		/**
		 * volumechange event (Fired when the volume changes)
		 */
		public static final String VOLUMECHANGE_EVENT = "volumechange";
		/**
		 * error event (Fired when there is an error in playback)
		 */
		public static final String ERROR_EVENT = "error";
		/**
		 * fullscreenchange event (Fired when the player switches in or out of fullscreen mode)
		 */
		public static final String FULLSCREENCHANGE_EVENT = "fullscreenchange";
		/**
		 * loadstart event (Fired when the user agent begins looking for media data)
		 */
		public static final String ANY_EVENT = "any";

		/**
		 * {@link Videojs} Component has triggered an event.
		 * 
		 * @param eventName name of the {@link Videojs.VjsListener VideojsListener} event.
		 * @param source the source component of the {@link Videojs.VjsListener VideojsListener} event.
		 * @param triggerTime the playback time of the videostream when the  event is triggered.
		 */
		@Override
		public void on(String eventName, Videojs source, Double triggerTime);

	}

}
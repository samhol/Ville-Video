package sph.vaadin.ui;

import com.vaadin.ui.themes.ChameleonTheme;
import com.vaadin.ui.themes.Reindeer;
/**
 * Contains predefined constants pointing to the CSS class names and image resources.
 *
 * <p><strong class="Red">USAGE EXAMPLES:</strong></p>
 * <ul>
 *  <li><strong>Style names for Buttons:</strong> <em>Button.addIcon({@link SPH_Theme#SAVE_ICON_16PX})</em></li>
 *  <li><strong>Theme resources for Buttons:</strong> <em>Button.addStyleName({@link SPH_Theme#SMALL_BLUE_BUTTON})</em></li>
 * </ul>
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 *
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 1.1
 * @since   30.10.2013
 */
public class SPH_Theme {

	/**
	 * CSS styles for a blue Button.
	 */
	public static final String BLUE_BUTTON = "v-button-default ";
	/**
	 * CSS styles for a small blue Button.
	 */
	public static final String SMALL_BLUE_BUTTON = BLUE_BUTTON + Reindeer.BUTTON_SMALL;
	/**
	 * CSS styles for a large blue Button.
	 */
	public static final String LARGE_BLUE_BUTTON = BLUE_BUTTON + ChameleonTheme.BUTTON_BIG;
	/**
	 * CSS styles for a default TextField.
	 */
	public static final String TEXTFIELD = "v-textfield-default ";
	/**
	 * CSS styles for a default small TextField.
	 */
	public static final String TEXTFIELD_SMALL = TEXTFIELD + Reindeer.TEXTFIELD_SMALL;
	/**
	 * Path to the delete icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String DELETE_ICON_16PX = "exercises/sphIcons/delete_16.png";
	/**
	 * Path to the plus icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String ADD_ICON_16PX = "exercises/sphIcons/add_16.png";
	/**
	 * Path to the save icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String SAVE_ICON_16PX = "exercises/sphIcons/save_16.png";
	/**
	 * Path to the cancel icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String CANCEL_ICON_16PX = "exercises/sphIcons/cancel_16.png";
	/**
	 * Path to the edit icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String EDIT_ICON_16PX = "exercises/sphIcons/edit_16.png";
	/**
	 * Path to the clear icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String CLEAR_ICON_16PX = "exercises/sphIcons/clear_16.png";
	/**
	 * Path to the load icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String LOAD_ICON_16PX = "exercises/sphIcons/load_16.png";
	/**
	 * Path to the magnifying glass icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String MAGNIFIER_ICON_16PX = "exercises/sphIcons/search_16.png";
	/**
	 * Path to the round ok icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String OK_ICON_16PX = "exercises/sphIcons/ok_16.png";
	/**
	 * Path to the round 'not ok' icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String NOT_OK_ICON_16PX = "exercises/sphIcons/not_ok_16.png";
	/**
	 * Path to the round 'next' icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String NEXT_ICON_16PX = "exercises/sphIcons/next_16.png";
	/**
	 * Path to the round 'previous' icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String PREVIOUS_ICON_16PX = "exercises/sphIcons/prev_16.png";
	/**
	 * Path to the round 'next' icon <strong>(size: 32X32 px)</strong>.
	 */
	public static final String NEXT_ICON_32PX = "exercises/sphIcons/next_32.png";
	/**
	 * Path to the round 'previous' icon <strong>(size: 32X32 px)</strong>.
	 */
	public static final String PREVIOUS_ICON_32PX = "exercises/sphIcons/prev_32.png";
	/**
	 * Path to the round blue 'play' icon <strong>(size: 48X48 px)</strong>.
	 */
	public static final String PLAY_ICON_48PX = "exercises/sphIcons/play_48.png";
	/**
	 * Path to the 'full screen' icon <strong>(size: 16X16 px)</strong>.
	 */
	public static final String FULLSCREEN_ICON_16PX = "exercises/sphIcons/fullscreen_16.png";
	/**
	 * Path to the 'full screen' icon <strong>(size: 32X32 px)</strong>.
	 */
	public static final String FULLSCREEN_ICON_32PX = "exercises/sphIcons/fullscreen_32.png";
	/**
	 * Path to the 'correct' icon image <strong>(size: 48X48 px)</strong>.
	 */
	public static final String CORRECT_ICON_48PX = "exercises/sphIcons/correct_48.png";
	/**
	 * Path to the 'incorrect' icon image <strong>(size: 48X48 px)</strong>.
	 */
	public static final String INCORRECT_ICON_48PX = "exercises/sphIcons/incorrect_48.png";
	/**
	 * Path to the 'ok' icon image <strong>(size: 24X24 px)</strong>.
	 */
	public static final String OK_ICON_24PX = "exercises/sphIcons/ok_24.png";
	/**
	 * Path to the 'incorrect' icon image <strong>(size: 24X24 px)</strong>.
	 */
	public static final String NOT_OK_ICON_24PX = "exercises/sphIcons/not_ok_24.png";
	/**
	 * Path to the 'zero' icon image <strong>(size: 26X26 px)</strong>.
	 */
	public static final String ZERO_ICON_24PX = "exercises/sphIcons/zero_26.png";
	/**
	 * Path to the 'forward' icon image <strong>(size: 20X20 px)</strong>.
	 */
	public static final String FORWARD_ICON_20PX = "exercises/sphIcons/forward_20.png";
	/**
	 * Path to the 'rewind' icon <strong>(size: 20X20 px)</strong>.
	 */
	public static final String REWIND_ICON_20PX = "exercises/sphIcons/rewind_20.png";
	/**
	 * Path to the 'start' icon <strong>(size: 128X128 px)</strong>.
	 */
	public static final String START_ICON_128PX = "exercises/sphIcons/start_128.png";
	/**
	 * Path to the 'information' icon <strong>(size: 24X24 px)</strong>.
	 */
	public static final String INFO_ICON_24PX = "exercises/sphIcons/i_24.png";
}
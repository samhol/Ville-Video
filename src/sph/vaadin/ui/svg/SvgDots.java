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
package sph.vaadin.ui.svg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;

import sph.event.BasicEventManager;
import sph.event.EventListener;

import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

/**
 * SvgDots Component draws colored circles (dots) horizontally to an SVG canvas.
 * 
 * <p>Uses <a href="http://raphaeljs.com/">Raphaël—JavaScript Library</a> for SVG graphics.</p>
 * 
 * <p><strong>copyright &copy; Ville-Video 2013</strong></p>
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author  Juha Mäkilä
 * @version 1.01
 * @since   19.10.2013
 * @see     <a href="http://raphaeljs.com/">Raphaël—JavaScript Library</a>
 * @see     <a href="http://raphaeljs.com/reference.html">Raphaël Reference</a>
 * @see     <a href="http://chrismichaelscott.github.io/fraphael/">FRaphael Filter Effects for Raphaël</a>
 * @see     <a href="https://github.com/chrismichaelscott/fraphael/wiki/Documentation">FRaphael documentation</a>
 * @see     <a href="http://www.w3schools.com/svg/svg_reference.asp">w3schools.com SVG Reference</a>
 */
@com.vaadin.annotations.JavaScript({ "javascript/raphael-min.js", "javascript/SvgDots.js" })
public class SvgDots extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 3689118911476002692L;

	private ArrayList<SvgDots.Dot> dots = new ArrayList<SvgDots.Dot>();

	/**
	 * an SvgDots EventManager
	 */
	private final BasicEventManager<String, SvgDots, Integer, DotListener> dotEventManager = new BasicEventManager<String, SvgDots, Integer, DotListener>();

	/**
	 * <strong>Default Constructor:</strong> Creates an empty SVG canvas for {@link SvgDots.Dot} elements.
	 */
	public SvgDots() {
		this(0);
		this.init();
	}

	/**
	 * <strong>Constructor:</strong> Creates amount of <code>count</code>
	 * Silver grey colored clickable {@link SvgDots.Dot} elements into an SVG canvas.
	 * 
	 * @.pre  count >= 0
	 * @.post {@link #size()} == count & FORALL({@link SvgDots.Dot dot} : this};
	 *        {@link SvgDots.Dot#getColor() dot.getColor()}.{@link String#equals(Object) equals("")} &
	 *        {@link SvgDots.Dot#getTitle() dot.getTitle()}.{@link String#equals(Object) equals("")} &
	 *        {@link SvgDots.Dot#isClickable() dot.isClickable()} == true)
	 * @param count the number of the created {@link SvgDots.Dot} elements.
	 */
	public SvgDots(int count) {
		ArrayList<SvgDots.Dot> dotsData = new ArrayList<SvgDots.Dot>();
		for (int i = 0; i < count; i++) {
			dotsData.add(new SvgDots.Dot("Silver"));
		}
		this.draw(dotsData);
		this.init();
	}

	/**
	 * Draws an amount of <code>dots.size()</code> {@link SvgDots.Dot} elements to the SVG canvas.
	 * 
	 * @param dots the list of the SVG dots.
	 */
	public SvgDots(final Collection<SvgDots.Dot> dots) {
		this.draw(dots);
		this.init();
	}

	/**
	 * Initializes the dots.
	 */
	private void init() {
		this.setId("svg_" + this.hashCode());
		this.addFunction("delegateDotEvent", new JavaScriptFunction() {

			private static final long serialVersionUID = -4245967454493315292L;

			@Override
			public void call(JSONArray arguments) throws JSONException {
				if (arguments.length() > 1) {
					dotEventManager.callListeners(arguments.getString(0), SvgDots.this, arguments.getInt(1));
				}
			}
		});
		this.addDetachListener(new DetachListener() {

			private static final long serialVersionUID = 5583909485994600778L;

			@Override
			public void detach(DetachEvent event) {
				dotEventManager.clear();
			}
		});
	}

	/**
	 * Draws the given {@link SvgDots.Dot} elements to the SVG canvas.
	 * 
	 * <p><strong class="red">NOTE:</strong> removes all previously defined {@link SvgDots.Dot} elements.</p>
	 * 
	 * @param dotList a collection of dots.
	 */
	public void draw(final Collection<SvgDots.Dot> dotList) {
		ArrayList<SvgDots.Dot> list = new ArrayList<SvgDots.Dot>(dotList.size());
		list.addAll(dotList);
		this.dots = list;
		getState().dots = list;
	}

	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param   index index of the element to return.
	 * @return  index of the element to return.
	 * @throws  IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size()).
	 */
	public SvgDots.Dot getDot(int index) {
		return this.dots.get(index);
	}

	/**
	 * Replaces the {@link SvgDots.Dot} element at the given index.
	 * 
	 * @param index the index of the replaced the {@link SvgDots.Dot} element.
	 * @param dot the {@link SvgDots.Dot} element.
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public void replaceDot(int index, SvgDots.Dot dot) {
		this.dots.set(index, dot);
		this.callFunction("replaceDot", index, dot);
	}

	/**
	 * Sets the color of the {@link SvgDots.Dot} element on the given index.
	 * 
	 * @param index the index of the {@link SvgDots.Dot}.
	 * @param color the color of the {@link SvgDots.Dot}.
	 */
	public void setDotColor(int index, String color) {
		this.callFunction("setDotColor", index, color);
	}

	/**
	 * Sets the title of the {@link SvgDots.Dot}  on the given index.
	 * 
	 * @param index the index of the {@link SvgDots.Dot}.
	 * @param title the color of the {@link SvgDots.Dot}.
	 */
	public void setDotTitle(int index, String title) {
		this.callFunction("setDotTitle", index, title);
	}

	/**
	 * Returns the number of SVG dots drawn into this {@link SvgDots}.
	 * 
	 * @.post RESULT >= 0
	 * @return the number of dots in this {@link SvgDots} Component
	 */
	public int size() {
		return this.dots.size();
	}

	/**
	 * Adds a new {@link DotListener} listener into the {@link SvgDots} component.
	 * 
	 * @param eventName the name of the {@link DotListener} event.
	 * @param listener the event listener object itself.
	 */
	public void addDotsListener(String eventName, DotListener listener) {
		this.dotEventManager.addListener(eventName, listener);
	}

	/**
	 * Remove listener from a specific {@link DotListener} event.
	 * 
	 * @param eventName the name of the {@link DotListener} event.
	 * @param listener the event listener to remove.
	 */
	public void removeListener(String eventName, DotListener listener) {
		this.dotEventManager.removeListener(eventName, listener);
	}

	/**
	 * Remove listener from all {@link DotListener} events it is registered by.
	 * Convenient way of cleaning up an listener object being destroyed.
	 * 
	 * @param listener the event listener to remove.
	 */
	public void removeListener(DotListener listener) {
		this.dotEventManager.removeListener(listener);
	}

	@Override
	protected SvgDotsState getState() {
		return (SvgDotsState) super.getState();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SvgDots [dots=" + dots + "]";
	}

	/**
	 * Dot contains all the required information about a single SVG dot in {@link SvgDots} component.
	 * 
	 * <p><strong class="red">Note:</strong> Dot is a JavaBeans Component.</p>
	 * 
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 1.01
	 * @since   22.10.2013
	 */
	public static class Dot implements Serializable, Cloneable {

		private static final long serialVersionUID = 5743971405439331162L;

		/**
		 * Property <code>title</code>: the title attribute of the SVG dot
		 */
		private String title = "";
		/**
		 * Property <code>color</code>: the color of the SVG dot
		 */
		private String color = "Silver";
		/**
		 * Property <code>clickable</code>: the color of the SVG dot
		 */
		private boolean clickable = true;
		/**
		 * Property <code>clickable</code>: the color of the SVG dot
		 */
		private Object data;

		/**
		 * <strong>Default Constructor:</strong> Creates an empty {@link SvgDots.Dot}
		 * element for an {@link SvgDots} component.
		 */
		public Dot() {
			this("Silver");
		}

		/**
		 * <strong>Constructor:</strong> Creates an {@link SvgDots.Dot} element
		 * of color <em>color</em> for an {@link SvgDots} component.
		 *
		 * @param color the color of the SVG dot.
		 */
		public Dot(String color) {
			this(color, "");
		}

		/**
		 * <strong>Constructor:</strong> Creates an {@link SvgDots.Dot} element
		 * of color <em>color</em> and title <em>title</em> for an {@link SvgDots} component.
		 * 
		 * @param color the color of the SVG dot.
		 * @param title the title attribute of the SVG dot.
		 */
		public Dot(String color, String title) {
			this.title = title;
			this.color = color;
		}

		/**
		 * <strong>Constructor:</strong> Creates an {@link SvgDots.Dot} element
		 * of color <em>color</em> and title <em>title</em> for an {@link SvgDots} component.
		 * 
		 * @param color the color of the SVG dot.
		 * @param title the title attribute of the SVG dot.
		 * @param clickable a boolean value specifying whether the dot is clickable or not.
		 */
		public Dot(String color, String title, boolean clickable) {
			this.title = title;
			this.color = color;
			this.clickable = clickable;
		}

		/**
		 * Sets the data object, that can be used for any application specific
		 * data. The component does not use or modify this data.
		 * 
		 * @param data the application specific data.
		 */
		public void setData(Object data) {
			this.data = data;
		}

		/**
		 * Gets the application specific data. See {@link #setData(Object)}.
		 * 
		 * @return the application specific data.
		 */
		public Object getData() {
			return data;
		}

		/**
		 * Returns the title attribute of the SVG dot.
		 * 
		 * @return the title attribute of the SVG dot.
		 */
		public String getTitle() {
			return this.title;
		}

		/**
		 * Sets the title attribute of the SVG dot.
		 * 
		 * @param title the title to set.
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/**
		 * Returns the color of the SVG dot.
		 * 
		 * @return the color of the SVG dot.
		 */
		public String getColor() {
			return this.color;
		}

		/**
		 * Sets the color of the SVG dot.
		 * 
		 * @param color the color to set.
		 */
		public void setColor(String color) {
			this.color = color;
		}

		/**
		 * Checks if the dot is clickable or not.
		 * 
		 * @return true if the dot is clickable and false otherwise.
		 */
		public boolean isClickable() {
			return clickable;
		}

		/**
		 * Changes the clickable state of the dot.
		 * 
		 * @param clickable a boolean value specifying whether the dot is clickable or not.
		 */
		public void setClickable(boolean clickable) {
			this.clickable = clickable;
		}

		/**
		 * Returns a hash code value for the object. This method is supported for
		 * the benefit of hash tables such as those provided by {@link java.util.HashMap}.
		 * 
		 * @return a hash code value for this object.
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (clickable ? 1231 : 1237);
			result = prime * result + ((color == null) ? 0 : color.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			return result;
		}

		/**
		 * Indicates whether some other object is equal to this {@link SvgDots.Dot} element.
		 * 
		 * @.post getColor().equals(RESULT.getColor()) &
		 *        getTitle().equals(RESULT.getTitle()) &
		 *        isClickable() == RESULT.isClickable()
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Dot other = (Dot) obj;
			if (clickable != other.clickable) {
				return false;
			}
			if (color == null) {
				if (other.color != null) {
					return false;
				}
			} else if (!color.equals(other.color)) {
				return false;
			}
			if (title == null) {
				if (other.title != null) {
					return false;
				}
			} else if (!title.equals(other.title)) {
				return false;
			}
			return true;
		}

		/**
		 * Returns a string representation of this {@link SvgDots.Dot} element.
		 *
		 * @return a string representation of this {@link SvgDots.Dot} element.
		 * @see    java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Dot [title=");
			builder.append(title);
			builder.append(", color=");
			builder.append(color);
			builder.append(", clickable=");
			builder.append(clickable);
			builder.append(", data=");
			builder.append(data);
			builder.append("]");
			return builder.toString();
		}
	}

	/**
	 * Returns a new {@link SvgDots.Dot} element of color <em>color</em> and
	 * title <em>title</em> for an {@link SvgDots} component.
	 * 
	 * @param color the color to set
	 * @param title the title to set.
	 * @param clickable a boolean value specifying whether the dot is clickable or not.
	 * @return the dot element created.
	 */
	public static Dot createDot(String color, String title, boolean clickable) {
		Dot dot = new Dot();
		dot.setColor(color);
		dot.setTitle(title);
		dot.setClickable(clickable);
		return dot;
	}

	/**
	 * SvgDotsListener interface for listening to {@link SvgDots.Dot} events fired by an {@link SvgDots} component.
	 * 
	 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
	 * @author  Juha Mäkilä
	 * @version 1.0
	 * @since   11.10.2013
	 */
	public static interface DotListener extends EventListener<String, SvgDots, Integer> {
		/**
		 * click event (Fired whenever a dot has been clicked)
		 */
		public static String CLICK_EVENT = "click";

		/**
		 * Called in response to a {@link SvgDots.Dot} event if the present listener is registered to listen for the given event.
		 * 
		 * @param eventName Name of the event.
		 * @param canvas the source SVG canvas of the SvgDot Event.
		 * @param dotIndex the index of the dot that triggered the event.
		 */
		@Override
		public void on(String eventName, SvgDots canvas, Integer dotIndex);
	}
}

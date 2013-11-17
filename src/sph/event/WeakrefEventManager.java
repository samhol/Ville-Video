/*
 * (C) 2004 - Geotechnical Software Services
 * 
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA  02111-1307, USA.
 */
package sph.event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generic event manager class. Events are of any type. Only the negotiating
 * classes needs to know about the events existence. It is transparent also to
 * the EventManager who only delegates the events.
 * 
 * <p class="red"><strong>Note:</strong> great care must be exercised if mutable
 * objects are used as events. The behavior of a map is not specified if the value
 * of an object is changed in a manner that affects equals comparisons while the
 * object is a key in the map.</p>
 * <p>
 * A potential problem with string based events are that there might be name
 * conflicts. In a large system, one might consider prefixing the event names to
 * avoid conflicts. If this seems to become a problem, the system is probably
 * bad designed anyway; The number of different event types should be kept low.
 * <p>
 * The listener class must implement the {@link sph.event.EventListener} interface
 * which implies the {@link sph.event.EventListener#on(E event, S source, D data)}
 * method. The listener class registers itself in the
 * EventManager
 * by:
 * 
 * <pre>
 * EventManager e = new EventManager();
 * e.addListener(&quot;EventName&quot;, source);
 * </pre>
 * 
 * The <em>source</em> class of the event will call:
 * 
 * <pre>
 * e.notify(&quot;EventName&quot;, object, data);
 * </pre>
 * and the EventManager will then call the {@link sph.event.EventListener#on()} method
 * of every listener.
 * <p>
 * The definition of <em>event</em>, <em>object</em> and <em>data</em> is purely up to the
 * involved classes. Typically <em>object</em> will be the source of the event
 * (the <em>created</em> object for a "Create" event, the <em>deleted</em>
 * object for a "Delete" event and so forth.) The additional <em>data</em> object
 * is for convenience only and will often be null.
 * 
 * @author <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 * @param  <E> the parameterized event type.
 * @param  <S> the parameterized type of the event source.
 * @param  <D> the parameterized type of the event data.
 */
public class WeakrefEventManager<E, S, D> {

	private final HashMap<E, Collection<WeakReference<EventListener<E, S, D>>>> listeners_;

	/**
	 * Create the event manager instance.
	 */
	public WeakrefEventManager() {
		listeners_ = new HashMap<E, Collection<WeakReference<EventListener<E, S, D>>>>();
	}

	/**
	 * Add a listener.
	 * 
	 * @param event The event the listener will listen to.
	 * @param eventListener The event listener object itself.
	 */
	public void addListener(E event, EventListener<E, S, D> eventListener) {
		// Check if this is a new event name
		Collection<WeakReference<EventListener<E, S, D>>> eventListeners = listeners_
				.get(event);
		if (eventListeners == null) {
			eventListeners = new ArrayList<WeakReference<EventListener<E, S, D>>>();
			listeners_.put(event, eventListeners);
		}

		// Check to see if the listener is already there
		for (Iterator<WeakReference<EventListener<E, S, D>>> i = eventListeners.iterator(); i.hasNext();) {
			WeakReference<EventListener<E, S, D>> reference = i.next();
			EventListener<E, S, D> listener = reference.get();
			if (listener == eventListener) {
				return;
			}
		}

		// Add the listener
		eventListeners.add(new WeakReference<EventListener<E, S, D>>(eventListener));
	}

	/**
	 * Remove listener from specific event.
	 * 
	 * @param event Event object to remove listener from.
	 * @param eventListener Listener to remove.
	 */
	public void removeListener(E event, EventListener<E, S, D> eventListener) {
		if (event == null) {
			removeListener(eventListener);
			return;
		}

		// Find the listeners for the specified event
		Collection<WeakReference<EventListener<E, S, D>>> eventListeners = listeners_
				.get(event);
		if (eventListeners == null) {
			return;
		}

		// Remove the listener
		for (Iterator<WeakReference<EventListener<E, S, D>>> i = eventListeners.iterator(); i.hasNext();) {
			WeakReference<EventListener<E, S, D>> reference = i.next();
			EventListener<E, S, D> listener = reference.get();
			if (listener == eventListener) {
				i.remove();
				break;
			}
		}

		// Remove the event as such if this was the last listener for this event
		if (eventListeners.size() == 0) {
			listeners_.remove(event);
		}
	}

	/**
	 * Remove listener from all events it is registered by. Convenient way of
	 * cleaning up an listener object being destroyed.
	 * 
	 * @param eventListener Event listener to remove.
	 */
	public void removeListener(EventListener<E, S, D> eventListener) {
		// Loop over all registered events and remove the specified listener
		// Loop over a copy incase the removeListener() call wants to
		// remove the entire event from the hash map.
		Collection<E> listeners = new ArrayList<E>(listeners_.keySet());
		for (Iterator<E> i = listeners.iterator(); i.hasNext();) {
			E eventName = i.next();
			removeListener(eventName, eventListener);
		}
	}

	/**
	 * Clears all EventListeners from the manager.
	 */
	public void clear() {
		listeners_.clear();
	}

	/**
	 * Calls listeners. The definition of <em>event</em>, <em>source</em> and <em>data</em> is
	 * purely up to the communicating classes.
	 * 
	 * @param event the event object.
	 * @param source Source of the event object (or null).
	 * @param data Additional data of the event (or null).
	 */
	public void callListeners(E event, S source, D data) {
		// Find all listeners of this event
		Collection<WeakReference<EventListener<E, S, D>>> eventListeners = listeners_
				.get(event);
		if (eventListeners == null) {
			return;
		}

		// Loop over a copy of the list in case it is altered by listener
		Collection<WeakReference<EventListener<E, S, D>>> copy = new ArrayList<WeakReference<EventListener<E, S, D>>>(
				eventListeners);
		for (Iterator<WeakReference<EventListener<E, S, D>>> i = copy.iterator(); i.hasNext();) {
			WeakReference<EventListener<E, S, D>> reference = i.next();
			EventListener<E, S, D> eventListener = reference.get();
			if (eventListener == null) {
				i.remove();
			} else {
				eventListener.on(event, source, data);
			}
		}
	}

	/**
	 * Calls listeners. Convenience front-end where the additional data parameter is null.
	 * The definition of <em>event</em> and <em>source</em> is purely up to the
	 * communicating classes.
	 * 
	 * @param event the event object.
	 * @param source Source of the event object (or null).
	 */
	public void callListeners(E event, S source) {
		callListeners(event, source, null);
	}
}

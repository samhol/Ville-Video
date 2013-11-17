/**
 * <p><b>Copyright 2013 Sami Holck</b></p>
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
package sph.event;

/**
 * Generic event manager interface. Events are of any type. Only the negotiating
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
 * e.callListeners(&quot;EventName&quot;, object, data);
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
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 0.1
 * @since   07.11.2013
 * @param   <E> the parameterized event type.
 * @param   <S> the parameterized type of the event source.
 * @param   <D> the parameterized type of the event data.
 * @param   <L> the parameterized type of the event listener.
 */
public interface EventManager<E, S, D, L extends EventListener<E, S, D>>  {

	/**
	 * Add a listener to the manager.
	 * 
	 * @param event The event the listener will listen to.
	 * @param eventListener The event listener object itself.
	 */
	public void addListener(E event, L eventListener);

	/**
	 * Remove listener from specific event.
	 * 
	 * @param event Event object to remove listener from.
	 * @param eventListener Listener to remove.
	 */
	public void removeListener(E event, L eventListener);

	/**
	 * Remove listener from all events it is registered by. Convenient way of
	 * cleaning up an listener object being destroyed.
	 * 
	 * @param eventListener Event listener to remove.
	 */
	public void removeListener(L eventListener);

	/**
	 * Clears all EventListeners from the manager.
	 */
	public void clear();

	/**
	 * Calls listeners. The definition of <em>event</em>, <em>source</em> and <em>data</em> is
	 * purely up to the communicating classes.
	 * 
	 * @param event the event object.
	 * @param source Source of the event object (or null).
	 * @param data Additional data of the event (or null).
	 */
	public void callListeners(E event, S source, D data);

	/**
	 * Calls listeners. Convenience front-end where the additional data parameter is null.
	 * The definition of <em>event</em> and <em>source</em> is purely up to the
	 * communicating classes.
	 * 
	 * @param event the event object.
	 * @param source Source of the event object (or null).
	 */
	public void callListeners(E event, S source);
}

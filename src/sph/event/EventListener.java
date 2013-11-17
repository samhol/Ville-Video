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

import java.io.Serializable;

/**
 * Interface shared by all <em>listeners</em> handled in an {@link BasicEventManager} object.
 * 
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 * @param <E> the parameterized event type.
 * @param <S> the parameterized type of the event source.
 * @param <D> the parameterized type of the event data.
 */
public interface EventListener<E, S, D> extends Serializable {

	/**
	 * Called in response to a {@link BasicEventManager#callListeners(E eventName, S source, D data)
	 *  EventManager.callListeners(E, S, T) } call if the
	 * present listener is registered to listen for the given event.
	 * 
	 * @param event  The event object fired.
	 * @param source The source the event (as defined by caller).
	 * @param data   Additional data (as defined by caller).
	 */
	public void on (E event, S source, D data);
}

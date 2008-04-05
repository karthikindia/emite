/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.core.packet;

public class Event extends DelegatedPacket {

	private static final String TYPE = "type";

	private static Packet cloneEvent(final Event event) {
		return new BasicPacket("event", "emite:event").With(TYPE, event
				.getType());
	}

	public Event(final Event event) {
		super(cloneEvent(event));
	}

	public Event(final String type) {
		super(new BasicPacket("event", "emite:event"));
		setAttribute(TYPE, type);
	}

	public Event Because(final String cause) {
		setAttribute("cause", cause);
		return this;
	}

	public String getType() {
		return getAttribute(TYPE);
	}
}

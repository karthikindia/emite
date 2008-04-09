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
package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.IPacket;

public class Presence extends BasicStanza {

    /**
     * 2.2.2.1. Show
     * 
     * <p>
     * If no 'show' element is provided, the entity is assumed to be online and
     * available.
     * </p>
     * 
     * <p>
     * If provided, the XML character data value MUST be one of the following
     * (additional availability types could be defined through a
     * properly-namespaced child element of the presence stanza):
     * </p>
     */
    public static enum Show {
	available, away, chat, dnd, xa
    }

    public enum Type {
	/**
	 * 2.2.1. Types of Presence
	 * 
	 * <p>
	 * The 'type' attribute of a presence stanza is OPTIONAL. A presence
	 * stanza that does not possess a 'type' attribute is used to signal to
	 * the server that the sender is online and available for communication.
	 * If included, the 'type' attribute specifies a lack of availability, a
	 * request to manage a subscription to another entity's presence, a
	 * request for another entity's current presence, or an error related to
	 * a previously-sent presence stanza. If included, the 'type' attribute
	 * MUST have one of the following values:
	 * </p>
	 */
	available,
	/**
	 * error -- An error has occurred regarding processing or delivery of a
	 * previously-sent presence stanza.
	 */
	error,
	/**
	 * probe -- A request for an entity's current presence; SHOULD be
	 * generated only by a server on behalf of a user.
	 */
	probe,
	/**
	 * subscribe -- The sender wishes to subscribe to the recipient's
	 * presence.
	 */

	subscribe,
	/**
	 * subscribed -- The sender has allowed the recipient to receive their
	 * presence.
	 */
	subscribed,
	/**
	 * unavailable -- Signals that the entity is no longer available for
	 * communication.
	 */
	unavailable,
	/**
	 * unsubscribe -- The sender is unsubscribing from another entity's
	 * presence.
	 */
	unsubscribe,
	/**
	 * unsubscribed -- The subscription request has been denied or a
	 * previously-granted subscription has been cancelled.
	 */
	unsubscribed
    }

    public Presence() {
	this(null, (String) null, (String) null);
    }

    public Presence(final IPacket stanza) {
	super(stanza);
    }

    public Presence(final Type type, final String from, final String to) {
	super("presence", "jabber:client");
	if (type != null) {
	    setType(type.toString());
	}
	if (from != null) {
	    setFrom(from.toString());
	}
	if (to != null) {
	    setTo(to.toString());
	}
    }

    public Presence(final Type type, final XmppURI from, final XmppURI to) {
	this(type, from != null ? from.toString() : null, to != null ? to.toString() : null);
    }

    public Presence(final XmppURI from) {
	this(null, from.toString(), null);
    }

    public int getPriority() {
	int value = 0;
	final IPacket priority = getFirstChild("priority");
	if (priority != null) {
	    try {
		value = Integer.parseInt(priority.getText());
	    } catch (final NumberFormatException e) {
		value = 0;
	    }
	}
	return value;
    }

    public Show getShow() {
	final IPacket show = getFirstChild("show");
	final String value = show != null ? show.getText() : null;
	return value != null ? Show.valueOf(value) : null;
    }

    public String getStatus() {
	final IPacket status = getFirstChild("status");
	return status != null ? status.getText() : null;
    }

    // TODO: revisar esto (type == null -> available)
    // http://www.xmpp.org/rfcs/rfc3921.html#presence
    public Type getType() {
	final String type = getAttribute(BasicStanza.TYPE);
	return type != null ? Type.valueOf(type) : Type.available;
    }

    public void setPriority(final int value) {
	IPacket priority = getFirstChild("priority");
	if (priority == null) {
	    priority = add("priority", null);
	}
	priority.setText(Integer.toString(value >= 0 ? value : 0));
    }

    public void setShow(final Show value) {
	IPacket show = getFirstChild("show");
	if (show == null) {
	    show = add("show", null);
	}
	show.setText(value.toString());
    }

    public void setStatus(final String statusMessage) {
	IPacket status = getFirstChild("status");
	if (status == null) {
	    status = add("status", null);
	}
	status.setText(statusMessage);
    }

    public Presence With(final Show value) {
	setShow(value);
	return this;
    }

}

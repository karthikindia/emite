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
package com.calclab.emite.client.extra.muc;

import java.util.ArrayList;

import com.calclab.emite.client.core.emite.Emite;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class Room implements Chat {
    private final XmppURI roomURI;
    private final String name;
    private final ArrayList<RoomUser> users;
    private final ArrayList<ChatListener> listeners;
    private final Emite emite;
    private final XmppURI userURI;

    public Room(final XmppURI userURI, final XmppURI roomURI, final String name, final Emite emite) {
	this.userURI = userURI;
	this.roomURI = roomURI;
	this.name = name;
	this.emite = emite;
	this.users = new ArrayList<RoomUser>();
	this.listeners = new ArrayList<ChatListener>();
    }

    /**
     * RoomListener are welcomed!
     */
    public void addListener(final ChatListener listener) {
	listeners.add(listener);
    }

    /**
     * In order to exit a multi-user chat room, an occupant sends a presence
     * stanza of type "unavailable" to the <room@service/nick> it is currently
     * using in the room.
     * 
     * @see http://www.xmpp.org/extensions/xep-0045.html#exit
     */
    public void close() {
	emite.send(new Presence(Type.unavailable, userURI, roomURI));
    }

    public String getID() {
	return roomURI.toString();
    }

    public String getName() {
	return name;
    }

    public XmppURI getOtherURI() {
	return roomURI;
    }

    public String getThread() {
	return roomURI.getNode();
    }

    public void send(final String text) {
	final Message message = new Message(userURI, roomURI, text);
	message.setType(Message.Type.groupchat);
	emite.send(message);
	for (final ChatListener listener : listeners) {
	    listener.onMessageSent(this, message);
	}
    }

    @Override
    public String toString() {
	return "ROOM: " + roomURI;
    }

    void addUser(final RoomUser roomUser) {
	users.add(roomUser);
	fireUserChanged();
    }

    void dispatch(final Message message) {
	for (final ChatListener listener : listeners) {
	    listener.onMessageReceived(this, message);
	}
    }

    private void fireUserChanged() {
	for (final ChatListener listener : listeners) {
	    try {
		((RoomListener) listener).onUserChanged(users);
	    } catch (final ClassCastException e) {
	    }
	}
    }

}

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
import java.util.List;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.client.xmpp.stanzas.Message.MessageType;

public class MUCRoomManager extends EmiteComponent implements RoomManager {
    private final Globals globals;
    private final ArrayList<RoomManagerListener> listeners;
    private final ArrayList<Room> rooms;

    public MUCRoomManager(final Emite emite, final Globals globals) {
	super(emite);
	this.globals = globals;
	this.listeners = new ArrayList<RoomManagerListener>();
	this.rooms = new ArrayList<Room>();
    }

    public void addListener(final RoomManagerListener listener) {
	listeners.add(listener);
    }

    @Override
    public void attach() {
	when(SessionManager.Events.loggedIn, new PacketListener() {
	    public void handle(final IPacket received) {
		sendMUCSupportQuery();
	    }
	});
	when("message", new PacketListener() {
	    public void handle(final IPacket received) {
		onMessageReceived(new Message(received));
	    }
	});
    }

    protected void onMessageReceived(final Message message) {
	if (message.getType() == MessageType.groupchat) {
	    // mensaje a una habitación
	    // Room room = this.getRoom(message.getFrom());
	    // room.dispatch(message);
	}
    }

    protected void sendMUCSupportQuery() {
	final IQ iq = new IQ(Type.get).From(globals.getOwnURI()).To(globals.getDomain());
	iq.setQuery("http://jabber.org/protocol/disco#info");
	emite.send("disco", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		System.out.println("MUC!!: " + received);
		sendRoomsQuery();
	    }
	});
    }

    /**
     * @see http://www.xmpp.org/extensions/xep-0045.html#disco-rooms
     */
    protected void sendRoomsQuery() {
	final IQ iq = new IQ(Type.get).From(globals.getOwnURI()).To(globals.getDomain());
	iq.setQuery("http://jabber.org/protocol/disco#items");
	emite.send("rooms", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		onRoomsQuery(received);
	    }
	});
    }

    private void fireRoomsChanged() {
	for (final RoomManagerListener listener : listeners) {
	    listener.onRoomsChanged(rooms);
	}
    }

    private void onRoomsQuery(final IPacket received) {
	final List<? extends IPacket> items = received.getFirstChild("query").getChildren();
	rooms.clear();
	for (final IPacket packet : items) {
	    rooms.add(new Room(packet.getAttribute("jid"), packet.getAttribute("name")));
	}
	fireRoomsChanged();
    }
}

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
package com.calclab.emite.client.xep.chatstate;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public class ChatStateManager {
    public static enum ChatState {
	active, composing, pause, inactive, gone
    }

    public static enum NegotiationStatus {
	notStarted, started, rejected, accepted
    }

    public static final String XMLNS = "http://jabber.org/protocol/chatstates";

    private ChatState ownState;
    private ChatState otherState;
    private final Chat chat;
    private NegotiationStatus negotiationStatus;
    private final Signal<ChatState> onChatStateChanged;

    final Slot<Message> doBeforeSend = new Slot<Message>() {
	public void onEvent(final Message message) {
	    switch (negotiationStatus) {
	    case notStarted:
		negotiationStatus = NegotiationStatus.started;
	    case accepted:
		boolean alreadyWithState = false;
		for (int i = 0; i < ChatState.values().length; i++) {
		    if (message.hasChild(ChatState.values()[i].toString())) {
			alreadyWithState = true;
		    }
		}
		if (!alreadyWithState) {
		    message.addChild(ChatState.active.toString(), XMLNS);
		}
		break;
	    case rejected:
	    case started:
		// do nothing
		break;
	    }
	}
    };

    public ChatStateManager(final Chat chat) {
	this.chat = chat;
	this.onChatStateChanged = new Signal<ChatState>("chatStateManager:onChatStateChanged");
	negotiationStatus = NegotiationStatus.notStarted;
	chat.onMessageReceived(new Slot<Message>() {
	    public void onEvent(final Message message) {
		onMessageReceived(chat, message);
	    }
	});
    }

    public NegotiationStatus getNegotiationStatus() {
	return negotiationStatus;
    }

    public ChatState getOtherState() {
	return otherState;
    }

    public ChatState getOwnState() {
	return ownState;
    }

    public void onChatStateChanged(final Slot<ChatState> slot) {
	onChatStateChanged.add(slot);
    }

    public void setOwnState(final ChatState chatState) {
	// From XEP: a client MUST NOT send a second instance of any given
	// standalone notification (i.e., a standalone notification MUST be
	// followed by a different state, not repetition of the same state).
	// However, every content message SHOULD contain an <active/>
	// notification.
	if (negotiationStatus.equals(NegotiationStatus.accepted)) {
	    if (ownState == null || !ownState.equals(chatState)) {
		this.ownState = chatState;
		Log.info("Setting own status to: " + chatState.toString());
		sendStateMessage(chatState);
	    }
	}
    }

    protected void onMessageReceived(final Chat chat, final Message message) {
	for (int i = 0; i < ChatState.values().length; i++) {
	    final ChatState chatState = ChatState.values()[i];
	    final String typeSt = chatState.toString();
	    if (message.hasChild(typeSt) || message.hasChild("cha:" + typeSt)) {
		otherState = chatState;
		if (negotiationStatus.equals(NegotiationStatus.notStarted)) {
		    sendStateMessage(ChatState.active);
		}
		if (chatState.equals(ChatState.gone)) {
		    negotiationStatus = NegotiationStatus.notStarted;
		} else {
		    negotiationStatus = NegotiationStatus.accepted;
		}
		Log.info("Receiver other chat status: " + typeSt);
		onChatStateChanged.fire(chatState);
	    }
	}
    }

    private void sendStateMessage(final ChatState chatState) {
	final Message message = new Message(chat.getFromURI(), chat.getOtherURI(), null).Thread(chat.getThread());
	message.addChild(chatState.toString(), XMLNS);
	chat.send(message);
    }
}

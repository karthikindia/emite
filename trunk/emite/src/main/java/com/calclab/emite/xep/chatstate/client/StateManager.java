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
package com.calclab.emite.xep.chatstate.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.events.Listener;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 * This implementation is limited to chat conversations. Chat state in MUC rooms
 * are not supported to avoid multicast of occupant states (in a BOSH medium can
 * be a problem).
 * 
 */
public class StateManager {

    public StateManager(final ChatManager chatManager) {

	chatManager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		getChatState(conversation);
	    }
	});

	chatManager.onChatClosed(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		Log.debug("Removing chat state to chat: " + conversation.getID());
		final ChatStateManager chatStateManager = conversation.getData(ChatStateManager.class);
		if (chatStateManager != null && chatStateManager.getOtherState() != ChatStateManager.ChatState.gone) {
		    // We are closing, then we send the gone state
		    chatStateManager.setOwnState(ChatStateManager.ChatState.gone);
		}
		conversation.setData(ChatStateManager.class, null);
	    }
	});
    }

    public ChatStateManager getChatState(final Conversation conversation) {
	ChatStateManager chatStateManager = conversation.getData(ChatStateManager.class);
	if (chatStateManager == null) {
	    chatStateManager = createChatState(conversation);
	}
	return chatStateManager;
    }

    private ChatStateManager createChatState(final Conversation conversation) {
	Log.debug("Adding chat state to chat: " + conversation.getID());
	final ChatStateManager chatStateManager = new ChatStateManager(conversation);
	conversation.setData(ChatStateManager.class, chatStateManager);
	conversation.onBeforeSend(chatStateManager.doBeforeSend);
	return chatStateManager;
    }

}
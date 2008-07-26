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
package com.calclab.emite.examples.chat.client;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatListenerAdaptor;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.examples.chat.client.ChatPanel.ChatPanelListener;
import com.calclab.emite.examples.chat.client.ConversationsPanel.ConversationsListener;
import com.calclab.emite.examples.chat.client.LoginPanel.LoginPanelListener;
import com.calclab.suco.client.signal.Slot;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ChatEntryPoint implements EntryPoint {

    private HashMap<XmppURI, ChatPanel> chats;
    private ConversationsPanel conversationsPanel;
    private DialogBox dialogBox;
    private LoginPanel loginPanel;
    private Xmpp xmpp;

    public void onModuleLoad() {

	Window.addWindowCloseListener(new WindowCloseListener() {
	    public void onWindowClosed() {
		if (xmpp != null) {
		    xmpp.stop();
		}
	    }

	    public String onWindowClosing() {
		return null;
	    }
	});

	chats = new HashMap<XmppURI, ChatPanel>();

	conversationsPanel = new ConversationsPanel(new ConversationsListener() {
	    public void onBeginChat(final String jid) {
		final Chat chat = xmpp.getChatManager().openChat(uri(jid), null, null);
		// we allways will have a chatPanel in chats because if its a
		// new conversations
		// the onChatcreated method in ChatManagerListener will be
		// called before
		// openChat returns
		final ChatPanel chatPanel = chats.get(chat.getOtherURI());
		conversationsPanel.show(jid, chatPanel);
	    }

	    public void onLogout() {
		xmpp.logout();
		conversationsPanel.clearRoster();
		dialogBox.show();
	    }
	});

	dialogBox = new DialogBox();
	loginPanel = new LoginPanel(new LoginPanelListener() {
	    public void onLogin(final String base, final String domain, final String name, final String password) {
		loginPanel.setStatus("preparing...");
		createXMPP(base, domain);
		xmpp.login(new XmppURI(name, domain, "emite"), password, Show.notSpecified, null);
	    }
	});
	dialogBox.setText("Login");
	dialogBox.setWidget(loginPanel);

	RootPanel.get().add(conversationsPanel);

	dialogBox.center();
	dialogBox.show();
    }

    protected void showMessageDialog(final String message) {
	final PopupPanel popupPanel = new PopupPanel(true, true);
	popupPanel.add(new Label(message));
	popupPanel.center();
	popupPanel.show();
    }

    private ChatPanel createChatPanel(final XmppURI uri, final Chat chat) {
	final ChatPanel chatPanel = new ChatPanel(new ChatPanelListener() {
	    public void onSend(final String text) {
		chat.send(new Message(text));
	    }
	});
	new ChatListenerAdaptor(chat, new ChatListener() {
	    public void onMessageReceived(final Chat chat, final Message message) {
		chatPanel.showIncomingMessage(message.getFrom(), message.getBody());
	    }

	    public void onMessageSent(final Chat chat, final Message message) {
		chatPanel.showOutcomingMessage(message.getBody());
	    }
	});
	chats.put(uri, chatPanel);
	conversationsPanel.addChat(uri.toString(), chatPanel);
	return chatPanel;
    }

    private void createXMPP(final String httpBase, final String hostName) {
	xmpp = Xmpp.create();
	xmpp.setBoshSettings(new Bosh3Settings(httpBase, hostName));

	xmpp.getSession().onStateChanged(new Slot<Session.State>() {
	    public void onEvent(final Session.State current) {
		final String theStatus = current.toString();
		loginPanel.setStatus(theStatus);
		conversationsPanel.setStatus(theStatus);
		switch (current) {
		case ready:
		    dialogBox.hide();
		    break;
		case notAuthorized:
		    showMessageDialog("not authorized!");
		    break;
		}
	    }
	});

	xmpp.getChatManager().onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		createChatPanel(chat.getOtherURI(), chat);
	    }
	});

	xmpp.getChatManager().onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		final ChatPanel panel = chats.remove(chat.getOtherURI());
		conversationsPanel.removeChat(chat.getOtherURI().toString(), panel);
	    }
	});

	xmpp.getRosterManager().setSubscriptionMode(SubscriptionMode.autoAcceptAll);

	xmpp.getRoster().onRosterChanged(new Slot<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		conversationsPanel.setRoster(items);
	    }
	});
    }
}
package com.calclab.hablar.chat.client.ui;

import java.util.HashMap;
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.hablar.basic.client.HablarEventBus;
import com.calclab.hablar.basic.client.ui.page.PageView.Visibility;
import com.calclab.hablar.basic.client.ui.pages.Pages;
import com.calclab.hablar.chat.client.ChatConfig;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;

public class ChatManagerLogic {
    public static interface ChatPageFactory {
	ChatPageView create(Chat chat, Visibility visibility, boolean sendButtonVisible);
    }
    private final HashMap<XmppURI, ChatPageView> chatPages;
    private final Pages pages;

    private final Roster roster;
    private final ChatPageFactory factory;
    private final boolean sendButtonVisible;

    public ChatManagerLogic(ChatConfig config, final Pages pages, ChatPageFactory factory) {
	this.pages = pages;
	this.factory = factory;
	this.chatPages = new HashMap<XmppURI, ChatPageView>();

	roster = Suco.get(Roster.class);
	final ChatManager chatManager = Suco.get(ChatManager.class);

	if (config.openChat != null) {
	    chatManager.open(config.openChat);
	}

	chatManager.onChatCreated(new Listener<Chat>() {
	    @Override
	    public void onEvent(Chat chat) {
		createChat(chat, Visibility.notFocused);
	    }
	});

	chatManager.onChatOpened(new Listener<Chat>() {
	    @Override
	    public void onEvent(Chat chat) {
		GWT.log("HABLAR ChatManager - OPEN", null);
		ChatPageView widget = chatPages.get(chat.getURI());
		assert widget != null;
		pages.open(widget);
	    }
	});
	roster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(RosterItem item) {
		XmppURI jid = item.getJID();
		Set<XmppURI> chats = chatPages.keySet();
		for (XmppURI chatURI : chats) {
		    if (chatURI.equalsNoResource(jid)) {
			ChatPageView page = chatPages.get(chatURI);
			page.setPresence(item.isAvailable(), item.getShow());
		    }
		}
	    }
	});

	sendButtonVisible = config.sendButtonVisible;

    }

    public ChatManagerLogic(final HablarEventBus hablarEventBus, ChatConfig config, final Pages pages) {
	this(config, pages, new ChatPageFactory() {
	    @Override
	    public ChatPageView create(Chat chat, Visibility visibility, boolean sendButtonVisible) {
		return new ChatPageWidget(hablarEventBus, chat, visibility, sendButtonVisible);
	    }
	});
    }

    private void createChat(Chat chat, Visibility visibility) {
	ChatPageView chatPage = factory.create(chat, Visibility.notFocused, sendButtonVisible);
	chatPage.setControlsVisible(true);
	chatPages.put(chat.getURI(), chatPage);
	pages.add(chatPage);
	RosterItem item = roster.getItemByJID(chat.getURI().getJID());
	Show show = item != null ? item.getShow() : Show.unknown;
	boolean available = item != null ? item.isAvailable() : false;
	chatPage.setPresence(available, show);
    }

}
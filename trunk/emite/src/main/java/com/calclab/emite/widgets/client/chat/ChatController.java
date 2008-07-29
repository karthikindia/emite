package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ChatController extends AbstractChatController {

    private XmppURI chatJID;
    private String userName;

    public ChatController(final Session session, final ChatManager manager) {
	super(session, manager);
    }

    public void setChatJID(final String jid) {
	this.chatJID = XmppURI.uri(jid);
	this.userName = null;
	showWaitingStatus();
    }

    public void setWidget(final ChatWidget widget) {
	widget.setController(this);
	super.setWidget(widget);
    }

    @Override
    protected XmppURI getChatURI() {
	return chatJID;
    }

    @Override
    protected String getFromUserName(final Message message) {
	if (userName == null) {
	    userName = message.getFrom().getNode();
	}
	return userName;
    }

    @Override
    protected boolean isOurChat(final Chat chat) {
	return chatJID.equalsNoResource(chat.getOtherURI());
    }

}
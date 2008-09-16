package com.calclab.emite.widgets.client.room;

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.widgets.client.chat.AbstractChatController;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.ioc.Provider;

public class RoomController extends AbstractChatController {
    private XmppURI chatJID;
    private String nick;
    private final Provider<RoomPresenceWidget> presenceWidgetProvider;
    private RoomPresenceWidget presenceWidget;

    public RoomController(final Session session, final RoomManager manager,
	    final Provider<RoomPresenceWidget> presenceWidgetProvider) {
	super(session, manager);
	this.presenceWidgetProvider = presenceWidgetProvider;
	this.nick = "user-" + new Date().getTime();
    }

    public void setNick(final String nick) {
	Log.debug("Nick: " + nick);
	this.nick = nick;
    }

    public void setWidget(final RoomWidget widget) {
	widget.setController(this);
	super.setWidget(widget);
    }

    public void showPresence(final boolean showPresence) {
	if (presenceWidget == null && showPresence == true) {
	    presenceWidget = presenceWidgetProvider.get();
	    if (chatJID != null) {
		presenceWidget.setParam(RoomPresenceWidget.PARAM_ROOM, chatJID.toString());
	    }
	    widget.dock(RoomWidget.EXT_RIGHT, presenceWidget);
	} else if (presenceWidget != null && showPresence == false) {
	    widget.unDock(presenceWidget);
	    presenceWidget = null;
	}
    }

    @Override
    protected XmppURI getChatURI() {
	return XmppURI.uri(chatJID.getNode(), chatJID.getHost(), nick);
    }

    @Override
    protected String getFromUserName(final Message message) {
	return message.getFrom().getResource();
    }

    @Override
    protected boolean isOurChat(final Chat chat) {
	return chatJID.equalsNoResource(chat.getOtherURI());
    }

    void setRoomJID(final String roomName) {
	assert this.chatJID == null;
	this.chatJID = XmppURI.uri(roomName);
	if (presenceWidget != null) {
	    presenceWidget.setParam(RoomPresenceWidget.PARAM_ROOM, roomName);
	}
    }

}

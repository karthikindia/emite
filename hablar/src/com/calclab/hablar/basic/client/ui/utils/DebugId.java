package com.calclab.hablar.basic.client.ui.utils;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.ui.UIObject;

// FIXME: ¿is there any way to remove this class in production?
public class DebugId {

    public static final String PRE = UIObject.DEBUG_ID_PREFIX;

    public static String getFromJid(final String prefix, final String jid) {
	return join(prefix, XmppURI.jid(jid).toString());
    }

    public static String getFromJid(final String prefix, final XmppURI jid) {
	return join(prefix, jid.toString());
    }

    public static String getGwtId(final String id) {
	return new StringBuffer().append(PRE).append(id).toString();
    }

    private static String join(final String prefix, final String jid) {
	return new StringBuffer().append(prefix).append(jid.replaceAll("@", "-")).toString();
    }

}

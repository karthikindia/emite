package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import com.calclab.emite.client.xmpp.stanzas.XmppJID;

public class ChatId {

    public final XmppJID jid;
    private final String thread;

    public ChatId(final XmppJID jid) {
        this(jid, "");
    }

    public ChatId(final XmppJID jid, final String thread) {
        this.jid = jid;
        this.thread = thread;
    }

    public XmppJID getJid() {
        return jid;
    }

    public String getThread() {
        return thread;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final ChatId other = (ChatId) obj;
        if (jid == null) {
            if (other.jid != null) {
                return false;
            }
        } else if (!jid.equals(other.jid)) {
            return false;
        }
        if (thread == null) {
            if (other.thread != null) {
                return false;
            }
        } else if (!thread.equals(other.thread)) {
            return false;
        }
        return true;
    }

}

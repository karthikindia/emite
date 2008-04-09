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
package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;

public class MultiChatCreationParam {

    private final BoshOptions boshOptions;
    private final String userJid;
    private final String userPassword;
    private final UserChatOptions userChatOptions;

    public MultiChatCreationParam(final BoshOptions boshOptions, final String userJid, final String userPasswd,
            final UserChatOptions userChatOptions) {
        this.boshOptions = boshOptions;
        this.userJid = userJid;
        this.userPassword = userPasswd;
        this.userChatOptions = userChatOptions;
    }

    public String getUserJid() {
        return userJid;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public BoshOptions getBoshOptions() {
        return boshOptions;
    }

    public UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

}

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
package com.calclab.emiteuimodule.client.chat;

import org.ourproject.kune.platf.client.View;

import com.calclab.emiteuimodule.client.roster.ChatIconDescriptor;

public interface ChatUIView extends View {

    void addDelimiter(String datetime);

    void addInfoMessage(String message);

    void addMessage(String userAlias, String color, String message);

    void destroy();

    void setChatTitle(String chatTitle, String tip, ChatIconDescriptor iconId);

}

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
package com.calclab.emiteui.client.emiteuiplugin;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.Xmpp;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUI;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIListener;
import com.calclab.emiteui.client.emiteuiplugin.dialog.MultiChat;
import com.calclab.emiteui.client.emiteuiplugin.dialog.MultiChatListener;
import com.calclab.emiteui.client.emiteuiplugin.params.MultiChatCreationParam;
import com.calclab.emiteui.client.emiteuiplugin.room.RoomUI;
import com.calclab.emiteui.client.emiteuiplugin.room.RoomUIListener;
import com.calclab.emiteui.client.emiteuiplugin.roster.RosterUI;

public interface ChatDialogFactory {

    public ChatUI createChatUI(final String currentUserAlias, final String currentUserColor, ChatUIListener listener);

    public MultiChat createMultiChat(final Xmpp xmpp, MultiChatCreationParam param, final I18nTranslationService i18n,
            final MultiChatListener listener);

    public RoomUI createRoomUI(final String currentUserAlias, final String currentUserColor,
            final I18nTranslationService i18n, RoomUIListener listener);

    public RosterUI createrRosterUI(final Xmpp xmpp, final I18nTranslationService i18n);

}
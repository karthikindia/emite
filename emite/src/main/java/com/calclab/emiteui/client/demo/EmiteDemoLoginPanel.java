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
package com.calclab.emiteui.client.demo;

import java.util.Date;

import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class EmiteDemoLoginPanel {

    public static interface LoginPanelListener {

        void onOffline();

        void onOnline();

        void onUserChanged(UserChatOptions generateUserChatOptions);

    }

    private final TextField fieldJid;
    private final TextField fieldPassw;
    private String release;

    public EmiteDemoLoginPanel(final LoginPanelListener listener) {
        release = "not-specified";
        final Panel panel = new Panel();
        panel.setBorder(false);
        panel.setPaddings(15);

        final FormPanel formPanel = new FormPanel();
        formPanel.setFrame(true);
        formPanel.setTitle("Some external Login Form");
        formPanel.setButtonAlign(Position.LEFT);

        formPanel.setWidth(320);
        formPanel.setLabelWidth(75);

        fieldJid = new TextField("Jabber id", "jid", 200);
        fieldJid.setAllowBlank(false);
        formPanel.add(fieldJid);

        fieldPassw = new TextField("Password", "last", 200);
        fieldPassw.setAllowBlank(false);
        fieldPassw.setPassword(true);
        formPanel.add(fieldPassw);

        Button onlineBtn = new Button("Go online");
        onlineBtn.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                listener.onOnline();
            }
        });

        Button offlineBtn = new Button("Go offline");
        offlineBtn.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                listener.onOffline();
            }
        });

        formPanel.addButton(onlineBtn);
        formPanel.addButton(offlineBtn);

        fieldJid.addListener(new FieldListenerAdapter() {
            @Override
            public void onChange(final Field field, final Object newVal, final Object oldVal) {
                listener.onUserChanged(getUserChatOptions());
            }
        });

        fieldPassw.addListener(new FieldListenerAdapter() {
            @Override
            public void onChange(final Field field, final Object newVal, final Object oldVal) {
                listener.onUserChanged(getUserChatOptions());
            }
        });
        panel.add(formPanel);

        RootPanel.get().add(panel);
    }

    public UserChatOptions getUserChatOptions() {
        final String resource = "emiteui-" + new Date().getTime() + "-" + release;
        return new UserChatOptions(fieldJid.getRawValue(), fieldPassw.getRawValue(), resource, "blue",
                XRosterManager.DEF_SUBSCRIPTION_MODE, true);
    }

    public void setInitalData(final String djid, final String pass, final String relVer) {
        this.release = relVer;
        fieldPassw.setValue(pass);
        fieldJid.setValue(djid);
    }
}

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
package com.calclab.emite.client.components;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;

@SuppressWarnings("serial")
public class DefaultContainer extends HashMap<String, Object> implements Container {
    private final ArrayList<Startable> startables;

    DefaultContainer() {
	this.startables = new ArrayList<Startable>();
    }

    public Object get(final String componentName) {
	return super.get(componentName);
    }

    public void install(final String name, final Startable startable) {
	register(name, startable);
	startables.add(startable);
    }

    public void register(final String name, final Object component) {
	Log.debug("Registering component " + name);
	super.put(name, component);
    }

    public void start() {
	for (final Startable c : startables) {
	    c.start();
	}
    }

    public void stop() {
	for (final Startable c : startables) {
	    c.stop();
	}
    }
}

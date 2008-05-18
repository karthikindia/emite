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
package com.calclab.emite.client.core.packet;

import java.util.HashMap;
import java.util.List;

public class DelegatedPacket implements IPacket {
    private final IPacket delegate;

    public DelegatedPacket(final IPacket delegate) {
	this.delegate = delegate;
    }

    public final IPacket add(final String nodeName, final String xmlns) {
	return delegate.add(nodeName, xmlns);
    }

    public void addChild(final IPacket child) {
	delegate.addChild(child);
    }

    public final String getAttribute(final String name) {
	return delegate.getAttribute(name);
    }

    public int getAttributeAsInt(final String name) {
	return delegate.getAttributeAsInt(name);
    }

    public HashMap<String, String> getAttributes() {
	return delegate.getAttributes();
    }

    public List<? extends IPacket> getChildren() {
	return delegate.getChildren();
    }

    public List<? extends IPacket> getChildren(final PacketFilter filter) {
	return delegate.getChildren(filter);
    }

    public List<? extends IPacket> getChildren(final String name) {
	return delegate.getChildren(name);
    }

    public int getChildrenCount() {
	return delegate.getChildrenCount();
    }

    public IPacket getFirstChild(final PacketFilter filter) {
	return delegate.getFirstChild(filter);
    }

    public final IPacket getFirstChild(final String childName) {
	return delegate.getFirstChild(childName);
    }

    public final String getName() {
	return delegate.getName();
    }

    public IPacket getParent() {
	return delegate.getParent();
    }

    public final String getText() {
	return delegate.getText();
    }

    public boolean hasAttribute(final String name) {
	return delegate.hasAttribute(name);
    }

    public boolean hasAttribute(final String name, final String value) {
	return delegate.hasAttribute(name, value);
    }

    public boolean hasChild(final String name) {
	return delegate.hasChild(name);
    }

    public void render(final StringBuffer buffer) {
	delegate.render(buffer);
    }

    public final void setAttribute(final String name, final String value) {
	delegate.setAttribute(name, value);
    }

    public final void setText(final String text) {
	delegate.setText(text);
    }

    @Override
    public String toString() {
	return delegate.toString();
    }

    public IPacket With(final IPacket child) {
	return delegate.With(child);
    }

    public IPacket With(final String name, final long value) {
	return delegate.With(name, value);
    }

    public IPacket With(final String name, final String value) {
	return delegate.With(name, value);
    }

    public IPacket WithText(final String text) {
	return delegate.WithText(text);
    }

}

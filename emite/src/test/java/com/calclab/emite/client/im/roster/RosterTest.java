package com.calclab.emite.client.im.roster;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.testing.MockSlot;

public class RosterTest {
    private Roster roster;

    @Before
    public void aaCreate() {
	roster = new Roster();
    }

    @Test
    public void shouldFindByJID() {
	final RosterItem item = new RosterItem(uri("someone@domain/resource"), null, null);
	roster.add(item);
	assertSame(item, roster.findItemByJID(uri("someone@domain/different_resource")));
    }

    @Test
    public void shouldFireListenersWhenPresenceChanged() {
	final RosterItem item = new RosterItem(uri("one@domain"), Subscription.none, "one");
	final MockSlot<RosterItem> listener = new MockSlot<RosterItem>();
	roster.onItemChanged(listener);
	roster.add(item);
	roster.changePresence(item.getJID(), new Presence());
	verifyCalled(listener);
    }

    @Test
    public void shouldFireListenerWhenItemRemoved() {
	final MockSlot<Collection<RosterItem>> slot = new MockSlot<Collection<RosterItem>>();
	roster.onRosterChanged(slot);
	roster.add(new RosterItem(uri("one@domain/resource1"), Subscription.none, "one"));
	verifyCalled(slot, 1);
	assertEquals(1, slot.getValue(0).size());
	roster.removeItem(uri("one@domain/resource2"));
	verifyCalled(slot, 2);

	assertEquals(0, slot.getValue(1).size());
    }

    @Test
    public void shouldInformWhenRosterChanged() {
	final MockSlot<Collection<RosterItem>> slot = new MockSlot<Collection<RosterItem>>();
	roster.onRosterChanged(slot);
	final List<RosterItem> itemCollection = new ArrayList<RosterItem>();
	itemCollection.add(new RosterItem(uri("name@domain"), Subscription.none, "name"));
	roster.setItems(itemCollection);
	verifyCalled(slot, 1);
	assertTrue(slot.getValue(0).contains(itemCollection.get(0)));
	// verify(oldListener).onRosterChanged(hasSame(itemCollection));
    }

    @Test
    public void shouldInformWhenRosterItemChanged() {
	final MockSlot<Collection<RosterItem>> slot = new MockSlot<Collection<RosterItem>>();
	roster.onRosterChanged(slot);
	roster.setItems(new ArrayList<RosterItem>());
	verifyCalled(slot, 1);
	assertEquals(0, slot.getValue(0).size());
	roster.add(new RosterItem(uri("name@domain/res"), null, null));
	verifyCalled(slot, 2);
	assertEquals(1, slot.getValue(1).size());
    }
}
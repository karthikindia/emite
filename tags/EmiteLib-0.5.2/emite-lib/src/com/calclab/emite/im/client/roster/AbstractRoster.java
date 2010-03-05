package com.calclab.emite.im.client.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractRoster implements Roster {
    private static final String[] EMPTY_GROUPS = new String[0];

    private final HashMap<XmppURI, RosterItem> itemsByJID;
    private final HashMap<String, List<RosterItem>> itemsByGroup;

    private final Event<Collection<RosterItem>> onRosterReady;
    private final Event<RosterItem> onItemAdded;
    private final Event<RosterItem> onItemChanged;
    private final Event<RosterItem> onItemRemoved;
    private boolean rosterReady;

    public AbstractRoster() {
	rosterReady = false;
	itemsByJID = new HashMap<XmppURI, RosterItem>();
	itemsByGroup = new HashMap<String, List<RosterItem>>();

	onItemAdded = new Event<RosterItem>("roster:onItemAdded");
	onItemChanged = new Event<RosterItem>("roster:onItemChanged");
	onItemRemoved = new Event<RosterItem>("roster:onItemRemoved");

	onRosterReady = new Event<Collection<RosterItem>>("roster:onRosterReady");
    }

    @Deprecated
    public final void addItem(final XmppURI jid, final String name, final String... groups) {
	requestAddItem(jid, name, groups);
    }

    public Set<String> getGroups() {
	return itemsByGroup.keySet();
    }

    public RosterItem getItemByJID(final XmppURI jid) {
	return itemsByJID.get(jid.getJID());
    }

    public Collection<RosterItem> getItems() {
	return new ArrayList<RosterItem>(itemsByJID.values());
    }

    public Collection<RosterItem> getItemsByGroup(final String groupName) {
	return itemsByGroup.get(groupName);
    }

    @Override
    public boolean isRosterReady() {
	return rosterReady;
    }

    public void onItemAdded(final Listener<RosterItem> listener) {
	onItemAdded.add(listener);
    }

    public void onItemChanged(final Listener<RosterItem> listener) {
	onItemChanged.add(listener);
    }

    public void onItemRemoved(final Listener<RosterItem> listener) {
	onItemRemoved.add(listener);
    }

    @Deprecated
    public void onItemUpdated(final Listener<RosterItem> listener) {
	onItemChanged(listener);
    }

    public void onRosterRetrieved(final Listener<Collection<RosterItem>> listener) {
	onRosterReady.add(listener);
    }

    /**
     * Updates a roster item in server side
     * 
     * @param item
     *            the roster item to be updated
     */
    @Override
    public void updateItem(final RosterItem item) {
	updateItem(item.getJID(), item.getName(), item.getGroups().toArray(EMPTY_GROUPS));
    }

    protected void clearitemsByJID() {
	itemsByJID.clear();
    }

    protected void fireItemAdded(final RosterItem item) {
	onItemAdded.fire(item);
    }

    protected void fireItemChanged(final RosterItem item) {
	onItemChanged.fire(item);
    }

    protected void fireItemRemoved(final RosterItem item) {
	onItemRemoved.fire(item);
    }

    protected void fireRosterReady(final Collection<RosterItem> collection) {
	rosterReady = true;
	onRosterReady.fire(collection);
    }

    protected List<RosterItem> getGroupItems(final String group) {
	return itemsByGroup.get(group);
    }

    protected Set<String> getGroupNames() {
	return itemsByGroup.keySet();
    }

    protected void remove(final XmppURI jid) {
	itemsByJID.remove(jid);
    }

    protected void removeFromGroup(final String groupName) {
	itemsByGroup.remove(groupName);
    }

    protected void storeItem(final RosterItem item) {
	itemsByJID.put(item.getJID(), item);
	for (final String group : item.getGroups()) {
	    List<RosterItem> items = itemsByGroup.get(group);
	    if (items == null) {
		items = new ArrayList<RosterItem>();
		itemsByGroup.put(group, items);
	    }
	    items.add(item);
	}

    }
}
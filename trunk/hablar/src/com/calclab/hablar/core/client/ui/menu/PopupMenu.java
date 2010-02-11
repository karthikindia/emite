package com.calclab.hablar.core.client.ui.menu;

import java.util.HashMap;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupMenu<T> extends PopupPanel implements MenuDisplay<T> {
    private final MenuBar bar;
    private final HashMap<Action<T>, MenuItem> items;

    public PopupMenu(final String debugId) {
	super(true);
	super.ensureDebugId(debugId);
	setStyleName("hablar-PopupPanel");
	this.items = new HashMap<Action<T>, MenuItem>();
	bar = new MenuBar(true);
	setWidget(bar);
    }

    @Override
    public void addAction(final Action<T> action, final Command command) {
	final MenuItem menuItem = bar.addItem(action.getName(), true, command);
	menuItem.ensureDebugId(action.getId());
	items.put(action, menuItem);
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public boolean isVisible() {
	return isShowing();
    }

    @Override
    public void setActionVisible(final Action<T> action, final boolean visible) {
	items.get(action).setVisible(visible);
    }

    @Override
    public void show(final int left, final int top) {
	setPopupPosition(left, top);
	show();
    }

}

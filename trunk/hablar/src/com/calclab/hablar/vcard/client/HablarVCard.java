package com.calclab.hablar.vcard.client;

import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.container.PageAddedEvent;
import com.calclab.hablar.core.client.container.PageAddedHandler;
import com.calclab.hablar.core.client.container.overlay.OverlayContainer;
import com.calclab.hablar.core.client.ui.menu.Action;
import com.calclab.hablar.core.client.ui.menu.SimpleAction;
import com.calclab.hablar.roster.client.page.RosterPage;
import com.calclab.hablar.roster.client.page.RosterPresenter;
import com.calclab.hablar.roster.client.ui.groups.RosterItemPresenter;
import com.calclab.hablar.user.client.UserContainer;
import com.google.gwt.core.client.EntryPoint;

public class HablarVCard implements EntryPoint {

    private static final String ACTION_ID_VIEW_VCARD = "HablarVCard-seeVCardAction";

    protected static Action<RosterItemPresenter> createViewVCardAction(final OthersVCardPresenter othersVCardPage) {
	return new SimpleAction<RosterItemPresenter>("See user profile", ACTION_ID_VIEW_VCARD) {
	    @Override
	    public void execute(final RosterItemPresenter target) {
		othersVCardPage.setUser(target.getItem().getJID());
	    }
	};
    }

    public static void install(final Hablar hablar) {
	final OwnVCardPresenter ownVCardPage = new OwnVCardPresenter(hablar.getEventBus(), new VCardWidget());
	hablar.addPage(ownVCardPage, UserContainer.ROL);

	final OthersVCardPresenter othersVCardPage = new OthersVCardPresenter(hablar.getEventBus(), new VCardWidget());
	hablar.addPage(othersVCardPage, OverlayContainer.ROL);

	hablar.addPageAddedHandler(new PageAddedHandler() {
	    @Override
	    public void onPageAdded(final PageAddedEvent event) {
		final RosterPage rosterPage = RosterPresenter.asRoster(event.getPage());
		if (rosterPage != null) {
		    rosterPage.getItemMenu().addAction(createViewVCardAction(othersVCardPage));
		}
	    }
	}, true);
    }

    @Override
    public void onModuleLoad() {
    }

}
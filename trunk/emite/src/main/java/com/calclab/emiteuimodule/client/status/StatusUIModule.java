package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.SingletonScope;

public class StatusUIModule extends AbstractModule {

    public StatusUIModule() {
	super(StatusUIModule.class);
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<StatusUI>(StatusUI.class) {
	    public StatusUI create() {
		final StatusUIPresenter presenter = new StatusUIPresenter($p(Xmpp.class), $(Session.class),
			$(PresenceManager.class), $p(RosterManager.class), $p(ChatManager.class),
			$p(RoomManager.class), $(I18nTranslationService.class));
		final StatusUIPanel panel = new StatusUIPanel(presenter, $(I18nTranslationService.class));
		presenter.init(panel);
		return presenter;
	    }
	});

    }

}
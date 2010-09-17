package com.calclab.hablar.testing;

import com.calclab.emite.core.client.bosh.BoshConnection;
import com.calclab.emite.core.client.bosh.XmppBoshConnection;
import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.services.gwt.GWTServices;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.DecoderRegistry;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.IMSessionManager;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.calclab.emite.core.client.xmpp.session.SessionImpl;
import com.calclab.emite.core.client.xmpp.session.SessionReady;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSessionLogic;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.GWT;

public class EmiteCoreModule extends AbstractModule {

	@Override
	public void onInstall() {
		registerDecorator(SessionComponent.class, new SessionComponent(
				container));

		register(Singleton.class, new Factory<Services>(Services.class) {
			@Override
			public Services create() {
				return new GWTServices();
			}
		});

		register(Singleton.class, new Factory<EmiteEventBus>(
				EmiteEventBus.class) {
			@Override
			public EmiteEventBus create() {
				return EventBusFactory.create("emite");
			}
		});

		register(Singleton.class, new Factory<XmppConnection>(
				XmppConnection.class) {
			@Override
			public XmppConnection create() {
				return new XmppBoshConnection($(EmiteEventBus.class),
						$(Services.class));
			}
		});

		register(Singleton.class, new Factory<XmppSession>(XmppSession.class) {
			@Override
			public XmppSession create() {
				return new XmppSessionLogic($(XmppConnection.class),
						$(SASLManager.class), $(ResourceBindingManager.class),
						$(IMSessionManager.class),
						$(SessionComponentsRegistry.class));
			}

			@Override
			public void onAfterCreated(final XmppSession session) {
				GWT.log("TRIGGER SESSION CREATED");
				$(SessionComponent.class).init();
			}
		});

		register(Singleton.class, new Factory<Connection>(Connection.class) {
			@Override
			public Connection create() {
				return new BoshConnection($(XmppConnection.class));
			}
		}, new Factory<IMSessionManager>(IMSessionManager.class) {
			@Override
			public IMSessionManager create() {
				return new IMSessionManager($(XmppConnection.class));
			}
		}, new Factory<Session>(Session.class) {
			@Override
			public Session create() {
				final SessionImpl session = new SessionImpl(
						$(XmppSession.class));
				GWT.log("SESSION CREATED!");
				return session;
			}

		}, new Factory<ResourceBindingManager>(ResourceBindingManager.class) {
			@Override
			public ResourceBindingManager create() {
				return new ResourceBindingManager($(XmppConnection.class));
			}
		}, new Factory<DecoderRegistry>(DecoderRegistry.class) {
			@Override
			public DecoderRegistry create() {
				return new DecoderRegistry();
			}
		}, new Factory<SASLManager>(SASLManager.class) {
			@Override
			public SASLManager create() {
				return new SASLManager($(XmppConnection.class),
						$(DecoderRegistry.class));
			}
		});
		register(SessionComponent.class, new Factory<SessionReady>(
				SessionReady.class) {
			@Override
			public SessionReady create() {
				return new SessionReady($(XmppSession.class));
			}
		});

	}

}

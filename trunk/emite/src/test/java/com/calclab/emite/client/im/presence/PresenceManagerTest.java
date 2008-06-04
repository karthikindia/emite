package com.calclab.emite.client.im.presence;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.SlotTester.verifyCalled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.SlotTester;

public class PresenceManagerTest {

    private PresenceManager manager;
    private EmiteTestHelper emite;

    @Before
    public void beforeTest() {
	emite = new EmiteTestHelper();
	manager = new PresenceManager(emite);

    }

    @Test
    public void shouldBroadcastPresenceIfLoggedin() {
	manager.logIn(uri("myself@domain"));
	manager.setOwnPresence("this is my new status", Show.away);
	emite.verifySent("<presence from='myself@domain'><show>away</show>"
		+ "<status>this is my new status</status></presence>");
	final Presence current = manager.getOwnPresence();
	assertEquals(Show.away, current.getShow());
	assertEquals("this is my new status", current.getStatus());
    }

    @Test
    public void shouldDelayPresenceIfNotLoggedIn() {
	manager.setOwnPresence("my message", Show.chat);
	emite.verifyNothingSent();
	assertEquals(Presence.Type.unavailable, manager.getOwnPresence().getType());
    }

    @Test
    public void shouldHavePresenceEvenLoggedOut() {
	assertNotNull(manager.getOwnPresence());
    }

    @Test
    public void shouldSendDelayedAsSoonAsPossible() {
	manager.setOwnPresence("my delayed status", Show.dnd);
	manager.logIn(uri("myself@domain"));
	emite.receives(RosterManager.Events.ready);
	emite.verifySent("<presence from='myself@domain'><show>chat</show></presence>");
	emite.verifySent("<presence from='myself@domain'><show>dnd</show>"
		+ "<status>my delayed status</status></presence>");
    }

    @Test
    public void shouldSendFinalPresence() {
	manager.logIn(uri("myself@domain"));
	manager.logOut();
	emite.verifySent("<presence from='myself@domain' type='unavailable' />");
    }

    @Test
    public void shouldSendInitialPresenceAfterRosterReady() {
	manager.logIn(uri("myself@domain"));
	emite.receives(RosterManager.Events.ready);
	emite.verifySent("<presence from='myself@domain'><show>chat</show></presence>");
    }

    @Test
    public void shouldSendPresenceIfLoggedIn() {
	manager.logIn(uri("myself@domain"));
	manager.setOwnPresence(new Presence().With(Presence.Show.dnd));
	emite.verifySent("<presence from='myself@domain'><show>dnd</show></presence>");

    }

    @Test
    public void shouldSignalIncommingPresence() {
	final SlotTester<Presence> slot = new SlotTester<Presence>();
	manager.onPresenceReceived(slot);
	emite.receives(createPresence(Type.available));
	emite.receives(createPresence(Type.unavailable));
	verifyCalled(slot, 2);
    }

    @Test
    public void shouldSignalOwnPresence() {
	manager.logIn(uri("myself@domain"));
	final SlotTester<Presence> listener = new SlotTester<Presence>();
	manager.onOwnPresenceChanged(listener);
	manager.setOwnPresence("status", Show.away);
	verifyCalled(listener);
	assertEquals("status", listener.getValue(0).getStatus());
	assertEquals(Show.away, listener.getValue(0).getShow());
    }

    private Presence createPresence(final Type type) {
	final Presence presence = new Presence(type, uri("from@domain"), uri("to@domain"));
	return presence;
    }
}
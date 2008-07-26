package com.calclab.emite.client.core.bosh3;

import static org.junit.Assert.*;

import org.junit.Test;

import com.calclab.emite.test.services.MockServices;
import com.calclab.emite.testing.IsPacketLike;

public class Bosh3ConnectionTests {

    private final MockServices services;
    private final Bosh3Connection connection;

    public Bosh3ConnectionTests() {
	services = new MockServices();
	connection = new Bosh3Connection(services);
    }

    @Test
    public void shouldSendInitialBody() {
	connection.setSettings(new Bosh3Settings("httpBase", "localhost"));
	connection.connect();
	assertEquals(1, services.requestSentCount());
	final IsPacketLike matcher = IsPacketLike.build("<body to='localhost' "
		+ "content='text/xml; charset=utf-8' xmlns:xmpp='urn:xmpp:xbosh' "
		+ " ack='1' hold='1' secure='true' xml:lang='en' "
		+ "xmpp:version='1.6' wait='60' xmlns='http://jabber.org/protocol/httpbind' />");
	assertTrue(matcher.matches(services.getSentPacket(0), System.out));
    }
}
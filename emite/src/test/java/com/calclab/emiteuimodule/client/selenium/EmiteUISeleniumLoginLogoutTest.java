package com.calclab.emiteuimodule.client.selenium;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;

public class EmiteUISeleniumLoginLogoutTest extends TestCase {

    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
	return new DefaultSelenium("localhost", 4441, "*firefox /usr/lib/firefox/firefox-2-bin", url);
    }

    public void testOnlineAndOffline() throws Exception {
	DefaultSelenium selenium = createSeleniumClient("http://localhost:4444/");
	selenium.start();

	try {
	    selenium.open("/gwt/com.calclab.emiteui.EmiteUI/EmiteUI.html");
	} catch (UnsupportedOperationException e) {
	    fail("Seems that selenium server is not running; run before: 'java -jar selenium-server.jar -port 4441' ");
	}
	selenium.click("EmiteDemoLoginPanel-online-button");
	for (int second = 0;; second++) {
	    if (second >= 60)
		fail("timeout");
	    try {
		if (selenium.getText("gwt-debug-MultiChatPanel-InfoLabel").matches(
			"^To start a chat, select a buddy or join to a chat room[\\s\\S]*$"))
		    break;
	    } catch (Exception e) {
	    }
	    Thread.sleep(1000);
	}

	selenium.click("EmiteDemoLoginPanel-offline-button");
	for (int second = 0;; second++) {
	    if (second >= 60)
		fail("timeout");
	    try {
		if ("To start a chat you need to be 'online'.".equals(selenium
			.getText("gwt-debug-MultiChatPanel-InfoLabel")))
		    break;
	    } catch (Exception e) {
	    }
	    Thread.sleep(1000);
	}
	selenium.stop();
    }
}
package com.gm.apps.demo.xmpp.config;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

@Configuration
public class XmppConnectionServiceConfig {
  @Autowired
  private AbstractXMPPConnection xmppConnection;
  private static final Logger LOGGER = LogManager.getLogger(XmppConnectionServiceConfig.class);

  @PostConstruct
  @Async
  public void connectToXmpp() {
    if (!xmppConnection.isConnected()) {
      LOGGER.debug("Starting XMPP server for connection...");
      try {
        xmppConnection.connect(); // Establishes a connection to the server
        xmppConnection.login(); // Logs in
      } catch (InterruptedException e) {
        LOGGER.error("Error starting XMPP connection", e);
        Thread.currentThread().interrupt();
      } catch (SmackException | IOException | XMPPException e) {
        LOGGER.error("Error starting XMPP connection", e);
      }
    }
  }

  @Async
  @PreDestroy
  public void shutdown() {
    if (xmppConnection.isConnected()) {
      LOGGER.debug("Stopping XMPP server for connection...");
      xmppConnection.disconnect();
    }
  }
}

package com.gm.apps.demo.xmpp.config;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import com.gm.apps.demo.xmpp.core.services.ChatGroupService;
import com.gm.apps.demo.xmpp.core.services.ChatRoomService;
import com.gm.apps.demo.xmpp.core.services.ChatUserService;
import com.gm.apps.demo.xmpp.core.services.impl.ChatGroupServiceImpl;
import com.gm.apps.demo.xmpp.core.services.impl.ChatRoomServiceImpl;
import com.gm.apps.demo.xmpp.core.services.impl.ChatUserServiceImpl;

// @Configuration
public class XmppConnectionServiceConfig {
  @Value("${xmpp.user.username}")
  private String xmppUser;
  @Value("${xmpp.user.phrase}")
  private String xmppPassPhrase;
  @Value("${xmpp.host}")
  private String xmppHost;
  @Value("${xmpp.port:9090}")
  private int xmppPort;
  @Autowired
  private AbstractXMPPConnection xmppConnection;
  private static final Logger LOGGER = LogManager.getLogger(XmppConnectionServiceConfig.class);
  @Autowired
  private ModelMapper modelMapper;

  @PostConstruct
  @Async
  public void connectToXmpp() {
    if (!xmppConnection.isConnected()) {
      LOGGER.debug("Starting XMPP server for connection...");
      try {
        xmppConnection.connect();
        LOGGER.debug("Connection created with server");
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

  @Bean
  public RestApiClient xmppApiClient() {
    AuthenticationToken authenticationToken = new AuthenticationToken(xmppUser, xmppPassPhrase);
    String host = xmppHost;
    if (!xmppHost.contains("http") || !xmppHost.contains("https")) {
      host = "http://".concat(host);
    }
    return new RestApiClient(host, xmppPort, authenticationToken);
  }

  @Bean
  public ChatUserService xmppUserSearchService() {
    return new ChatUserServiceImpl(xmppApiClient(), modelMapper);
  }

  @Bean
  public ChatGroupService chatGroupService() {
    return new ChatGroupServiceImpl(xmppApiClient(), modelMapper);
  }

  @Bean
  public ChatRoomService chaRoomService() {
    return new ChatRoomServiceImpl(xmppApiClient(), modelMapper);
  }
}

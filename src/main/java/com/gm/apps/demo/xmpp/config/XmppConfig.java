package com.gm.apps.demo.xmpp.config;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

// @Configuration
public class XmppConfig {
	@Value("${xmpp.user.username}")
	private String xmppUser;
	@Value("${xmpp.user.phrase}")
	private String xmppPassPhrase;
	@Value("${xmpp.user.domain}")
	private String xmppDomain;
	@Value("${xmpp.host}")
	private String xmppHost;
	@Value("${xmpp.port:9090}")
	private int xmppPort;

	@Bean
	public XMPPTCPConnectionConfiguration xmppTcpConnectionConfiguration() throws XmppStringprepException {
	// @formatter:off
    return XMPPTCPConnectionConfiguration.builder()
        .setXmppDomain(xmppDomain)
        .setCompressionEnabled(true)
        .setSecurityMode(SecurityMode.disabled)
        .setHost(xmppHost)
        .build();
    // @formatter:on
	}

	@Bean
	public AbstractXMPPConnection xmppConnection(XMPPTCPConnectionConfiguration config) {
		return new XMPPTCPConnection(config);
	}
}

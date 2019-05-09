package com.gm.apps.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class}, scanBasePackages = {"com.gm.apps"})
@PropertySource(value = {"classpath:demo-app.properties"})
@EnableJpaAuditing
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  // @Bean
  // public CommandLineRunner demo(AbstractXMPPConnection xmppConnection) {
  // return args -> {
  // if(null == args || args.length <= 0) {
  // return;
  // }
  //
  // System.out.println("-----------Using username" + args[0]);
  // xmppConnection.login(args[0], "Welcome@1");
  // ChatManager chatManager = ChatManager.getInstanceFor(xmppConnection);
  // chatManager.addIncomingListener((EntityBareJid from, Message message, Chat chat) -> {
  // System.out.println("Incoming chat message");
  // System.out.println("From: " + from.asEntityBareJidString());
  // System.out.println("Message: " + message.getBody());
  // try {
  // chat.send("Sending Message from t1u1 from incoming");
  // } catch (NotConnectedException | InterruptedException e) {
  // e.printStackTrace();
  // }
  // });
  // chatManager.addOutgoingListener((EntityBareJid from, Message message, Chat chat) -> {
  // System.out.println("Outgoing chat message");
  // System.out.println("From: " + from.asEntityBareJidString());
  // System.out.println("Message: " + message.getBody());
  //// try {
  //// chat.send("Message from t1u1 from outgoing");
  //// } catch (NotConnectedException | InterruptedException e) {
  //// e.printStackTrace();
  //// }
  // });
  // };
  // }
}

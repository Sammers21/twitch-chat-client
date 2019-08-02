package io.github.sammers21.twitch.chat;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class TwitchChatClient {

    private static final Logger log = LoggerFactory.getLogger(TwitchChatClient.class);
    public static final String IRC_HOST = "irc.chat.twitch.tv";
    public static final Integer IRC_PORT = 6667;

    private final String oauthToken;
    private final String nickName;
    private final TcpTextClient tcpTextClient;
    private Consumer<ChatMessage> handler;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public TwitchChatClient(String oauthToken, String nickName) {
        this.oauthToken = oauthToken;
        this.nickName = nickName;
        tcpTextClient = new TcpTextClient(IRC_HOST, IRC_PORT);
        tcpTextClient.outputHandler(event -> {
            if (event.equals("PING :tmi.twitch.tv")) {
                tcpTextClient.input("PONG :tmi.twitch.tv");
                log.debug("PING PONG OK");
                return;
            }
            if (event.endsWith(">") && countDownLatch.getCount() == 1) {
                countDownLatch.countDown();
                log.debug("Input can be started");
                return;
            }
            if (handler != null) {
                ChatMessage parse = ChatMessage.parse(event);
                if (parse != null) {
                    Objects.requireNonNull(handler);
                    Objects.requireNonNull(parse);
                    handler.accept(parse);
                }
            }
        });
    }

    public void start() {
        tcpTextClient.start();
        tcpTextClient.input(String.format("PASS oauth:%s", oauthToken));
        tcpTextClient.input(String.format("NICK %s", nickName));
    }

    public void stop() {
        tcpTextClient.stop();
    }

    public void messageHandler(Consumer<ChatMessage> handler) {
        this.handler = handler;
    }

    public void joinChannel(String chan) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tcpTextClient.input(String.format("JOIN #%s", chan));
    }

}

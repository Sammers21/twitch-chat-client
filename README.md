# twitch-chat-client

This library can be used for reading messages from a twitch chat. 

Usage:

1. Obtain token on https://twitchapps.com/tmi/

2. Code:
```java
TwitchChatClient twitchChatClient = new TwitchChatClient(token, "mytwitchnick");
twitchChatClient.start();
twitchChatClient.joinChannel("ninja");
twitchChatClient.joinChannel("shroud");
twitchChatClient.messageHandler(message -> {
    String text = message.getText();
    Instant receivedAt = message.getReceivedAt();
    String chanName = message.getChanName();
    String author = message.getAuthor();
    // do whatever you whant
});
```

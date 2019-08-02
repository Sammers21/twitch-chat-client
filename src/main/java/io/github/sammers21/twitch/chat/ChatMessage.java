package io.github.sammers21.twitch.chat;

import java.time.Clock;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessage {

    public static Pattern MSG_PATTERN = Pattern.compile(":(.+)!.+@.+ PRIVMSG #(\\S+) :(.+)");

    private final String text;
    private final String chanName;
    private final String author;
    private final Instant receivedAt;

    public ChatMessage(String text, String chanName, String author, Instant receivedAt) {
        this.text = text;
        this.chanName = chanName;
        this.author = author;
        this.receivedAt = receivedAt;
    }

    public static ChatMessage parse(String line) {
        Matcher matcher = MSG_PATTERN.matcher(line);
        boolean found = matcher.matches();
        if (found) {
            return new ChatMessage(matcher.group(3), matcher.group(2), matcher.group(1), Instant.now(Clock.systemUTC()));
        } else {
            return null;
        }
    }

    public String getText() {
        return text;
    }

    public String getChanName() {
        return chanName;
    }

    public String getAuthor() {
        return author;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}

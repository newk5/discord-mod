package com.github.newk5.discordmod.wrappers;

import com.eclipsesource.v8.V8Function;
import java.util.List;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class BotWrapper {

    private JDA jda;
    private V8Function onMessageCallback;
    private V8Function onReadyCallback;
    private V8Function onShutdownCallback;
    private V8Function onPrivateMsgCallback;

    public BotWrapper() {
    }

    public BotWrapper(JDA jda) {
        this.jda = jda;
    }

    public void send(String channel, String msg) {
        List<TextChannel> channels = jda.getTextChannelsByName(channel, false);

        for (TextChannel ch : channels) {
            ch.sendMessage(msg).queue();
            break;
        }
    }

    public void message(String channel, String msg) {

        List<User> users = jda.getUsersByName(channel, false);

        for (User user : users) {
            user.openPrivateChannel().complete().sendMessage(msg).queue();
            break;
        }
    }

    public JDA getJda() {
        return jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }

    public V8Function getOnMessageCallback() {
        return onMessageCallback;
    }

    public void setOnMessageCallback(V8Function onMessageCallback) {
        this.onMessageCallback = onMessageCallback;
    }

    public V8Function getOnReadyCallback() {
        return onReadyCallback;
    }

    public void setOnReadyCallback(V8Function onReadyCallback) {
        this.onReadyCallback = onReadyCallback;
    }

    public V8Function getOnShutdownCallback() {
        return onShutdownCallback;
    }

    public void setOnShutdownCallback(V8Function onShutdownCallback) {
        this.onShutdownCallback = onShutdownCallback;
    }

    public V8Function getOnPrivateMsgCallback() {
        return onPrivateMsgCallback;
    }

    public void setOnPrivateMsgCallback(V8Function onPrivateMsgCallback) {
        this.onPrivateMsgCallback = onPrivateMsgCallback;
    }

}

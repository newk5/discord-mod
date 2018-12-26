package com.github.newk5.discordmod.injectables;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8ResultUndefined;
import com.github.newk5.discordmod.DiscordModule;
import com.github.newk5.discordmod.listeners.MessageListener;
import com.github.newk5.discordmod.wrappers.BotWrapper;
import static com.github.newk5.vcmp.javascript.plugin.internals.Runtime.console;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.pmw.tinylog.Logger;

public class DiscordWrapper {

    private static CopyOnWriteArrayList<String> botsTokens = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<String> botNames = new CopyOnWriteArrayList<>();

    public static Map<String, BotWrapper> bots = new ConcurrentHashMap<>();

    public void init(String token, V8Object events, V8Object options) throws LoginException {
        if (botsTokens.addIfAbsent(token)) {
            BotWrapper bw = new BotWrapper();
            bw.setOnMessageCallback(getCallBack(events, "onMessage"));
            bw.setOnPrivateMsgCallback(getCallBack(events, "onPrivateMsg"));
            bw.setOnReadyCallback(getCallBack(events, "onReady"));            
            JDA jda = new JDABuilder(token).build();

            DiscordModule.pool.submit(() -> {

                try {

                    jda.addEventListener(new MessageListener());
                    bw.setJda(jda);
                    bots.put(token, bw);

                } catch (Exception ex) {
                    Logger.error(ex);
                    if (ex.getCause() != null) {
                        console.error(ex.getCause().toString());
                    } else {
                        console.error(ex.getMessage());
                    }
                }

            });
        }

    }

    public void send(String token, String channel, String msg) {
        if (bots.containsKey(token)) {
            DiscordModule.pool.submit(() -> {
                bots.get(token).send(channel, msg);

            });
        }
    }

    public void message(String token, String user, String msg) {
        if (bots.containsKey(token)) {
            DiscordModule.pool.submit(() -> {
                try {
                    bots.get(token).message(user, msg);
                } catch (Exception e) {
                    Logger.error(e);
                    console.error(e.getCause().toString());
                }
            });
        }
    }

    public V8Function getCallBack(V8Object m, String functionName) {
        Object callback = m.get(functionName);
        if (callback != null && callback instanceof V8Function) {
            return (V8Function) callback;
        }
        if (callback instanceof V8ResultUndefined) {
            return null;
        }
        return null;
    }

}

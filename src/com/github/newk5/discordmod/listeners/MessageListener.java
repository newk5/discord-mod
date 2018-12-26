package com.github.newk5.discordmod.listeners;

import static com.github.newk5.discordmod.injectables.DiscordWrapper.botNames;
import static com.github.newk5.discordmod.injectables.DiscordWrapper.bots;
import static com.github.newk5.vcmp.javascript.plugin.internals.Runtime.eventLoop;
import com.github.newk5.vcmp.javascript.plugin.internals.result.AsyncResult;
import com.github.newk5.vcmp.javascript.plugin.internals.result.CommonResult;
import java.util.List;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    
    public String[] getRoles(List<Role> roles){
        String[] array = new String[roles.size()];
        
        for (int i = 0; i < roles.size();i++){
            array[i] = roles.get(i).getName();
        }
        return array;
    }

   
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean isBot = event.getAuthor().isBot();
        String name = event.getAuthor().getName();
        if (!isBot && !botNames.contains(name)) {
            if (event.isFromType(ChannelType.TEXT)) {

                String token = event.getJDA().getToken().split("Bot ")[1];
                if (bots.containsKey(token)) {
                    AsyncResult res = new CommonResult(bots.get(token).getOnMessageCallback(), new Object[]{event, getRoles(event.getMember().getRoles())});
                    res.setMaintainCallback(true);
                    eventLoop.queue.add(res);
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String token = event.getJDA().getToken().split("Bot ")[1];
        if (bots.containsKey(token)) {
            AsyncResult res = new CommonResult(bots.get(token).getOnPrivateMsgCallback(), new Object[]{event});
            res.setMaintainCallback(true);
            eventLoop.queue.add(res);
        }
    }

  

    @Override
    public void onReady(ReadyEvent event) {

        String name = event.getJDA().getSelfUser().getName();

        botNames.add(name);

        String token = event.getJDA().getToken().split("Bot ")[1];
        if (bots.containsKey(token)) {
            AsyncResult res = new CommonResult(bots.get(token).getOnReadyCallback(), new Object[]{event});
            res.setMaintainCallback(true);
            eventLoop.queue.add(res);
        }

    }

}

package net.notfab.discord.selfbot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * DiscordSelfBot - http://notfab.net/
 * Created by Fabricio20 on 4/10/2016.
 */
public class MessageListener extends ListenerAdapter {

    private final String LENNY = "( ͡° ͜ʖ ͡°)";
    private final String SHRUG = "¯\\_(ツ)_/¯";

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(!e.getAuthor().getId().equals(Main.getInstance().getJDA().getSelfInfo().getId())) {
            return;
        }
        if(e.getMessage().getContent().contains("(lenny)")) {
            e.getMessage().updateMessage(e.getMessage().getRawContent().replace("(lenny)", this.LENNY));
        } else if(e.getMessage().getContent().contains("(shrug)")) {
            e.getMessage().updateMessage(e.getMessage().getRawContent().replace("(shrug)", this.SHRUG));
        } else if(e.getMessage().getContent().contains(":lenny:")) {
            e.getMessage().updateMessage(e.getMessage().getRawContent().replace(":lenny:", this.LENNY));
        } else if(e.getMessage().getContent().contains(":shrug:")) {
            e.getMessage().updateMessage(e.getMessage().getRawContent().replace(":shrug:", this.SHRUG));
        }
    }

}

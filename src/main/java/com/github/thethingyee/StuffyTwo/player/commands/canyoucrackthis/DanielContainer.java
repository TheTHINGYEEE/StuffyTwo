package com.github.thethingyee.StuffyTwo.player.commands.canyoucrackthis;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DanielContainer extends Command {
    @Override
    public String getName() {
        return "D6A18C73FCFF26A44A318EBE89EA853E";
    }

    @Override
    public String getDescription() {
        return "why";
    }

    @Override
    public String getArguments() {
        return "<nostalgia|happy|chill|sad>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        if(args.length != 1) return;
        event.getMessage().delete().queue();
        switch(args[0]) {
            case "nostalgia": {
                manager.getPlayerManager().loadAndPlay(event.getChannel(), "https://www.youtube.com/playlist?list=PL-TPluNxJzZLOanGDCqiExigPv6Q-h1TC",
                        event.getMember(), true);
                break;
            }
            case "happy": {
                manager.getPlayerManager().loadAndPlay(event.getChannel(), "https://www.youtube.com/playlist?list=PLoLgHeTExdPlNJuVFTenYWOE8TWiF_7S-",
                        event.getMember(), true);
                break;
            }
            case "chill": {
                manager.getPlayerManager().loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=SHpQ77wDfYg",
                        event.getMember(), true);

                break;
            }
            case "sad": {
                manager.getPlayerManager().loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=nAX_dT-S1EI",
                        event.getMember(), true);
                break;
            }
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean useCommandHashing() {
        return true;
    }
}

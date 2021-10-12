package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand extends Command {

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current track and moves on to the next track.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        manager.getPlayerManager().skipTrack(event.getChannel());
    }
}

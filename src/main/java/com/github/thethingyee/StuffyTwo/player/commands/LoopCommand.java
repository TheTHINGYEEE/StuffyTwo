package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.TrackScheduler;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LoopCommand extends Command {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getDescription() {
        return "Loops the current playing track.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        TrackScheduler scheduler = manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler();
        scheduler.setRepeating(scheduler.isRepeating());
        String msg = (scheduler.isRepeating()) ? "Looping is now disabled." : "Looping is now enabled.";
        event.getChannel().sendMessage(msg).queue();
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean useCommandHashing() {
        return false;
    }
}

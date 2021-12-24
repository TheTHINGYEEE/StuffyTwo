package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PauseCommand extends Command {

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Pauses the audio player.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        AudioPlayer player = manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getPlayer();
        String pauseMsg = player.isPaused() ? "Resuming..." : "Pausing...";
        player.setPaused(!player.isPaused());

        event.getChannel().sendMessage(pauseMsg).queue();
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

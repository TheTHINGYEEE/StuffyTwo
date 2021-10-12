package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.Embeds;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ClearQueueCommand extends Command {

    @Override
    public String getName() {
        return "clearqueue";
    }

    @Override
    public String getDescription() {
        return "Clears the whole queue.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        List<AudioTrack> queue = manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler().getQueue();

        EmbedBuilder b = queue.isEmpty() ? Embeds.getRemovedQueueEmbed(event.getGuild(), false) : Embeds.getRemovedQueueEmbed(event.getGuild(), true);

        if(!queue.isEmpty()) manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler().clearQueue(event.getGuild());
        event.getChannel().sendMessageEmbeds(b.build()).queue();
    }
}

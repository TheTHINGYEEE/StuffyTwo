package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.Embeds;
import com.github.thethingyee.StuffyTwo.handlers.TrackScheduler;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class QueueCommand extends Command {
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Returns the queued songs that are gonna be playing next.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        if(args.length != 0) return;
        EmbedBuilder builder = Embeds.getListQueueEmbed(manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()),
                event.getJDA().getSelfUser(),
                event.getMember());

        if(builder == null) {
            event.getChannel().sendMessage("There is nothing playing.").queue();
            return;
        }
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
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

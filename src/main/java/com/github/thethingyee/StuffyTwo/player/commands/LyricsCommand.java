package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.Embeds;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.jagrosh.jlyrics.Lyrics;
import com.jagrosh.jlyrics.LyricsClient;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.ExecutionException;

/**
 * Uses the JLyrics API for querying or searching lyrics.
 * Source: https://github.com/jagrosh/JLyrics
 */
public class LyricsCommand extends Command {

    @Override
    public String getName() {
        return "lyrics";
    }

    @Override
    public String getDescription() {
        return "Gets the lyrics for the song playing.";
    }

    @Override
    public String getArguments() {
        return "<query||none>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        AudioTrack trackPlaying = manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getPlayer().getPlayingTrack();

        if(args.length == 0) {
            sendLyrics(event.getChannel(), trackPlaying.getInfo().title, event.getMember());
            return;
        }
        if(args.length >= 1) {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < args.length; i++) {
                builder.append(args[i] + " ");
            }
            String query = builder.toString();
            sendLyrics(event.getChannel(), query, event.getMember());
        }

    }

    private void sendLyrics(TextChannel channel, String query, Member member) {
        Lyrics lyrics = findLyrics(query);
        if(lyrics == null) {
            channel.sendMessage("Can't find any lyrics for '" + query + "'").queue();
            return;
        }
        channel.sendMessageEmbeds(Embeds.getTrackLyricsEmbed(lyrics, member).build()).queue();
    }

    private Lyrics findLyrics(String query) {
        LyricsClient lyricsClient = new LyricsClient();
        try {
            return lyricsClient.getLyrics(query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

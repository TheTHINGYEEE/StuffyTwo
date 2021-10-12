package com.github.thethingyee.StuffyTwo.player.manager;

import com.github.thethingyee.StuffyTwo.handlers.Embeds;
import com.github.thethingyee.StuffyTwo.handlers.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private final AudioPlayerManager audioPlayerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.audioPlayerManager = new DefaultAudioPlayerManager();

        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(audioPlayerManager, guild);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(final TextChannel channel, final String trackUrl, Member member, boolean sendEmbed) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if(sendEmbed) channel.sendMessageEmbeds(Embeds.getTrackLoadedEmbed(track).build()).queue();

                play(channel.getGuild(), musicManager, track, member);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                AudioTrack firstTrack = playlist.getSelectedTrack();

                if(playlist.isSearchResult()) {
                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().get(0);
                    }
                }

                channel.sendMessageEmbeds(Embeds.getTrackLoadedEmbed(firstTrack).build()).queue();

                play(channel.getGuild(), musicManager, firstTrack, member);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
        if(member.getVoiceState().inVoiceChannel() && (member.getVoiceState().getChannel() != null)) {
            connectToMemberVoiceChannel(guild.getAudioManager(), member);
            musicManager.getScheduler().queue(track);
            return;
        }
        connectToFirstVoiceChannel(guild.getAudioManager());
        musicManager.getScheduler().queue(track);
    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.getScheduler().nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    public static void connectToMemberVoiceChannel(AudioManager manager, Member member) {
        if(!manager.isConnected()) {
            if(member.getVoiceState().inVoiceChannel()) {
                if(member.getVoiceState().getChannel() != null) {
                    manager.getGuild().getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
                }
            }
        }
    }

    public static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }
}

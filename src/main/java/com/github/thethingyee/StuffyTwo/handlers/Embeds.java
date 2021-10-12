package com.github.thethingyee.StuffyTwo.handlers;

import com.github.thethingyee.StuffyTwo.player.manager.PlayerManager;
import com.jagrosh.jlyrics.Lyrics;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Embeds {

    // here is where u can make skins for StuffyTwo lol


    public static EmbedBuilder getTrackLyricsEmbed(Lyrics lyrics, Member member) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(lyrics.getContent());
        embed.setAuthor(lyrics.getAuthor());
        embed.setTitle(lyrics.getTitle(), lyrics.getURL());
        embed.setColor(member.getColor());

        return embed;
    }

    public static EmbedBuilder getTrackLoadedEmbed(AudioTrack track) {
        EmbedBuilder builder = new EmbedBuilder();

        String videoId = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");

        builder.setTitle("Track added.", track.getInfo().uri);
        builder.addField("Track name:", track.getInfo().title, false);
        builder.addField("Video ID:", videoId, true);

        ColorHandler cHandler = new ColorHandler(ImageHandler.getYoutubeThumbnail(videoId));
        builder.setColor(cHandler.getDominantColor());

        String templateYt = "https://i.ytimg.com/vi/%s/mqdefault.jpg";
        builder.setImage(String.format(templateYt, videoId));

        return builder;

    }

    public static EmbedBuilder getRemoveTrackEmbed(AudioTrack trackToBeRemoved, TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Status");
        builder.addField("Removed:", trackToBeRemoved.getInfo().title, false);
        builder.setFooter(trackToBeRemoved.getInfo().uri);

        builder.setColor(channel.getGuild().getSelfMember().getColor());

        return builder;
    }

    public static EmbedBuilder getRemovedQueueEmbed(Guild guild, boolean cleared) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Status");

        if(cleared) {
            builder.addField("Cleared queue:", guild.getName(), false);
            builder.setColor(Color.GREEN);
            return builder;
        }
        builder.addField("Queue already cleared:", guild.getName(), false);
        builder.setColor(guild.getSelfMember().getColor());

        return builder;
    }

    public static EmbedBuilder getListQueueEmbed(GuildMusicManager manager, User selfBot, Member sender) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(selfBot.getName() + " Queue");

        List<AudioTrack> queue = manager.getScheduler().getQueue();

        if(queue.isEmpty() && (manager.getPlayer().getPlayingTrack() == null)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        String temp;
        for(int i = 0; i < queue.size(); i++) {
            temp = (i+1) + ". `" + queue.get(i).getInfo().title + "`\n";
            if(builder.toString().length() >= 1500) {
                temp = "..." + (queue.size() - i) + "tracks more.";
                builder.append(temp);
                break;
            }
            builder.append(temp);
        }

        embed.setDescription(builder.toString());
        embed.setColor(sender.getColor());
        embed.addField("Now playing", "`" + manager.getPlayer().getPlayingTrack().getInfo().title + "`", false);

        if(queue.isEmpty()) {
            embed.setDescription("No tracks in queue.");
        }
        return embed;
    }

    public static EmbedBuilder getSavedQueueEmbed(JSONObject json, String url) {
        List<Object> songList = json.getJSONArray("songsInSort").toList();

        EmbedBuilder builder = new EmbedBuilder();

        StringBuilder sBuilder = new StringBuilder();
        builder.setTitle("Saved queue (" + json.getString("queueName") + ")", url);
        for(int i = 0; i < songList.size(); i++) {
            sBuilder.append((i+1) + ". `" + songList.get(i).toString() + "`\n");
        }

        builder.setDescription(sBuilder.toString());
        builder.setColor(Color.GREEN);

        return builder;
    }
}

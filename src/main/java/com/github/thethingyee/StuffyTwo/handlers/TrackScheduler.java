package com.github.thethingyee.StuffyTwo.handlers;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    private final Guild guild;

    private boolean repeating = false;

    private EqualizerFactory equalizer;
    private int boostPercentage;

    private static final float[] BASS_BOOST = {0.15f, 0.14f, 0.13f, 0.14f, 0.05f, 0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.09f, 0.1f};


    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.guild = guild;
    }

    public boolean isRepeating() {
        return !repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            if(repeating) {
                player.startTrack(track.makeClone(), false);
                return;
            }
            if(queue.isEmpty()) {
                guild.getAudioManager().closeAudioConnection();
            }
            nextTrack();
        }
    }

    /**
     * Removes a specific track by it's index.
     *
     * @param queueNumber The index to get the track.
     * @param channel The channel where removeTrack is executed.
     */
    public void removeTrack(int queueNumber, TextChannel channel) {
        List<AudioTrack> tracks = new ArrayList<>(queue);

        AudioTrack tracktoBeRemoved = tracks.get((queueNumber - 1));
        queue.remove(tracks.get((queueNumber - 1)));

        channel.sendMessageEmbeds(Embeds.getRemoveTrackEmbed(tracktoBeRemoved, channel).build()).queue();
    }

    /**
     * Clears the queued tracks of a specific guild.
     *
     * @param guild The guild where the tracks will be cleared.
     */
    public void clearQueue(Guild guild) {
        List<AudioTrack> tracks = new ArrayList<>(queue);
        for (AudioTrack track : tracks) {
            queue.remove(track);
        }
    }

    /**
     * Sets the bass based on the percentage parameter.
     *
     * @param percentage The percentage of the bass boost
     */
    public void bassBoost(int percentage) {
        final int previousPercentage = this.boostPercentage;
        this.boostPercentage = percentage;

        if (previousPercentage > 0 && percentage == 0) {
            this.player.setFilterFactory(null);
            return;
        }

        if (previousPercentage == 0 && percentage > 0) {
            if (this.equalizer == null) {
                this.equalizer = new EqualizerFactory();
            }
            this.player.setFilterFactory(this.equalizer);
        }

        final float multiplier = percentage / 100.0f;
        for (int i = 0; i < BASS_BOOST.length; i++) {
            this.equalizer.setGain(i, BASS_BOOST[i] * multiplier);
        }

        this.boostPercentage = percentage;
    }

    public List<AudioTrack> getQueue() {
        return new ArrayList<>(queue);
    }

    public List<AudioTrack> getQueueWithPlayingTrack() {
        ArrayList<AudioTrack> queueList = new ArrayList<>(queue);
        queueList.add(0, player.getPlayingTrack());


        return queueList;
    }
}
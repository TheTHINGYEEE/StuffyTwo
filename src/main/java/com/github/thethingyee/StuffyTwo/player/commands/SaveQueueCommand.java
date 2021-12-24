package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.Embeds;
import com.github.thethingyee.StuffyTwo.handlers.aws.AWSHandler;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveQueueCommand extends Command {
    @Override
    public String getName() {
        return "savequeue";
    }

    @Override
    public String getDescription() {
        return "Saves the queue online so that its easier to be able to get tracks loaded before.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        if(args.length != 0) return;
        try {
            AWSHandler aws = new AWSHandler();
            if(aws.bucketIsNull()) event.getChannel().sendMessage("This command is currently disabled. Please contact the bot owner if you believe this was a mistake.").queue();
            File f = aws.savePublicQueue(manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler().getQueueWithPlayingTrack());
            String[] uploadedFile = aws.uploadFile(f);

            event.getChannel().sendMessageEmbeds(Embeds.getSavedQueueEmbed(new JSONObject(uploadedFile[1]), uploadedFile[0]).build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean useCommandHashing() {
        return false;
    }

    /**
     * Parses a JSON file to a readable object in Java.
     *
     * @param f The JSON file input.
     * @return A JSONObject (org.json) which you could use to easily read it.
     * @throws IOException The file can throw IOException when the file doesn't exist or etc.
     */
    private JSONObject parseJSON(File f) throws IOException {
        // https://stackoverflow.com/a/43091074
        // Not used yet but will use later.
        String content = new String(Files.readAllBytes(Paths.get(f.toURI())));
        return new JSONObject(content);
    }
}

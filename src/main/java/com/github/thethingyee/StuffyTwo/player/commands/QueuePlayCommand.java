package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.github.thethingyee.StuffyTwo.validators.URLValidate;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static com.github.thethingyee.StuffyTwo.StuffyTwo.logger;

public class QueuePlayCommand extends Command {
    @Override
    public String getName() {
        return "playqueue";
    }

    @Override
    public String getDescription() {
        return "Plays a queue playlist by URL.";
    }

    @Override
    public String getArguments() {
        return "<url>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        if(args.length == 0) {
            event.getChannel().sendMessage("Please specify a valid url!").queue();
            return;
        }
        if(args.length == 1) {
            if(!URLValidate.validate(args[0])) {
                event.getChannel().sendMessage("Please specify a valid url!").queue();
                return;
            }

            try {
                Document doc = Jsoup.connect(args[0]).timeout(15 * 1000).ignoreContentType(true).get();
                JSONObject json = new JSONObject(doc.text());
                JSONArray songList = json.getJSONArray("songsInSort");
                if(songList == null) {
                    event.getChannel().sendMessage("Songs not found. Are you sure you gave a correct url?").queue();
                    return;
                }
                for(int i = 0; i < songList.length(); i++) {
                    manager.getPlayerManager().loadAndPlay(event.getChannel(), (String)songList.get(i), event.getMember(), false);
                }
                event.getChannel().sendMessage("Added " + songList.length() + " songs in queue!").queue();
            } catch (IOException e) {
                logger.warning("Caught an error: " + e.getMessage());
            }
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
}

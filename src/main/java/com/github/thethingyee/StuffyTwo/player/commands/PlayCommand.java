package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.github.thethingyee.StuffyTwo.validators.URLValidate;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PlayCommand extends Command {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a track by a search query or a url.";
    }

    @Override
    public String getArguments() {
        return "<ytquery||ytlink>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {

        if(args.length == 0) {
            event.getChannel().sendMessage("Please specify a link or a song name!").queue();
            return;
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            builder.append(args[i] + " ");
        }

        String e = URLValidate.validate(builder.toString()) ? builder.toString().trim() : "ytsearch:" + builder;

        manager.getPlayerManager().loadAndPlay(event.getChannel(), e, event.getMember(), true);
    }
}

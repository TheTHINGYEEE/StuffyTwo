package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.StuffyTwo;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.github.thethingyee.StuffyTwo.validators.NumberValidator;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemoveTrackCommand extends Command {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Removes a track on the queue based on an index.";
    }

    @Override
    public String getArguments() {
        return "<pos||index>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {

        if(args.length == 0) {
            event.getChannel().sendMessage("Please specify the song number in the queue to be removed.").queue();
            return;
        }

        if(NumberValidator.validate(args[0])) {
            int num = Integer.parseInt(args[0]);
            manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler().removeTrack(num, event.getChannel());
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

    @Override
    public boolean isDisabled() {
        return false;
    }
}

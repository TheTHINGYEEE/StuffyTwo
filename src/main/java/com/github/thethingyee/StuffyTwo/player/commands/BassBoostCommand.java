package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.TrackScheduler;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.github.thethingyee.StuffyTwo.validators.NumberValidator;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BassBoostCommand extends Command {
    @Override
    public String getName() {
        return "bassboost";
    }

    @Override
    public String getDescription() {
        return "Bass boosts the entire audio player to a specific level.";
    }

    @Override
    public String getArguments() {
        return "<percentage>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        TrackScheduler scheduler = manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler();
        if(args.length == 0) {
            event.getChannel().sendMessage(event.getJDA().getSelfUser().getName() + "'s bass boost percentage is set to " + scheduler.boostPercentage + "%").queue();
            return;
        }
        if(args.length == 1) {
            if(!NumberValidator.validate(args[0]) && Integer.parseInt(args[0]) <= 200) {
                event.getChannel().sendMessage("You must input a number! (0-200)").queue();
                return;
            }
            scheduler.bassBoost(Integer.parseInt(args[0]));
            event.getChannel().sendMessage(event.getJDA().getSelfUser().getName() + "'s bass boost percentage is now set to " + args[0] + "%!").queue();
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

package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import com.github.thethingyee.StuffyTwo.validators.NumberValidator;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class VolumeCommand extends Command {
    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getDescription() {
        return "Sets the volume of the player.";
    }

    @Override
    public String getArguments() {
        return "<percentage>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        AudioPlayer player = manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getPlayer();
        if(args.length == 0) {
            event.getChannel().sendMessage(event.getJDA().getSelfUser().getName() + "'s volume is set to " + player.getVolume() + "%").queue();
            return;
        }
        if(args.length == 1) {
            if(!NumberValidator.validate(args[0]) && Integer.parseInt(args[0]) <= 200) {
                event.getChannel().sendMessage("You must input a number! (0-200)").queue();
                return;
            }
            player.setVolume(Integer.parseInt(args[0]));
            event.getChannel().sendMessage(event.getJDA().getSelfUser().getName() + "'s volume is now set to " + args[0] + "%!").queue();
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

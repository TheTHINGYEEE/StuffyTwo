package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class DisconnectCommand extends Command {
    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getDescription() {
        return "Disconnects the voice channel that the bot is in.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    private String[] phrases = {
            "Disconnecting voice channel because you actually are the one who's gay.",
            "I would roast you but my mom said burning trash isn't good.",
            "I would slap you but that will be animal abuse.",
            "Brother: You're adopted." +
                    "\n" +
                    "Sister: At least, they wanted me. >:D",
            "8==D ur mom haha",
            "Teacher: Are you talking back?" +
                    "\n" +
                    "Student: Yes, that's how this conversation works.",
            "Teacher: Have you done your homework?" +
                    "\n" +
                    "Student: Have you done my tests?" +
                    "\n" +
                    "Teacher: I have other children's test to check." +
                    "\n" +
                    "Student: I have other teacher's home work to do.",
            "Teacher: Can you touch and see God?" +
                    "\n" +
                    "Teacher: No, therefore God doesn't exist." +
                    "\n" +
                    "Student: Can you touch and see your brain?" +
                    "\n" +
                    "Student: No, therefore you have no brain."
    };

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        if (event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().closeAudioConnection();
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("or") && args[1].equalsIgnoreCase("gay")) {
                    int rndNum = new Random().nextInt(phrases.length);
                    event.getChannel().sendMessage(phrases[rndNum]).queue();
                    return;
                }
            }
            manager.getPlayerManager().getGuildAudioPlayer(event.getGuild()).getScheduler().getQueue().clear();
            event.getChannel().sendMessage("Disconnected from voice channel.").queue();
        }
    }
}

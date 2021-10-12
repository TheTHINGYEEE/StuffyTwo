package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.ColorHandler;
import com.github.thethingyee.StuffyTwo.handlers.ImageHandler;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.image.BufferedImage;

public class TestCommand extends Command {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "A simple test command for the developer.";
    }

    @Override
    public String getArguments() {
        return "idk";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {

        String id = "dV-dJevn6GI";

        if(args.length == 0) {
            event.getChannel().sendMessage("Please specify the kind of test.").queue();
            return;
        }

        if(args[0].equalsIgnoreCase("dominantcolor")) {
            event.getChannel().sendMessage("Getting dominant color of " + id + " using Color object.").queue();
            BufferedImage img = ImageHandler.getYoutubeThumbnail(id);

            long startTime = System.nanoTime();
            new ColorHandler(img).getDominantColor();
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;
            event.getChannel().sendMessage("Took approximately " + timeElapsed + "ms").queue();

            event.getChannel().sendMessage("Getting dominant color of " + id + " using raster.").queue();
            long startTime2 = System.nanoTime();
            new ColorHandler(img).getDominantColorByRaster();
            long endTime2 = System.nanoTime();
            long timeElapsed2 = (endTime2 - startTime2) / 1000000;
            event.getChannel().sendMessage("Took approximately " + timeElapsed2 + "ms").queue();

            return;
        }

        if(args[0].equalsIgnoreCase("url")) {
            manager.getPlayerManager().loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=HL8pbO3Ryc8", event.getMember(), true);
            event.getChannel().sendMessage("Executed test!").queue();
        }
    }
}

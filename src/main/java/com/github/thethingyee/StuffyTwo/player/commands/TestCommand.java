package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.handlers.ColorHandler;
import com.github.thethingyee.StuffyTwo.handlers.ImageHandler;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
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

        BufferedImage img = ImageHandler.getYoutubeThumbnail(id);
        String thumbnailUrl = ImageHandler.getYouTubeThumbnailURL(id);

        if(args[0].equalsIgnoreCase("dominantcolor")) {
            event.getChannel().sendMessage("Getting dominant color of " + id + " using Color object.").queue();
            long startTime = System.nanoTime();
            Color dominantColor = new ColorHandler(img).getDominantColor();
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;

            EmbedBuilder colorBuilder = new EmbedBuilder();
            colorBuilder.setColor(dominantColor);
            colorBuilder.setImage(thumbnailUrl);
            colorBuilder.setDescription("Color object: Took approximately " + timeElapsed + "ms");

            event.getChannel().sendMessage("Getting dominant color of " + id + " using raster.").queue();
            long startTime2 = System.nanoTime();
            int[] rasterDom = new ColorHandler(img).getDominantColorByRaster();
            long endTime2 = System.nanoTime();
            long timeElapsed2 = (endTime2 - startTime2) / 1000000;

            EmbedBuilder rasterBuilder = new EmbedBuilder();
            rasterBuilder.setColor(new Color(rasterDom[0], rasterDom[1], rasterDom[2]));
            rasterBuilder.setImage(thumbnailUrl);
            rasterBuilder.setDescription("Raster: Took approximately " + timeElapsed2 + "ms");

            event.getChannel().sendMessageEmbeds(colorBuilder.build(), rasterBuilder.build()).queue();

            return;
        }

        if(args[0].equalsIgnoreCase("url")) {
            manager.getPlayerManager().loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=HL8pbO3Ryc8", event.getMember(), true);
            event.getChannel().sendMessage("Executed test!").queue();
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

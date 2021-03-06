package com.github.thethingyee.StuffyTwo;

import com.github.thethingyee.StuffyTwo.handlers.ConfigurationHandler;
import com.github.thethingyee.StuffyTwo.handlers.aws.AWSHandler;
import com.github.thethingyee.StuffyTwo.player.CommandManager;
import com.github.thethingyee.StuffyTwo.player.commands.*;
import com.github.thethingyee.StuffyTwo.player.commands.canyoucrackthis.DanielContainer;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.logging.Logger;

public class StuffyTwo extends ListenerAdapter {

    private static MainManager mainManager;
    private static String prefix;

    public static Logger logger = Logger.getLogger(StuffyTwo.class.getName());

    public static void main(String[] args) throws Exception {

        JDA jda = JDABuilder.createDefault(ConfigurationHandler.getInstance().getProperty("bot.token"))
                .addEventListeners(new CommandManager())
                .build();

//        jda.getPresence().setPresence(Activity.streaming("on a garbage channel", "https://www.twitch.tv/thethingyeee"), false);
//        jda.getPresence().setPresence(Activity.playing("while on maintenance."), false);
        jda.getPresence().setPresence(Activity.listening("music while I'm being revamped."), true);
        new StuffyTwo().initialize();

    }

    private void initialize() {
        mainManager = new MainManager();
        prefix = ".";

        CommandManager.registerCommand(
                new ClearQueueCommand(),
                new PauseCommand(),
                new PlayCommand(),
                new RemoveTrackCommand(),
                new SkipCommand(),
                new TestCommand(),
                new LoopCommand(),
                new DisconnectCommand(),
                new HelpCommand(),
                new LyricsCommand(),
                new QueueCommand(),
                new VolumeCommand(),
                new SaveQueueCommand(),
                new QueuePlayCommand(),
                new BassBoostCommand(),
                new MD5Command(),
                new DanielContainer());

        logger.info("Successfully loaded!");
    }

    public static MainManager getMainManager() {
        return mainManager;
    }

    public static String getPrefix() {
        return prefix;
    }

}

/*
    90% of the code was written by me. (ThingyTV)
    You'll find the sources at the very top of a method/class if it is copied.
 */

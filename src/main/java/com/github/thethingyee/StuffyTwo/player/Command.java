package com.github.thethingyee.StuffyTwo.player;

import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {

    public abstract String getName();
    public abstract String getDescription();
    public abstract String getArguments();
    public abstract void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args);
    public abstract boolean isVisible();
    public abstract boolean useCommandHashing();

    public boolean hasName() {
        return getName() != null;
    }

    public boolean hasDescription() {
        return getDescription() != null;
    }
}

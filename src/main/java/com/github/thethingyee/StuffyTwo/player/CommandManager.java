package com.github.thethingyee.StuffyTwo.player;

import com.github.thethingyee.StuffyTwo.StuffyTwo;
import com.github.thethingyee.StuffyTwo.player.commands.MD5Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {

    private static ArrayList<Command> registeredCommands = new ArrayList<>();

    public static void registerCommand(Command... commands) {
        for(Command command : commands) {
            if(!(command.hasName() || command.hasDescription())) {
                throw new NullPointerException("Command is null!");
            }
            registeredCommands.add(command);

        }
    }

    public static ArrayList<Command> getRegisteredCommands() {
        return registeredCommands;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");

        MainManager manager = StuffyTwo.getMainManager();
        String prefix = StuffyTwo.getPrefix();

        if(!command[0].startsWith(prefix)) return;
        command[0] = command[0].replace(prefix, "");

        ArrayList<String> subListCommands = new ArrayList<>();
        for(int i = 1; i < command.length; i++) {
            subListCommands.add(command[i]);
        }
        String[] subCommands = subListCommands.toArray(new String[0]);

        for(Command c : registeredCommands) {
            if(c.useCommandHashing()) {
                if(!MD5Command.strToMD5Str(command[0]).equals(c.getName())) return;
                c.execute(event, manager, subCommands);
                return;
            }
            if(command[0].equalsIgnoreCase(c.getName())) {
                c.execute(event, manager, subCommands);
            }
        }
    }
}

package com.github.thethingyee.StuffyTwo.player;

import com.github.thethingyee.StuffyTwo.StuffyTwo;
import com.github.thethingyee.StuffyTwo.player.commands.MD5Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.github.thethingyee.StuffyTwo.StuffyTwo.logger;

public class CommandManager extends ListenerAdapter {

    private static ArrayList<Command> registeredCommands = new ArrayList<>();
    private static ArrayList<Command> disabledCommands = new ArrayList<>();

    public static void registerCommand(Command... commands) {
        for(Command command : commands) {
            if(!(command.hasName() || command.hasDescription())) {
                throw new NullPointerException("Command is null!");
            }
            if(command.isDisabled()) disabledCommands.add(command);
            registeredCommands.add(command);
        }
        if(disabledCommands.isEmpty()) return;
        StringBuilder disabledList = new StringBuilder();
        for(int i = 0; i < disabledCommands.size(); i++) {
            int disabledCommandTrueIndex = disabledCommands.size() - 1;
            disabledList.append("\"" + disabledCommands.get(i).getName()
                    + "\"" + ((disabledCommandTrueIndex == i) ? "" : ", "));
        }
        logger.warning("Disabled commands: " + disabledList.toString());
    }

//    public static void disableCommands(String... commandName) {
//
//        for(String name : commandName) {
//            for (Command registered : registeredCommands) {
//                if (!name.equalsIgnoreCase(registered.getName())) continue;
//                disabledCommands.add(registered);
//            }
//        }
//        if(disabledCommands.isEmpty()) return;
//        StringBuilder builder = new StringBuilder();
//        for(Command disabled : disabledCommands) {
//            builder.append(disabled.getName() + ", ");
//        }
//        logger.warning("Commands disabled: " + builder.toString());
//
//    }

    public static ArrayList<Command> getRegisteredCommands() {
        return registeredCommands;
    }

    public static ArrayList<Command> getDisabledCommands() {
        return disabledCommands;
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
                if(disabledCommands.contains(c)) {
                    event.getChannel().sendMessage("Sorry folk! This command is disabled by the author.").queue();
                    continue;
                }
                c.execute(event, manager, subCommands);
            }
        }
    }
}

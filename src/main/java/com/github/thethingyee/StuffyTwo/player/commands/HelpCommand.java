package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.StuffyTwo;
import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.CommandManager;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Lists all the commands and gives their usage.";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append("__**").append(event.getJDA().getSelfUser().getName()).append(" Help**__");
        builder.append("\nPlease contact the bot owner if there's any bugs.\n \n");
        for(Command cmd : CommandManager.getRegisteredCommands()) {
            if(!(cmd.hasDescription() && cmd.hasName() && cmd.isVisible())) continue;
            builder.append("`" + StuffyTwo.getPrefix() + cmd.getName() + " " + cmd.getArguments() + "` - " + cmd.getDescription() + "\n");
        }

        sendPrivateMessage(event.getAuthor(), builder.toString());
        event.getChannel().sendMessage("I have sent the help section of me to your DMs!").queue();
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean useCommandHashing() {
        return false;
    }

    private void sendPrivateMessage(User to, String msg) {
        to.openPrivateChannel().queue((privateChannel ->
            privateChannel.sendMessage(msg).queue()));
    }
}

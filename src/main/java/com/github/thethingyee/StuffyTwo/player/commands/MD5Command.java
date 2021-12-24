package com.github.thethingyee.StuffyTwo.player.commands;

import com.github.thethingyee.StuffyTwo.player.Command;
import com.github.thethingyee.StuffyTwo.player.manager.MainManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Command extends Command {
    @Override
    public String getName() {
        return "md5hash";
    }

    @Override
    public String getDescription() {
        return "Development purposes - Hashes a string to md5.";
    }

    @Override
    public String getArguments() {
        return "<string(nospaces)>";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, MainManager manager, String[] args) {
        if(args.length == 0) return;

        String md5 = strToMD5Str(args[0]);

        String msg = (md5 == null ? "MD5 Hashing failed!" : args[0] + ": " + md5);

        event.getChannel().sendMessage(msg).queue();

    }

    public static String strToMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(str.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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

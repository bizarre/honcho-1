package com.qrakn.honcho.command.example;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "honcho", permission = "honcho.example.command")
public class ExampleCommand  {

    public void execute(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "pussy got that wet wet got that drip drip got that super soakers");
    }

    @CommandMeta(label = "help", permission = "honcho.example.subcommand")
    public class HelpCommand extends ExampleCommand {

        public void execute(CommandSender sender) {
            sender.sendMessage(ChatColor.RED  + "honcho is the shit fam");
        }

    }

    @CommandMeta(label = "broadcast", permission = "honcho.example.subcommand")
    public class BroadcastCommand extends ExampleCommand {

        public void execute(CommandSender sender, String message) {
            Bukkit.broadcastMessage(ChatColor.AQUA + message);
        }

    }

}

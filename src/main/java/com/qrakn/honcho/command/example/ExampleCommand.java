package com.qrakn.honcho.command.example;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    @CommandMeta(label = { "gamemode", "gm" }, permission = "honcho.example.subcommand")
    public class GameModeCommand extends ExampleCommand {

        public void execute(CommandSender sender, GameMode gameMode) {
            if (gameMode == null) {
                sender.sendMessage(ChatColor.RED + "Game mode with that name not found.");
                return;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player.");
                return;
            }

            ((Player) sender).setGameMode(gameMode);

            sender.sendMessage(ChatColor.GRAY + "You set your game mode to " + gameMode.name());
        }

    }

}

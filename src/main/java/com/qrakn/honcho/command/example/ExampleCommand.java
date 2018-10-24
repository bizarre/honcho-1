package com.qrakn.honcho.command.example;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "msg", permission = "honcho.example.command")
public class ExampleCommand  {

    public void execute(CommandSender sender, Player player, String message) {

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found with that name.");
            return;
        }

        player.sendMessage(ChatColor.GRAY + "(" + sender.getName() + " -> " + player.getName() + ") " + message);
    }

}

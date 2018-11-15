package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(Bukkit.getPlayer(string));
    }

    @Override
    public <T> List<String> tabComplete(String string, Class<T> type) {
        List<String> completed = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(string.toLowerCase())) {
                completed.add(player.getName());
            }
        }

        return completed;
    }

}

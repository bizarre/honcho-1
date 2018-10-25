package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import org.bukkit.Bukkit;

public class WorldTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(Bukkit.getWorld(string));
    }
}

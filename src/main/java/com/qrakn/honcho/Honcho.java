package com.qrakn.honcho;

import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import com.qrakn.honcho.command.adapter.impl.IntegerTypeAdapter;
import com.qrakn.honcho.command.adapter.impl.PlayerTypeAdapter;
import com.qrakn.honcho.command.adapter.impl.StringTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Honcho implements Listener {

    private final JavaPlugin plugin;
    private final Map<Class, CommandTypeAdapter> adapters;
    private final Map<String, Object> commands;

    public Honcho(JavaPlugin plugin) {
        this.plugin = plugin;

        this.adapters = new HashMap<>();
        this.commands = new HashMap<>();

        registerTypeAdapter(Player.class, new PlayerTypeAdapter());
        registerTypeAdapter(String.class, new StringTypeAdapter());
        registerTypeAdapter(Integer.class, new IntegerTypeAdapter());

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String[] messageSplit = event.getMessage().substring(1).split(" ");
        Object command = commands.get(messageSplit[0].toLowerCase());

        String[] args = new String[0];
        if (messageSplit.length > 1) {
            args = new String[messageSplit.length - 1];
            System.arraycopy(messageSplit,1, args,0,messageSplit.length - 1);
        }

        if (command != null) {
            event.setCancelled(true);
            new HonchoExecutor(this, event.getPlayer(), command, args).execute();
        }
    }

    public void registerTypeAdapter(Class clazz, CommandTypeAdapter adapter) {
        adapters.put(clazz, adapter);
    }

    public CommandTypeAdapter getTypeAdapter(Class clazz) {
        return adapters.get(clazz);
    }

    public void registerCommand(Object object) {
        CommandMeta meta = object.getClass().getAnnotation(CommandMeta.class);

        if (meta == null) {
            throw new RuntimeException(new ClassNotFoundException(object.getClass().getName() + " is missing CommandMeta annotation"));
        }

        for (String label : meta.label()) {
            commands.put(label.toLowerCase(), object);
        }
    }

}

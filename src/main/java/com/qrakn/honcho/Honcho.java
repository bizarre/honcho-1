package com.qrakn.honcho;

import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import com.qrakn.honcho.command.adapter.impl.IntegerTypeAdapter;
import com.qrakn.honcho.command.adapter.impl.PlayerTypeAdapter;
import com.qrakn.honcho.command.adapter.impl.StringTypeAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
        Object command = null;
        String label = null;

        for (int remaining = messageSplit.length; remaining > 0; remaining--) {
            label = StringUtils.join(messageSplit, " ", 0, remaining);

            if (commands.get(label.toLowerCase()) != null) {
                command = commands.get(label.toLowerCase());
                break;
            }
        }

        if (command != null) {
            String[] labelSplit = label.split(" ");
            String[] args = new String[0];

            if (messageSplit.length != labelSplit.length) {
                int numArgs = messageSplit.length - labelSplit.length;
                args = new String[numArgs];
                System.arraycopy(messageSplit, labelSplit.length, args,0, numArgs);
            }

            event.setCancelled(true);
            new HonchoExecutor(this, messageSplit[0].toLowerCase(), event.getPlayer(), command, args).execute();
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

        for (String label : getLabels(object.getClass(), new ArrayList<>())) {
            commands.put(label.toLowerCase(), object);
        }

        if (meta.autoAddSubCommands()) {
            for (Class<?> clazz : object.getClass().getDeclaredClasses()) {
                if (clazz.getSuperclass().equals(object.getClass())) {
                    try {
                        registerCommand(clazz.getDeclaredConstructor(object.getClass()).newInstance(object));
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private List<String> getLabels(Class clazz, List<String> list) {
        List<String> toReturn = new ArrayList<>();
        Class superClass = clazz.getSuperclass();

        if (superClass != null) {
            CommandMeta meta = (CommandMeta) superClass.getAnnotation(CommandMeta.class);

            if (meta != null) {
                list = getLabels(superClass, list);
            }
        }

        CommandMeta meta = (CommandMeta) clazz.getAnnotation(CommandMeta.class);

        if (meta == null) {
            return list;
        }

        if (list.isEmpty()) {
            toReturn.addAll(Arrays.asList(meta.label()));
        } else {
            for (String prefix : list) {
                for (String label : meta.label()) {
                    toReturn.add(prefix + " " + label);
                }
            }
        }

        return toReturn;
    }

}

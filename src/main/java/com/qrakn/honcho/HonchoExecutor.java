package com.qrakn.honcho;

import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class HonchoExecutor {

    private final Honcho honcho;
    private final String label;
    private final CommandMeta meta;
    private final CommandSender sender;
    private final Object command;
    private final String[] args;

    public HonchoExecutor(Honcho honcho, String label, CommandSender sender, Object command, String[] args) {
        this.honcho = honcho;
        this.label = label;
        this.meta = command.getClass().getAnnotation(CommandMeta.class);
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public void execute() {

        if (!(sender.hasPermission(meta.permission()))) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return;
        }

        outer: for (Method method : command.getClass().getMethods()) {

            if (!(method.getDeclaringClass().equals(command.getClass()))) {
                continue;
            }

            if (method.getParameterCount() - 1 > args.length) {
                continue;
            }

            if (args.length != method.getParameterCount() - 1) {
                for (Method otherMethod : command.getClass().getMethods()) {
                    if (!(otherMethod.equals(method))) {
                        if (args.length - method.getParameterCount() > args.length - otherMethod.getParameterCount()) {
                            continue outer;
                        }
                    }
                }
            }

            if (method.getParameterCount() > 0 && method.getParameters()[0].getType().equals(CommandSender.class)) {
                List<Object> arguments = new ArrayList<>();
                Parameter[] parameters = method.getParameters();

                arguments.add(sender);

                for (int i = 1; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    CommandTypeAdapter adapter = honcho.getTypeAdapter(parameter.getType());

                    if (adapter == null) {
                        // TODO: throw error or log?
                        arguments.add(null);
                        continue;
                    }

                    if (i == (parameters.length - 1)) {
                        arguments.add(adapter.convert(StringUtils.join(args, " ", i-1, args.length), parameter.getType()));
                    } else {
                        arguments.add(adapter.convert(args[i-1], parameter.getType()));
                    }
                }

                try {
                    method.invoke(command, arguments.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                return;
            }

        }

        sender.sendMessage(getUsage());
    }

    //TODO: finish
    private String getUsage() {
        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.RED).append("Usage: /").append(label);

        Map<Integer, List<String>> arguments = new HashMap<>();

        for (Method method : command.getClass().getDeclaredMethods()) {
            Parameter[] parameters = method.getParameters();
            for (int i = 1; i < parameters.length; i++) {
                List<String> argument = arguments.getOrDefault(i - 1, new ArrayList<>());

                argument.add(parameters[i].getName());

                arguments.put(i - 1, argument);
            }
        }

        for (int i = 0; i < arguments.size(); i++) {
            List<String> argument = arguments.get(i);

            builder.append(" <").append(StringUtils.join(argument, "/")).append(">");
        }

        return builder.toString();
    }

}

package com.qrakn.honcho;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HonchoExecutor {

    private final Honcho honcho;
    private final CommandSender sender;
    private final Object command;
    private final String[] args;

    public HonchoExecutor(Honcho honcho, CommandSender sender, Object command, String[] args) {
        this.honcho = honcho;
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public void execute() {
        outer: for (Method method : command.getClass().getMethods()) {

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

        sender.sendMessage("usage msg todo xd");
    }

}

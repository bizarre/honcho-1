package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.CommandOption;
import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

public class CommandOptionTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        if (string.startsWith("-")) {
            return type.cast(new CommandOption(string.substring(1)));
        }

        return null;
    }

}

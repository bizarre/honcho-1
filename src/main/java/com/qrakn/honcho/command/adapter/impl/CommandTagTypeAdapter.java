package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.CommandTag;
import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandTagTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        if (string.startsWith("-")) {
            return type.cast(new CommandTag(string.substring(1)));
        }

        return null;
    }

}

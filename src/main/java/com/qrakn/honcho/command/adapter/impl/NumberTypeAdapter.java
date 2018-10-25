package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import java.text.NumberFormat;
import java.text.ParseException;
import org.bukkit.Bukkit;

public class NumberTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(String string, Class<T> type) {
        try {
            return type.cast(NumberFormat.getInstance().parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.GameMode;

public class GameModeTypeAdapter implements CommandTypeAdapter {

    private static final Map<String, GameMode> MAP = new HashMap<>();

    static {
        MAP.put("0", GameMode.SURVIVAL);
        MAP.put("s", GameMode.SURVIVAL);
        MAP.put("survival", GameMode.SURVIVAL);

        MAP.put("1", GameMode.CREATIVE);
        MAP.put("c", GameMode.CREATIVE);
        MAP.put("creative", GameMode.CREATIVE);
    }

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(MAP.get(string.toLowerCase()));
    }

}

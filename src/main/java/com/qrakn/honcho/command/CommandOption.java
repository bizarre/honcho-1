package com.qrakn.honcho.command;

import lombok.Getter;

public class CommandOption {

    @Getter private final String tag;

    public CommandOption(String tag) {
        this.tag = tag.toLowerCase();
    }

}

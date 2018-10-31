package com.qrakn.honcho.command;

import lombok.Getter;

public class CommandTag {

    @Getter private final String tag;

    public CommandTag(String tag) {
        this.tag = tag;
    }

}

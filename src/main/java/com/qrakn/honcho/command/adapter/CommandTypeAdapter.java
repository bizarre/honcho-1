package com.qrakn.honcho.command.adapter;

public interface CommandTypeAdapter {

    <T> T convert(String string, Class<T> type);

}

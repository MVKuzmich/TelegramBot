package com.kuzmich.enums;

import lombok.Getter;

@Getter
public enum ServiceCommands {
    HELP("/help", "List of commands"),
    REGISTRATION("/registration", "User registration"),
    CANCEL("/cancel", "Cancel current command"),
    START("/start", "Bot launching");

    private final String command;
    private final String description;


    ServiceCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public boolean equals(String command) {
        return this.getCommand().equals(command);
    }
}

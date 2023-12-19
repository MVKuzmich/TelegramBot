package com.kuzmich.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ServiceCommand {
    HELP("/help", "List of commands"),
    REGISTRATION("/registration", "User registration"),
    CANCEL("/cancel", "Cancel current command"),
    START("/start", "Bot launching");

    private final String command;
    private final String description;


    ServiceCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

   public static ServiceCommand fromText(String text) {
        return Arrays.stream(ServiceCommand.values())
                .filter(cmd -> cmd.getCommand().equals(text))
                .findFirst()
                .orElse(null);
   }
}

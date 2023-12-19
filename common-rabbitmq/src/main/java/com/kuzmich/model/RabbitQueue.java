package com.kuzmich.model;

import lombok.Getter;

@Getter
public enum RabbitQueue {
    DOC_MESSAGE_UPDATE("doc_message_update"), 
    PHOTO_MESSAGE_UPDATE("photo_message_update"),
    TEXT_MESSAGE_UPDATE("text_message_update"),
    DICE_MESSAGE_UPDATE("dice_message_update"),
    ANSWER_MESSAGE("answer_message");

    private final String name;
    RabbitQueue(String name) {
        this.name = name;
    }
}

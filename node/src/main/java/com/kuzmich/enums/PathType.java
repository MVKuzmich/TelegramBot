package com.kuzmich.enums;

import lombok.Getter;

@Getter
public enum PathType {
    GET_DOCUMENT("/file/document"),
    GET_PHOTO("/file/photo");

    private final String path;

    PathType(String path) {
        this.path = path;
    }

}

package com.alexsitiy.ideas.project.exception;

import lombok.Getter;

@Getter
public class UploadingFileException extends RuntimeException{

    private final String fileName;
    private final String contentType;

    public UploadingFileException(String message, String fileName, String contentType) {
        super(message);
        this.fileName = fileName;
        this.contentType = contentType;
    }
}

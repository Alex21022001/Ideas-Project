package com.alexsitiy.ideas.project.dto.error;

import com.alexsitiy.ideas.project.exception.UploadingFileException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class FileErrorResponse {

    @JsonProperty("error")
    private final FileError fileError;
    private final ErrorType errorType;

    public static FileErrorResponse of(UploadingFileException fileException) {
        return new FileErrorResponse(new FileError(
                fileException.getFileName(),
                fileException.getContentType(),
                fileException.getMessage()
        ), ErrorType.FILE_ERROR);
    }

    record FileError(String fileName, String contentType, String message) {
    }
}

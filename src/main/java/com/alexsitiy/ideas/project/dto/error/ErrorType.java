package com.alexsitiy.ideas.project.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ErrorType {
    @JsonProperty("credentials") BAD_CREDENTIALS,
    @JsonProperty("file") FILE_ERROR;
}

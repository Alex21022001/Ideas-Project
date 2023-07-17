package com.alexsitiy.ideas.project.dto;

import org.hibernate.envers.RevisionType;

public enum ActionType {
    CREATE,
    UPDATE,
    DELETE;

    public static ActionType getByRevisionType(RevisionType type) {
        return switch (type) {
            case ADD -> CREATE;
            case MOD -> UPDATE;
            case DEL -> DELETE;
        };
    }
}

package com.alexsitiy.ideas.project.entity;

import com.alexsitiy.ideas.project.entity.Revision;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.envers.RevisionType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@AllArgsConstructor
@Data
public class HistoryEntity<T> {
    private T entity;
    private Revision revision;
    private RevisionType revisionType;
}

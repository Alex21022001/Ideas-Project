package com.alexsitiy.ideas.project.repository.custom;

import com.alexsitiy.ideas.project.entity.HistoryEntity;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.Revision;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProjectHistoryRepositoryImpl implements ProjectHistoryRepository {

    private final EntityManager entityManager;

    @Override
    public Page<HistoryEntity<Project>> findAllProjectHistoryByUsername(String username, Pageable pageable) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery auditQuery = createAuditQueryForProjectHistory(username, auditReader)
                .addOrder(AuditEntity.revisionProperty("timestamp").desc())
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        List<Object[]> projects = auditQuery.getResultList();

        Long totalCount = (Long) createAuditQueryForProjectHistory(username, auditReader)
                .addProjection(AuditEntity.id().count())
                .getSingleResult();

        List<HistoryEntity<Project>> historyProjects = projects.stream()
                .map(objects -> {
                    Project project = (Project) objects[0];
                    Revision revision = (Revision) objects[1];
                    RevisionType revisionType = (RevisionType) objects[2];
                    return new HistoryEntity<>(project, revision, revisionType);
                })
                .toList();

        return new PageImpl<>(historyProjects, pageable, totalCount);
    }

    private AuditQuery createAuditQueryForProjectHistory(String username, AuditReader auditReader) {
        return auditReader.createQuery()
                .forRevisionsOfEntity(Project.class, false, true)
                .add(AuditEntity.revisionProperty("username").eq(username));
    }
}

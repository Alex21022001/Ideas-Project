package com.alexsitiy.ideas.project.repository;

import com.alexsitiy.ideas.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    Optional<Comment> findCommentByProjectIdAndUserId(Integer projectId, Integer userId);
}

package com.alexsitiy.ideas.project.dto;

import com.alexsitiy.ideas.project.entity.Status;

import java.util.List;

public record ProjectFilter(String title, List<Status> statuses) {
}

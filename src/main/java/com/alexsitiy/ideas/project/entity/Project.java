package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
@ToString(exclude = {"user"})
@EqualsAndHashCode(exclude = {"user"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title",nullable = false,unique = true)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "image_path",nullable = false)
    private String imagePath;

    @Column(name = "docs_path")
    private String docsPath;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}

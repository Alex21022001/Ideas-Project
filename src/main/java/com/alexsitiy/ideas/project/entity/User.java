package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = {"projects","password"})
@EqualsAndHashCode(exclude = {"projects"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname",nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Project> projects = new ArrayList<>();

    public void addProject(Project project){
        projects.add(project);
        project.setUser(this);
    }
}

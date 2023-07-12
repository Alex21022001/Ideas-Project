package com.alexsitiy.ideas.project;

import com.alexsitiy.ideas.project.config.JwtTokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtTokenProperties.class)
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class IdeasProjectApplication {

    public static void main(String[] args) {
        // TODO: 11.07.2023
        //  1) Check Like and Dislike method + tests
        //  2) Add new Table which contains Likes and Dislikes for each project and use it in order to set LOCK
        //  Create Reaction when a new Project is created
        //  Add Project's likes and dislikes in ProjectReadDto
        //  Add LOCK on Reaction while User is commenting on the Project
        //  Add Like-Dislike increment during commenting
        //  Change Like-Dislike Tests (change data.slq)
        //
        //
        //  3) Change UserVerify method. Change findById() to exists
        //  3) Add Project created_at in order to see a new Project and add title "NEW" on Front
        //  4) User's liked and disliked projects
        SpringApplication.run(IdeasProjectApplication.class, args);
    }

}

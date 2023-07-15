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
        //  Add Project.getImage() + Project.getDoc() + Tests
        //  Add ContentType for FileCheck
        //  Add User's history
        //  Change WAITING to IN_PROGRESS
        //
        //
        //  Add Project created_at in order to see a new Project and add title "NEW" on Front
        //  Add getProjectImage() and getProjectDoc()
        //
        //  Add caching on Project
        //
        SpringApplication.run(IdeasProjectApplication.class, args);
    }

}

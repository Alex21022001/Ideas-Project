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
        //  Add User's history
        //  Add Hibernate Envers
        //  1) Add Revision + Add Project Aud
        //  Show User's:
        //  1) Add Project
        //  2) Update Project
        //  3) Delete Project
        //
        //  Add Project created_at in order to see a new Project and add title "NEW" on Front
        //
        //  Add caching on Project
        //
        SpringApplication.run(IdeasProjectApplication.class, args);
    }

}

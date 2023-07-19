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
        //  Change Project.Status to separate Table (Project.Id,Status) +
        //  Add Listener to set default values in Project during Persist +
        //  Add Project.accept() & Project.reject() for Expert
        //  Add Optimistic LOCK and inform Expert of exception
        //  Forbid to change Project after it was estimated
        //
        //  Add Test on Project create (check Reaction, ProjectStatus creation)
        //  User.create() Test, whether Avatar exists or not
        //
        //  Add caching on Project
        //
        SpringApplication.run(IdeasProjectApplication.class, args);

    }

}

package com.alexsitiy.ideas.project;

import com.alexsitiy.ideas.project.config.JwtTokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(JwtTokenProperties.class)
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class IdeasProjectApplication {

    public static void main(String[] args) {
        // TODO: 11.07.2023
        //  *Advanced*
        //  Create notification via OnProjectEstimationListener
        //  Add Test
        //  Create notification on Comment
        //
        //  Change boolean to void in Project.comment() + Replace Project.existById() with Reaction.findById Check
        //
        //  Add Test on Project create (check Reaction, ProjectStatus creation)
        //  User.create() Test, whether Avatar exists or not
        //
        //  Add caching on Project
        //
        SpringApplication.run(IdeasProjectApplication.class, args);

    }

}

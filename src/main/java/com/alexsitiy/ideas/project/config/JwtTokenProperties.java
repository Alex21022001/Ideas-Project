package com.alexsitiy.ideas.project.config;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@NoArgsConstructor
@Data
public class JwtTokenProperties {

    private final String subject = "User details";

    private final String issuer = "Application name";

    private final String secret = "Here should be secret key";

    /**
     * set time in minutes
     */
    private final Long longevity = 60L;

}

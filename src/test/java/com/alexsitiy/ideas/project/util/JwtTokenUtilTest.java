package com.alexsitiy.ideas.project.util;

import com.alexsitiy.ideas.project.config.JwtTokenProperties;
import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilTest {
    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;
    @Mock(strictness = Mock.Strictness.LENIENT)
    private JwtTokenProperties jwtProperties;

    @BeforeEach
    void init(){
        doReturn("Test").when(jwtProperties).getSubject();
        doReturn("Tester").when(jwtProperties).getIssuer();
        doReturn(10L).when(jwtProperties).getLongevity();
        doReturn("Test").when(jwtProperties).getSecret();
    }

    @Test
    void generateToken() {
        String username = "test@gmail.com";
        SecurityUser user = new SecurityUser(1, username, "test", Collections.singleton(Role.USER));

        String token = jwtTokenUtil.generateToken(user);

        String actualResult = jwtTokenUtil.validateAndExtractUsername(token);
        assertEquals(username, actualResult);
    }

    @Test
    void shouldFailValidateAndExtractUsername() {
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0IiwidXNlcm5hbWUiOiJ0ZXN0QGdtYWlsLmNvbSIsImlzcyI6IlRlc3RlciIsImlhdCI6MTY4ODQ3OTIwNiwiZXhwIjoxNjg4NDc5MDAxfQ.qGenTATt1_eYQtvAOrSIPGouGhlG_s8Zj6chB5CaaF0";
        assertThrows(JWTVerificationException.class,()-> jwtTokenUtil.validateAndExtractUsername(expiredToken));
    }
}
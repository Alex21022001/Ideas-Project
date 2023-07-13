package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.dto.UserFullReadDto;
import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.mapper.Mapper;
import com.alexsitiy.ideas.project.mapper.UserFullReadMapper;
import com.alexsitiy.ideas.project.mapper.UserReadMapper;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final S3Service s3Service;

    private final UserReadMapper userReadMapper;
    private final UserFullReadMapper userFullReadMapper;

    public Optional<UserReadDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userReadMapper::map);
    }

    public Optional<UserFullReadDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(userFullReadMapper::map);
    }

    @Transactional
    public void updateAvatar(Integer id, MultipartFile image) {
        userRepository.findById(id)
                .ifPresent(user -> {
                    s3Service.upload(image, User.class)
                            .ifPresent(user::setAvatar);

                    userRepository.saveAndFlush(user);
                    log.debug("User's Avatar {} was update", user);
                });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(SecurityUser::of)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with username:" + username));
    }
}

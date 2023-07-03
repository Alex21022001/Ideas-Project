package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.mapper.UserReadMapper;
import com.alexsitiy.ideas.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserReadMapper userReadMapper;

    public Optional<UserReadDto> findByUsername(String username){
        return userRepository.findByUsername(username)
                .map(userReadMapper::map);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with username:" + username));
    }
}

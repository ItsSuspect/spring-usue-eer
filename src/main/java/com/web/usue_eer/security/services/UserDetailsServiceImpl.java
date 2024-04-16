package com.web.usue_eer.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.usue_eer.entities.User;
import com.web.usue_eer.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));

        return UserDetailsImpl.build(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Ошибка: Пользователь с id " + userId + " не найден"));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка: Пользователь с именем " + username + " не найден"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findByDisciplinesId(Long disciplineId) {
        return userRepository.findByDisciplineId(disciplineId);
    }

    public List<User> findUsersByGroupName(String groupName) {
        return userRepository.findUsersByGroupName(groupName);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("userAuthService")
public class UserAuthService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserAuthService(
            UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepo.findByEmailIgnoreCase(email);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("There no such user with id: " + id));
    }
}
package org.example.userauthservice_nov2025morning.services;

import org.example.userauthservice_nov2025morning.models.User;
import org.example.userauthservice_nov2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User findUserById(Long id) {
        Optional<User> userOptional = userRepo.findById(id);

        if(userOptional.isPresent()) {
            return userOptional.get();
        }

        return null;
    }
}

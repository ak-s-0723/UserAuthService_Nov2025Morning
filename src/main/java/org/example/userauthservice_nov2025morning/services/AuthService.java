package org.example.userauthservice_nov2025morning.services;

import org.example.userauthservice_nov2025morning.exceptions.PasswordMismatchException;
import org.example.userauthservice_nov2025morning.exceptions.UserAlreadyExistException;
import org.example.userauthservice_nov2025morning.exceptions.UserNotRegisteredException;
import org.example.userauthservice_nov2025morning.models.Role;
import org.example.userauthservice_nov2025morning.models.State;
import org.example.userauthservice_nov2025morning.models.User;
import org.example.userauthservice_nov2025morning.repos.RoleRepo;
import org.example.userauthservice_nov2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);

        if  (userOptional.isPresent()) {
          throw new UserAlreadyExistException("Please try different email Id");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password); //ToDo - to be encoded by Anurag on friday
        user.setName(name);
        user.setState(State.ACTIVE);
        user.setCreatedAt(new Date());

        Role role;

        Optional<Role> roleOptional = roleRepo.findByValue("NON_ADMIN");
        if(roleOptional.isEmpty()) {
            role = new Role();
            role.setValue("NON_ADMIN");
            role.setState(State.ACTIVE);
            role.setCreatedAt(new Date());
            roleRepo.save(role);
        }  else {
            role = roleOptional.get();
        }

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setRoles(roleList);
        return userRepo.save(user);
    }

    @Override
    public User login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);

        if  (userOptional.isEmpty()) {
          throw new UserNotRegisteredException("Please register first");
        }

        User user = userOptional.get();
        if(!password.equals(user.getPassword())) {
            throw new PasswordMismatchException("Please pass correct password");
        }

        //JWT creation on friday

        return user;
    }
}

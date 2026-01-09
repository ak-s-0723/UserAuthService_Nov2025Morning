package org.example.userauthservice_nov2025morning.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_nov2025morning.exceptions.PasswordMismatchException;
import org.example.userauthservice_nov2025morning.exceptions.UserAlreadyExistException;
import org.example.userauthservice_nov2025morning.exceptions.UserNotRegisteredException;
import org.example.userauthservice_nov2025morning.models.Role;
import org.example.userauthservice_nov2025morning.models.State;
import org.example.userauthservice_nov2025morning.models.User;
import org.example.userauthservice_nov2025morning.repos.RoleRepo;
import org.example.userauthservice_nov2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);

        if  (userOptional.isPresent()) {
          throw new UserAlreadyExistException("Please try different email Id");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
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
    public Pair<User,String> login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);

        if  (userOptional.isEmpty()) {
          throw new UserNotRegisteredException("Please register first");
        }

        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())) {
            throw new PasswordMismatchException("Please pass correct password");
        }

        //JWT creation on friday

//        String message = "{\n" +
//                "   \"email\": \"anurag@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2026\"\n" +
//                "}";
//
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());

        List<String> roles = new ArrayList<>();
        for(Role role : user.getRoles()) {
            roles.add(role.getValue());
        }
        claims.put("access",roles);
        Long currentTime = System.currentTimeMillis();
        claims.put("iat",currentTime);
        claims.put("exp",currentTime+100000);
        claims.put("issuer","scaler");

        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        return new Pair<>(user,token);
    }
}

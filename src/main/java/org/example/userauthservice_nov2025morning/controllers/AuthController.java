package org.example.userauthservice_nov2025morning.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_nov2025morning.dtos.LoginRequestDto;
import org.example.userauthservice_nov2025morning.dtos.RoleDto;
import org.example.userauthservice_nov2025morning.dtos.SignupRequestDto;
import org.example.userauthservice_nov2025morning.dtos.UserDto;
import org.example.userauthservice_nov2025morning.exceptions.PasswordMismatchException;
import org.example.userauthservice_nov2025morning.exceptions.UserAlreadyExistException;
import org.example.userauthservice_nov2025morning.models.Role;
import org.example.userauthservice_nov2025morning.models.User;
import org.example.userauthservice_nov2025morning.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    //signup
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        User user = authService.signup(signupRequestDto.getName(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword());

        UserDto userDto = from(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }


    //login
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User,String> p= authService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword());

        String token = p.b;
        User user = p.a;
        UserDto userDto = from(user);


        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        List<RoleDto> roleDtoList = new ArrayList<>();
        for(Role role : user.getRoles()) {
            RoleDto roleDto = new RoleDto();
            roleDto.setValue(role.getValue());
            roleDtoList.add(roleDto);
        }
        userDto.setRoles(roleDtoList);
        return userDto;
    }
}


//UserAlreadyExistException  - BAD_REQUEST
//
//UserNotRegisteredException - NOT_FOUND
//
//PasswordMismatchException  - BAD_REQUEST
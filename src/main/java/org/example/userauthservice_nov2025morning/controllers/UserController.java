package org.example.userauthservice_nov2025morning.controllers;

import org.example.userauthservice_nov2025morning.dtos.RoleDto;
import org.example.userauthservice_nov2025morning.dtos.UserDto;
import org.example.userauthservice_nov2025morning.models.Role;
import org.example.userauthservice_nov2025morning.models.User;
import org.example.userauthservice_nov2025morning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public UserDto getUserDetails(@PathVariable Long id) {
       User user = userService.findUserById(id);
       return from(user);
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

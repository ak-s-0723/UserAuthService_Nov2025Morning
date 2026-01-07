package org.example.userauthservice_nov2025morning.services;

import org.example.userauthservice_nov2025morning.models.User;

public interface IAuthService {

    User signup(String name,String email,String password);

    User login(String email,String password);
}

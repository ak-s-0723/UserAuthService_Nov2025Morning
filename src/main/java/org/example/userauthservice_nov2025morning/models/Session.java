package org.example.userauthservice_nov2025morning.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Session extends BaseModel {
    @ManyToOne
    private User user;

    private String token;
}


//1                      1
//Session              user
//m                       1

//m : 1

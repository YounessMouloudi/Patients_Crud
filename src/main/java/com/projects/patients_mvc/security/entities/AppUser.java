package com.projects.patients_mvc.security.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/* hna ghadi nkhadmo b */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AppUser {

    @Id
    private String userId;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    /* un user peut avoir plusieurs roles et un role peut attribué a plusieurs users alors dans ça
     on utilise une relation de type Many To Many
    */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppRole> roles;

}

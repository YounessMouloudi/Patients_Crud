package com.projects.patients_mvc.security.service;

import com.projects.patients_mvc.security.entities.AppRole;
import com.projects.patients_mvc.security.entities.AppUser;

public interface AccountService {

    AppUser addNewUser(String username, String password, String email, String confirmPassword);
    AppRole addNewRole(String role);

    void addRoleToUser(String username, String role);
    void removeRoleFromUser(String username, String role);

    AppUser loadUserByUsername(String username);
}

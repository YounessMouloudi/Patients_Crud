package com.projects.patients_mvc.security.service;

import com.projects.patients_mvc.security.entities.AppRole;
import com.projects.patients_mvc.security.entities.AppUser;
import com.projects.patients_mvc.security.reposi.AppRoleRepository;
import com.projects.patients_mvc.security.reposi.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional // hadi pour demander a spring de gérer les transactions des méthodes
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    /* hna derna l'injection de dépendances ima tandiroha b Autowired wla b un Constructeur avec args
    wla n9adro ndiroha b l'annotation @AllArgsConstructor */
    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
                              PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser != null) throw new RuntimeException("This User Already exists");
        if( !password.equals(confirmPassword) ) throw new RuntimeException("Password not Match ConfirmPassword");

        appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = appRoleRepository.findById(role).orElse(null);

        if(appRole != null) throw new RuntimeException("This role Already exists");
        appRole = AppRole.builder().role(role).build();

        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {

        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();

        appUser.getRoles().add(appRole);
        /* on a la méthode transactionnel c'est pour ça on utilise pas cette ligne ci-dessous car il fois
        appUser add appRole il va mettre a jour les données automatiquement sans le fair save */
        // appUserRepository.save(appUser);
    }

    @Override
    public void removeRoleFromUser(String username, String role) {

        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();

        appUser.getRoles().remove(appRole);

    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);
    }
}

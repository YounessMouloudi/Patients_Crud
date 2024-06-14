package com.projects.patients_mvc.security;

import com.projects.patients_mvc.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity  // pour activer la sécurité web
@EnableMethodSecurity(prePostEnabled = true) // hadi tahia tan3tiw biha les authorizations pour les urls li 3ndna
public class SecurityConfig {

    /* Lorsque vous utilisez @Autowired, vous indiquez à Spring qu'il doit vous fournir automatiquement
    les dépendances nécessaires à votre classe, au lieu de les créer manuellement. mais Autowired not recommended
    mn l'a7ssan nb9aw nssta3mlo constructeur par arg aw @AllArgsConstructor
    */
    @Autowired // hadi pour l'injection de dépendences
    private PasswordEncoder passwordEncoder; // hadi tatssma instance de class PasswordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    /* had annotation Bean tat3ni mnin ghandémariw l'application ga3 les méthodes li 3ndhom Bean ghay t éxecutaw
    au démarage de l'application */
    // @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {

        String pwd = passwordEncoder.encode("aaaa");
        System.out.println(pwd); // hna kol mera executina ghadi yhachi had lcode w machi howa li kan 9bal

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                /* hna ghadi na3tiwh la list dial les utilisateurs li bghinahom y accédiw l'app
                   ay tancréyiw des users avec les roles dialhom f la mémoire dial Spring sécurity mais had la
                   mémoire une fois tanssado l'app taymchi ya3ni darori f lewal bach tdémari l'app khass t éxécuté
                   had la méthode w aprés 7abssha bach machi kol mera y3awd y exécutiha => commmenter @Bean

                   hadik User c'est une class f spring security

                   spring security f l password utilise un Hashcode ay had l password li tat3tih tayCodih
                   w ta mnin tatbghi dkhal password f login tayHasher dak lcode w tay9arno m3a li 3ando donc
                   bach tgol l spring security bila password mat codihech tadir hadi {noop} donc pass ghayb9a aaaa
                */
                User.withUsername("user1").password("{noop}aaaa").roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("aaaa")).roles("USER").build(),
                User.withUsername("admin").password(pwd).roles("USER", "ADMIN").build()
        );

        return inMemoryUserDetailsManager;
    }

    // @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){ // datasource hia l BD dialna
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

//        httpSecurity.formLogin(Customizer.withDefaults()); // hadi login par défault dial Spring

        /* hadi login li saybna 7na => w ila darti login dialk nta darori khass ta3ti l permission
        tal webjars bach ytaba9 css aw base donné mhm ayi fichier khass ama f login dial Spring taykon khdam
        dakchi mzn */
        httpSecurity.formLogin(form -> form.loginPage("/login").permitAll());
        httpSecurity.authorizeHttpRequests(css -> css.requestMatchers("/webjars/**","/h2-console/**")
                .permitAll());

        httpSecurity.rememberMe( remember -> remember.key("Aso9k;wF3-d")); // hadi dial checkbox remember me
        httpSecurity.logout(Customizer.withDefaults());

       // httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN"));
       // httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/user/**").hasRole("USER"));
        /* ça la sécurity basé sur la configuration càd ay tatconfiguré la sécurity b had la méthode
           authorizeHttpRequests had l9adia mzn f kolchi mais naf3a bzf w wahd lhaja par ex: ila 3tawk chi app
           déja wajda w galo lik sécurisi lina les urls hadi ghatnaf3ak bla mat9ad f code source ay l'controller
        */

        httpSecurity.authorizeHttpRequests(
                // (auth) -> auth.requestMatchers("/index").permitAll().anyRequest().authenticated()
                (auth) -> auth.anyRequest().authenticated()
                /* hadi tat3ni ana tous les requests Http darori khass t authentifia 3ad ymkan t accéder lihom
                hit derna anyRequest() kima kant */
        );
        httpSecurity.exceptionHandling(exception -> exception.accessDeniedPage("/notAuthorized"));
        httpSecurity.userDetailsService(userDetailsServiceImpl);

        return httpSecurity.build();

    }

}



/*
il existe plusieurs stratégies dans spring security pour préciser les utilisateurs et les roles
mais nous allons traité 3 stratégies principales :

    1 - InMemoryAuthentication : càd les utilisateurs qui ont le droit de l'accées (roles) a l'app il est stocker
        dans la mémoire
    2 - JdbcAuthentication : càd les utilisateurs qui ont le droit de l'accées (roles) a l'app il est stocker dans
        une BD Relationnelle
    3 - UserDétailsService : c'est la plus générale càd on va créé les utilisateurs et les roles au sein de la
        couche service de l'application


spring sécurity tay3tik tsécuriser l'app b anwa3 mokhtalifa dial la sécurisation ay b ayi methode bghiti nta
par ex : kayna sécurité par configuration w li hia li tadirha b la methode authorizeHttpRequests w kayna sécurité
par annotation w hia li tankhadmo biha b @PreAuthorize("hasRole('')") or @PreAuthorize("hasAuthority('')")


il y a 2 méthodes d'authentication :
    - authentication stateFull : les données de la session sont enregistrées coté serveur d'authentification

      alors hna ghanchra7 kifach taytra : hia mnin taydakhal l user ma3lomat dialo f login spring sécurity tay
      générer wahd l cookie li taykon fih wahd idSession khass bik nta bach tay3arfak serveur w ayi action dertiha
      f l'app taytseft m3aha dak l sessionId w had l cookie li fih sessionID maymkanch t9rah b Javascript aw
      décodih taykon ghi http li 3ndo l7a9 y9rah w yssefto ay (HttpOnly)

    - authentication stateLess : les données de la session sont enregistrées dans un jeton d'authentification
      délivré au client
      alors hna ghanchra7 kifach taytra : hia mnin taydakhal l user ma3lomat dialo f login spring sécurity tay
      générer wahd Token JWT tayt7at f local storage had token khass bik nta bach tay3arfak serveur w ayi action
      dertiha f l'app taytseft m3aha dak l token hna serveur taymchi y9alab dak token li tsseft wach howa le meme
      li f local storage mnin tayl9ah howa nafsso tay authorize lik la requete li tlabtiha w hadchi taydar f
      coté client b Angular w kda ... w hadi tatkon hssan mn statFull li tatkon par défault hit f stateFull
      taykon dak CSRF w hada ymkan yat hacka

mnin tan3ayto l un class A wesst class B ay tancréyiw instance de class A taytssma dépendances hna ghadi nsta3mlo
l'injection de dépendances ima b @Autowired aw b un constructeur avec arg de class A
*/

package com.projects.patients_mvc;

import com.projects.patients_mvc.entities_model.Patient;
import com.projects.patients_mvc.repositories_dao.PatientRepository;
import com.projects.patients_mvc.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication implements CommandLineRunner {

	@Autowired // on utilise Autowired pour fair l'injection de dépendences
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(PatientsMvcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//			patientRepository.save( new Patient(null,"Ali",new Date(),false,10) );
//			patientRepository.save( new Patient(null,"Hassan",new Date(),true,25) );
//			patientRepository.save( new Patient(null,"Sara",new Date(),true,12) );
//			patientRepository.save( new Patient(null,"Noha",new Date(),false,20) );

			patientRepository.findAll().forEach(patient -> {
				System.out.println(patient.getNom());
			});

			// En peut créé un nouveau patient en utilisant l'annottation @Builder
			// Patient patient = Patient.builder()
			//		.nom("yassine")
			//		.DateNaissance(new Date())
			//		.malade(true)
			//		.score(37)
			//		.build();
	}

	/* had PassowrdEncoder tandiroha ila bghina nsta3mlo chi codage dial password mn ghir dial spring li par défault
   	   hna ghadi nsta3mlo had BCrypt bach ycoder lina password w li howa a9wa wahd kayn db f codage => 3lach howa
   	   a9wa wahd hit f kol mera tat exécuté l'app dak lpassword tay3awd ycodih w machi nafss lcodage li kan 9bal
   	   ay kol mera taycodih f chkal w hadchi li taykhih s3ib chi wahd ykhtar9o w taykhlih A9wa wahd kayn db f codage
	*/
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	CommandLineRunner commandLineRunnerJdbcUsers(JdbcUserDetailsManager jdbcUserDetailsManager) {

		PasswordEncoder passwordEncoder = passwordEncoder();

		/* hna f JDBC ghadi ncréyiw les users f table users w les roles dialhom f table authorities li ghatcréa
		   mn schema.sql ay hna ghadi n3tamdo 3la BD Relationnells machi mémoire w hadchi li khass ykon

		   mhm ila bghiti tkhdam b autorities ghadir authorities b7al li dary lta7et mhm homa rah b7al b7al
		   ta9riban ghir f table ila derti role ghat sauvegarda f table ROLE_ADMIN or ROLE_USER
		   mais la khdemti b authorities ghat sauvegarda f table ghir ADMIN or USER
		*/

		return args -> {

			UserDetails user1 = jdbcUserDetailsManager.loadUserByUsername("user1");

			if(user1 == null) {
				jdbcUserDetailsManager.createUser(
					User.withUsername("user1").password(passwordEncoder.encode("aaaa"))
					.roles("USER").build()
				);
//				jdbcUserDetailsManager.createUser(
//					User.withUsername("user1").password(passwordEncoder.encode("aaaa"))
//					.authorities("USER").build()
//				);
			}

			UserDetails user2 = jdbcUserDetailsManager.loadUserByUsername("user2");

			if(user2 == null) {
				jdbcUserDetailsManager.createUser(
					User.withUsername("user2").password(passwordEncoder.encode("aaaa"))
					.roles("USER").build()
				);
//				jdbcUserDetailsManager.createUser(
//					User.withUsername("user2").password(passwordEncoder.encode("aaaa"))
//					.authorities("USER").build()
//				);
			}

			UserDetails admin = jdbcUserDetailsManager.loadUserByUsername("admin");

			if(admin == null) {
				jdbcUserDetailsManager.createUser(
					User.withUsername("admin").password(passwordEncoder.encode("aaaa"))
					.roles("USER","Admin").build()
				);
//				jdbcUserDetailsManager.createUser(
//					User.withUsername("admin").password(passwordEncoder.encode("aaaa"))
//					.authorities("USER","Admin").build()
//				);
			}
		};
	}

	// @Bean
	// hadi pour UserDetailService et li lfo9 pour JDBC
	CommandLineRunner commandLineRunnerUserDetailsService(AccountService accountService) {

        return args -> {
			accountService.addNewRole("USER");
			accountService.addNewRole("ADMIN");

			accountService.addNewUser("user1","aaaa","user1@gmail.com","aaaa");
			accountService.addNewUser("user2","aaaa","user2@gmail.com","aaaa");
			accountService.addNewUser("admin","aaaa","admin@gmail.com","aaaa");

			accountService.addRoleToUser("user1","USER");
			accountService.addRoleToUser("user2","USER");
			accountService.addRoleToUser("admin","USER");
			accountService.addRoleToUser("admin","ADMIN");
		};
    };

	//@Bean  // hna ghadi ncommentiw had bean bach mayb9ach kol mera y executé lina had lméthode ay maghayb9ach y
			//	ajouté lina had les données
//	CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
//		return args -> {
//			patientRepository.save( new Patient(null,"Ali",new Date(),false,10) );
//			patientRepository.save( new Patient(null,"Hassan",new Date(),true,25) );
//			patientRepository.save( new Patient(null,"Sara",new Date(),true,12) );
//			patientRepository.save( new Patient(null,"Noha",new Date(),false,20) );
//
//			patientRepository.findAll().forEach(patient -> {
//				System.out.println(patient.getNom());
//			});
//		};
//	}

}

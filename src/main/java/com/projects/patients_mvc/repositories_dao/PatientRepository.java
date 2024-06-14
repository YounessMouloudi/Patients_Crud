package com.projects.patients_mvc.repositories_dao;

import com.projects.patients_mvc.entities_model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Page<Patient> findByNomContains(String kw, Pageable pageable);
    /* hadi methode nafss hadi li lta7t ghi hna redina List b Page w zdna Pageable bach nkhadmo b la pagination */


    /* hadi methode dial Jpa howa taygénérer lik sql dialha 3an tari9 les attributs li 3andak =>
       tat9alab 3la les Patients b nom dialhom

       List<Patient> findByNomContains(String kw);
     */

    /* hadi methode tadir nafss hadchi li lfo9 mais hna 7na bghina n3tiwha smia khassa w la bghiti dir
       hadchi khask ta3tiha query w param hit jpa maghay3rafch ygénérer lik sql
       => Query utilise HQl(langage d'interrogation d'Hibernate) pour générer la requete sql

       @Query("select p from Patient p where p.nom like :x ")
       List<Patient> search(@Param("x") String kw);
    */

}

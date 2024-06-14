package com.projects.patients_mvc.entities_model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Entity // drna hadi bach ywali had lclass entity JPA
@Data // hadi tandir lina getters et setters et constructeur par déf sans argument
@AllArgsConstructor // hadi tadir lina un constructeur avec tous les args
@NoArgsConstructor // hadi tadir lina un constructeur par déf sans arg hit construct dial Data protected
//@Builder
// /* had Builder dial lombok ymkan lina ncréyiw biha un nouveau objet tandiroha f PatientsMvcApplication hit ta
// traja3 lina un constructeur avec args et dans PatientsMvcApplication => tandiro
//  Patient p = p.builder().nom("ali").DateNaissance(new Date()).malade(true).score(22).build()
//  rah dayr tema kifach ncréyiw un objet avec cette annotation */

public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min= 3, max = 30)
    private String nom;

    @NotNull
    @Temporal(TemporalType.DATE) // hadi drnaha bach mnin tandiro new date() taywali ghir Y/m/d
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    // hadi drnaha bach t7awal lina date dial input l had date hit les données matay tsauvegardawch w tay3tina 404
    private Date DateNaissance;

    private boolean malade;

    @DecimalMin("5")
    private int score;

}

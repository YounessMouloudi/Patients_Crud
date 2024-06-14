package com.projects.patients_mvc.web;

import com.projects.patients_mvc.entities_model.Patient;
import com.projects.patients_mvc.repositories_dao.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    /* hna ila kona derna f jdbcUserDetails authorities khass tdir hna aw f ayi blassa khassha
     Authorisation b7al f les templates ghadir hasAuthority w ila derti roles ghadir hasRole
     */

    @GetMapping("/user/index")
    @PreAuthorize("hasRole('USER')")
    //    @PreAuthorize("hasAuthority('USER')")
    public String patients(Model model, HttpServletRequest request,
       @RequestParam(name = "page", defaultValue = "0") int page,
       @RequestParam(name = "size",defaultValue = "5") int size,
       @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        /* had les parametres(page,size,keyword) kon kena bghina ndirohom b servlet kan khassn ndéclariw
           HttpServletRequest request => aprés wesst lfunction ghandiro :
           int page = Integer.parseInt(request.getParameter("page"))
           int size = Integer.parseInt(request.getParameter("size"))
           String keyword = request.getParameter("keyword")
           mais db spring boot ssahal 3lina l9adia bzf
        */

        // mnin tandiro had pageRequest bach ndiro l pagination khass nbadlo List l Page
        Page<Patient> patients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listPatients",patients);
        model.addAttribute("pages",new int[patients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);

        return "patients"; // ça return un view w li hia patients.html
    }

    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    //    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(Long id,String keyword,int page) {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    //    @PreAuthorize("hasAuthority('USER')")
    public String home() {
        return "redirect:/user/index";
    }

    @GetMapping("/patients")
    @ResponseBody
    public List<Patient> listPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ADMIN')")
    //    @PreAuthorize("hasAuthority('ADMIN')")
    public String createPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/save") // hna drna RequestParam bach nrécupériw les données li 3ndna f url
    @PreAuthorize("hasRole('ADMIN')")
    //    @PreAuthorize("hasAuthority('ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword)
    {
        if (bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public String editPatient(Model model, Long id,String keyword,int page) {
        Patient patient = patientRepository.findById(id).orElse(null);

        if(patient == null) throw new RuntimeException("Patient Not Exist");
        model.addAttribute("patient", patient);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";
    }
}

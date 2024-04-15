package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.entities.Information;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.InformationRequest;
import com.web.usue_eer.security.services.DisciplineService;
import com.web.usue_eer.security.services.InformationService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import com.web.usue_eer.security.services.UserDisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/portal/discipline")
public class InformationController {
    private final InformationService informationService;
    private final UserDisciplineService userDisciplineService;
    private final UserDetailsServiceImpl userDetailsService;
    private final DisciplineService disciplineService;

    @Autowired
    public InformationController(InformationService informationService, UserDisciplineService userDisciplineService, UserDetailsServiceImpl userDetailsService, DisciplineService disciplineService) {
        this.informationService = informationService;
        this.userDisciplineService = userDisciplineService;
        this.userDetailsService = userDetailsService;
        this.disciplineService = disciplineService;
    }

    @GetMapping("/{disciplineId}/information")
    public String getDisciplineInformation(Model model, @PathVariable Long disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());

        Information information = informationService.findInformationByDisciplineId(disciplineId)
                .orElseGet(() -> {
                    Information defaultInformation = new Information();
                    defaultInformation.setInformationOfDiscipline("Преподаватель скоро предоставит информацию");
                    defaultInformation.setInformationOfTeacher("Преподаватель скоро предоставит информацию");
                    defaultInformation.setContacts("Преподаватель скоро предоставит информацию");
                    return defaultInformation;
                });

        model.addAttribute("information", information);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "discipline-tabs/discipline-information/discipline-information");
        return "index";
    }

    @GetMapping("/{disciplineId}/information/edit-information")
    public String getEditInformation(Model model, @PathVariable Long disciplineId) {
        Optional<Information> informationOptional = informationService.findInformationByDisciplineId(disciplineId);
        Information information = informationOptional.orElse(new Information());

        model.addAttribute("information", information);
        model.addAttribute("content", "discipline-tabs/discipline-information/edit-information");
        return "index";
    }

    @PostMapping("/{disciplineId}/information/edit-information")
    public ResponseEntity<Void> editInformation(@PathVariable Long disciplineId, @RequestBody InformationRequest informationRequest) {
        Discipline discipline = disciplineService.findDisciplineById(disciplineId);
        Information information = new Information(
                informationRequest.getInformationOfDiscipline(),
                informationRequest.getInformationOfTeacher(),
                informationRequest.getContacts(),
                discipline
        );

        if (informationRequest.getId() != null) {
            information.setId(informationRequest.getId());
        }

        informationService.saveInformation(information);
        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}

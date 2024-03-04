package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.payload.request.DisciplineRequest;
import com.web.usue_eer.payload.response.MessageResponse;
import com.web.usue_eer.security.services.DisciplineService;
import com.web.usue_eer.security.services.GroupService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TeacherController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DisciplineService disciplineService;
    @GetMapping("/teacher/new-discipline")
    public String getNewDiscipline() {
        return "discipline_builder";
    }

//    @PostMapping("/teacher/new-discipline")
//    public ResponseEntity<?> createDiscipline(@Valid @RequestBody DisciplineRequest disciplineRequest) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
////        User user = userDetailsService.findUserByUsername(disciplineRequest.getGroup());
//
//        Discipline discipline = new Discipline(disciplineRequest.getName(), username);
//        disciplineService.saveDiscipline(discipline);
//
//        user.addDiscipline(discipline);
//        userDetailsService.saveUser(user);
//        return ResponseEntity.ok().body(new MessageResponse("Новая дисциплина создана"));
//    }
}

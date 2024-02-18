package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.repository.DisciplineRepository;
import com.web.usue_eer.repository.UserRepository;
import com.web.usue_eer.security.services.DisciplineService;
import com.web.usue_eer.security.services.GroupService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class PortalController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("/portal")
    public String index(Model model) { //Todo: Не работает вывод дисциплин
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        Set<Discipline> disciplines = user.getDisciplines();
        System.out.println(disciplines);
        model.addAttribute("disciplines", disciplines);
        return "index";
    }
}